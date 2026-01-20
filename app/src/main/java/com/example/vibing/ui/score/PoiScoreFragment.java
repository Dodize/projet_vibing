package com.example.vibing.ui.score;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.vibing.R;
import com.example.vibing.databinding.FragmentPoiScoreBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.example.vibing.models.Poi;
import com.example.vibing.models.QuizQuestion;
import com.example.vibing.models.Bonus;
import com.example.vibing.models.BonusType;
import com.example.vibing.repository.QuizRepository;
import com.example.vibing.ui.score.ScoreViewModel;
import com.example.vibing.ui.camera.CameraCaptureFragment;
import com.example.vibing.ui.quiz.QuizResultDialog;

import androidx.fragment.app.FragmentResultListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import com.google.firebase.firestore.FirebaseFirestore;

public class PoiScoreFragment extends Fragment {

    private FragmentPoiScoreBinding binding;
    private PoiScoreViewModel poiScoreViewModel;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private QuizRepository quizRepository;
    private AlertDialog loadingDialog;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    
    // Team names cache
    private java.util.Map<Integer, String> teamNamesCache = new java.util.HashMap<>();
    
    // POI data passed from HomeFragment
    private String poiName;
    private String poiId; // Actual POI ID from Firebase
    private float poiLatitude;
    private float poiLongitude;
    private int poiScore;
    private String poiOwningTeam;
    private String userTeamId; // Current user's team
    
    // Bonus management
    private Bonus activeFreezeBonus = null;
    private Bonus activeBoostBonus = null;
    private int userMoney = 0;
    
    // Store current zone score for result dialog
    private int currentZoneScore = 100; // Default value

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i("PoiScoreFragment", "onCreateView() called");
        poiScoreViewModel = new ViewModelProvider(this).get(PoiScoreViewModel.class);
        quizRepository = new QuizRepository(requireContext());
        Log.i("PoiScoreFragment", "QuizRepository created");

        binding = FragmentPoiScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        // Enable back button in toolbar
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Listen for camera capture results
        getParentFragmentManager().setFragmentResultListener("cameraResult", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("quizShouldStart", false)) {
                    // Show success message first
                    Toast.makeText(getContext(), "Zone reconnue! Lancement du quiz...", Toast.LENGTH_SHORT).show();
                    
                    // Start quiz after a short delay
                    new android.os.Handler().postDelayed(() -> {
                        showLoadingDialog();
                        showQuizDialog();
                    }, 1000);
                }
            }
        });
        
        // Initialize team names cache from Firebase
        initializeTeamNamesCache();
        
        // Load user money from Firebase
        poiScoreViewModel.loadUserMoneyFromFirebase(requireContext());
        
        // Observe user money changes
        poiScoreViewModel.getMoneyScore().observe(getViewLifecycleOwner(), money -> {
            if (money != null) {
                userMoney = money;
            }
        });
        
        // Get POI data from arguments
        Bundle args = getArguments();
        if (args != null) {
            poiName = args.getString("poiName");
            poiId = args.getString("poiId"); // Use actual POI ID from Firebase
            poiLatitude = args.getFloat("poiLatitude");
            poiLongitude = args.getFloat("poiLongitude");
            poiScore = args.getInt("poiScore");
            // Convert int to string for consistency
            int owningTeamInt = args.getInt("poiOwningTeam", 0);
            poiOwningTeam = owningTeamInt > 0 ? "team_" + owningTeamInt : null;
            userTeamId = args.getString("userTeamId", "team_1"); // Default to team_1 if not provided
            
            // Initialize POI score with actual POI data
            try {
                poiScoreViewModel.initializePoi(poiId, poiName, poiScore);
                // loadFromFirebase() is now called automatically in initializePoi()
            } catch (Exception e) {
                android.util.Log.e("POI_SCORE", "Error initializing POI: " + e.getMessage());
                // Set default values if initialization fails
                if (poiScoreViewModel.getCurrentScore().getValue() == null) {
                    poiScoreViewModel.setScore(100);
                }
            }
        } else {
            // Set default values
            poiName = "Zone inconnue";
            poiId = "unknown_zone";
            poiScore = 100;
            poiOwningTeam = null;
            userTeamId = "team_1";
            if (poiScoreViewModel.getCurrentScore().getValue() == null) {
                poiScoreViewModel.setScore(100);
            }
        }

        final TextView textView = binding.textScore;
        // Observe score changes
        poiScoreViewModel.getCurrentScore().observe(getViewLifecycleOwner(), score -> {
            if (score != null) {
                updateDisplayText(score, null);
            } else {
                android.util.Log.w("POI_SCORE", "Score is null, not updating display");
            }
        });
        
        // Observe team changes
        poiScoreViewModel.getOwningTeam().observe(getViewLifecycleOwner(), team -> {
            updateDisplayText(null, team);
        });

        final TextView moneyTextView = binding.textMoneyScore;
        poiScoreViewModel.getMoneyScore().observe(getViewLifecycleOwner(), money -> {
            if (getContext() != null && moneyTextView != null) {
                moneyTextView.setText("Argent: " + (money != null ? money : 0) + "€");
            }
        });

        Button recordVoiceButton = binding.buttonRecordVoice;
        recordVoiceButton.setOnClickListener(v -> startVoiceRecognition());

        Button manualActionsButton = binding.buttonManualActions;
        manualActionsButton.setOnClickListener(v -> showManualActionsDialog());

        // Initialize speech recognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {
                Toast.makeText(getContext(), "Erreur de reconnaissance vocale: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String command = matches.get(0).toLowerCase();
                    handleVoiceCommand(command);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        return root;
    }

    private void startVoiceRecognition() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            speechRecognizer.startListening(speechRecognizerIntent);
            Toast.makeText(getContext(), "Parlez maintenant...", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleVoiceCommand(String command) {
        Log.i("PoiScoreFragment", "handleVoiceCommand() called with: " + command);
        // This is where you'll recognize specific phrases
        if (command.contains("je dépose les armes")) {
            poiScoreViewModel.addMoneyBonus(25, requireContext()); // Bonus de 25€ pour déposer les armes
            recordPoiVisit(); // Enregistrer la visite du POI
            
            // Show surrender dialog
            showSurrenderDialog();

        } else if (command.contains("je capture la zone")) {
            Log.i("PoiScoreFragment", "Command: je capture la zone - calling navigateToCameraCapture()");
            Toast.makeText(getContext(), "Commande reconnue: Je capture la zone", Toast.LENGTH_SHORT).show();
            navigateToCameraCapture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecognition();
            } else {
                Toast.makeText(getContext(), "Permission d'enregistrement audio refusée.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Score is automatically updated by PoiScoreViewModel
    }

    private void showQuizDialog() {
        Log.i("PoiScoreFragment", "showQuizDialog() called");
        
        // Récupérer les questions depuis l'API QuizAPI
        quizRepository.getQuizQuestions(new QuizRepository.QuizCallback() {
            @Override
            public void onSuccess(List<QuizQuestion> questions) {
                Log.i("PoiScoreFragment", "API success with " + questions.size() + " questions");
                hideLoadingDialog();
                // Utiliser les questions de l'API
                showApiQuestionDialog(questions, 0, 0);
            }

            @Override
            public void onError(String errorMessage) {
                hideLoadingDialog();
                // En cas d'erreur, utiliser les questions par défaut
                Log.w("PoiScoreFragment", "Erreur API: " + errorMessage + ", utilisation des questions par défaut");
                showDefaultQuestionDialog();
            }
        });
    }

    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chargement des questions");
        builder.setMessage("Veuillez patienter pendant que nous chargeons les questions...");
        builder.setCancelable(false);
        
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    private void showDefaultQuestionDialog() {
        // Questions par défaut (fallback) - MODIFIÉES pour tester
        List<DefaultQuizQuestion> questions = Arrays.asList(
            new DefaultQuizQuestion("TEST API - Quelle est la capitale de la France?", 
                Arrays.asList("Londres", "Paris", "Berlin", "Madrid"), 1),
            new DefaultQuizQuestion("TEST API - Combien font 2 + 2?", 
                Arrays.asList("3", "4", "5", "6"), 1),
            new DefaultQuizQuestion("TEST API - Quelle couleur est le ciel?", 
                Arrays.asList("Rouge", "Vert", "Bleu", "Jaune"), 2),
            new DefaultQuizQuestion("TEST API - Quel est le plus grand océan?", 
                Arrays.asList("Atlantique", "Indien", "Arctique", "Pacifique"), 3),
            new DefaultQuizQuestion("TEST API - Combien y-a-t-il de jours dans une semaine?", 
                Arrays.asList("5", "6", "7", "8"), 2),
            new DefaultQuizQuestion("TEST API - Quel est le plus grand pays du monde en superficie ?",
                Arrays.asList("Chine", "Canada", "Russie", "États-Unis"), 2),
            new DefaultQuizQuestion("TEST API - Quel est l'animal le plus rapide du monde ?",
                Arrays.asList("Guépard", "Faucon pèlerin", "Antilope", "Lion"), 1),
            new DefaultQuizQuestion("TEST API - Quelle est la couleur du cheval blanc d'Henri IV ?",
                Arrays.asList("Noir", "Blanc", "Gris", "Marron"), 1),
            new DefaultQuizQuestion("TEST API - Combien de continents y-a-t-il sur Terre ?",
                Arrays.asList("5", "6", "7", "8"), 2),
            new DefaultQuizQuestion("TEST API - Quel est le plus haut sommet du monde ?",
                Arrays.asList("Mont Blanc", "K2", "Everest", "Kangchenjunga"), 2)
        );

        showDefaultQuestionDialog(questions, 0, 0);
    }

    private void showApiQuestionDialog(List<QuizQuestion> questions, int currentQuestionIndex, int currentScore) {
        Log.i("PoiScoreFragment", "showApiQuestionDialog() called");
        
        // S'assurer qu'on est sur le thread UI
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(() -> showApiQuestionDialogOnUiThread(questions, currentQuestionIndex, currentScore));
        } else {
            showApiQuestionDialogOnUiThread(questions, currentQuestionIndex, currentScore);
        }
    }
    
    private void showApiQuestionDialogOnUiThread(List<QuizQuestion> questions, int currentQuestionIndex, int currentScore) {
        Log.i("PoiScoreFragment", "showApiQuestionDialogOnUiThread() called");
        
        if (currentQuestionIndex >= questions.size()) {
            // Quiz finished, check if score beats zone score
            checkQuizResult(currentScore);
            return;
        }
        
        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        final int finalScore = currentScore; // Make effectively final for lambda
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Quiz de capture de zone - Question " + (currentQuestionIndex + 1) + "/" + questions.size());

        // Create a custom layout for the quiz
        View quizView = getLayoutInflater().inflate(R.layout.quiz_dialog, null);
        builder.setView(quizView);

        TextView questionText = quizView.findViewById(R.id.text_question);
        RadioGroup radioGroup = quizView.findViewById(R.id.radio_group_quiz);
        Button submitButton = quizView.findViewById(R.id.button_submit_quiz);

        questionText.setText(currentQuestion.getQuestion());

        // Clear existing radio buttons
        radioGroup.removeAllViews();
        
        // Add radio buttons for each option from API
        List<String> options = getOptionsFromApiQuestion(currentQuestion);
        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(options.get(i));
            radioButton.setId(i);
radioGroup.addView(radioButton);
        }

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        submitButton.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                int newScore = finalScore;
                if (isCorrectAnswer(currentQuestion, selectedId)) {
                    newScore += 10; // Add 10 points for correct answer
                    Toast.makeText(getContext(), "Bonne réponse! +10 points", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Mauvaise réponse!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                showApiQuestionDialog(questions, currentQuestionIndex + 1, newScore);
            } else {
                Toast.makeText(getContext(), "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showDefaultQuestionDialog(List<DefaultQuizQuestion> questions, int currentQuestionIndex, int currentScore) {
        if (currentQuestionIndex >= questions.size()) {
            // Quiz finished, check if score beats zone score
            checkQuizResult(currentScore);
            return;
        }

        DefaultQuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        final int finalScore = currentScore; // Make effectively final for lambda
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Quiz de capture de zone - Question " + (currentQuestionIndex + 1) + "/" + questions.size());

        // Create a custom layout for the quiz
        View quizView = getLayoutInflater().inflate(R.layout.quiz_dialog, null);
        builder.setView(quizView);

        TextView questionText = quizView.findViewById(R.id.text_question);
        RadioGroup radioGroup = quizView.findViewById(R.id.radio_group_quiz);
        Button submitButton = quizView.findViewById(R.id.button_submit_quiz);

        questionText.setText(currentQuestion.getQuestion());

        // Clear existing radio buttons
        radioGroup.removeAllViews();
        
        // Add radio buttons for each option
        for (int i = 0; i < currentQuestion.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(currentQuestion.getOptions().get(i));
            radioButton.setId(i);
radioGroup.addView(radioButton);
        }

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        submitButton.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                int newScore = finalScore;
                if (selectedId == currentQuestion.getCorrectAnswerIndex()) {
                    newScore += 10; // Add 10 points for correct answer
                    Toast.makeText(getContext(), "Bonne réponse! +10 points", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Mauvaise réponse!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                showDefaultQuestionDialog(questions, currentQuestionIndex + 1, newScore);
            } else {
                Toast.makeText(getContext(), "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void checkQuizResult(int quizScore) {
        try {
            // Enregistrer la visite du POI (que ce soit succès ou échec)
            recordPoiVisit();
            
            // Get current zone score for dialog
            currentZoneScore = poiScoreViewModel.getCurrentScore().getValue() != null ? 
                poiScoreViewModel.getCurrentScore().getValue() : 100;
            
            // Calculate win condition locally (database update will happen on Continue click)
            boolean playerWon = quizScore > currentZoneScore;
            
            // Apply penalty if player lost
            if (!playerWon) {
                userMoney -= 10; // Pénalité de 10€ pour échec au quiz
                poiScoreViewModel.setMoney(userMoney);
                poiScoreViewModel.saveUserMoneyToFirebase(userMoney, requireContext());
            }
            
            // Show appropriate dialog based on result
            // Show bonus options dialog after quiz completion, which will then show quiz result dialog
            showBonusOptionsDialog(quizScore, playerWon);
            
        } catch (Exception e) {
            android.util.Log.e("POI_SCORE", "Error in checkQuizResult", e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Une erreur est survenue lors du traitement du résultat", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private int getCurrentZoneScore() {
        // Use the stored current zone score
        return currentZoneScore;
    }

    private void showQuizResultDialog(boolean playerWon, int quizScore, int zoneScore) {
        QuizResultDialog.ResultType resultType = playerWon ? 
            QuizResultDialog.ResultType.SUCCESS : 
            QuizResultDialog.ResultType.FAILURE;
        
        QuizResultDialog dialog = QuizResultDialog.newInstance(
            poiName,
            resultType,
            userTeamId,
            quizScore,
            zoneScore
        );
        
        dialog.setQuizResultListener(new QuizResultDialog.QuizResultListener() {
            @Override
            public void onDialogClosed() {
                // Navigate back to main page after dialog closes
                navigateBackToHome();
            }
        });
        
        dialog.show(getParentFragmentManager(), "QuizResultDialog");
    }
    
    private void showSurrenderDialog() {
        QuizResultDialog dialog = QuizResultDialog.newInstance(
            poiName,
            QuizResultDialog.ResultType.SURRENDER,
            userTeamId,
            0,
            0
        );
        
        dialog.setQuizResultListener(new QuizResultDialog.QuizResultListener() {
            @Override
            public void onDialogClosed() {
                // Retourner à la carte après le bonus pour éviter les actions multiples
                new android.os.Handler().postDelayed(() -> {
                    if (isAdded() && getView() != null) {
                        try {
                            NavController navController = Navigation.findNavController(requireView());
                            navController.navigateUp(); // Retour à la page principale pour recharger la carte
                        } catch (IllegalStateException e) {
                            Log.w("PoiScoreFragment", "Fragment not attached to activity, skipping navigation");
                        }
                    }
                }, 2000); // 2 secondes de délai
            }
        });
        
        dialog.show(getParentFragmentManager(), "QuizResultDialog");
    }
    
    /**
     * Interface for handling bonus dialog completion
     */
    private interface BonusDialogCallback {
        void onBonusDialogCompleted(int finalQuizScore, boolean finalPlayerWon);
    }

    private void showBonusOptionsDialog(int quizScore, boolean playerWon) {
        showBonusOptionsDialog(quizScore, playerWon, new BonusDialogCallback() {
            @Override
            public void onBonusDialogCompleted(int finalQuizScore, boolean finalPlayerWon) {
                // Show quiz result dialog after bonus dialog is completed
                showQuizResultDialog(finalPlayerWon, finalQuizScore, getCurrentZoneScore());
            }
        });
    }

    private void showBonusOptionsDialog(int initialQuizScore, boolean initialPlayerWon, BonusDialogCallback callback) {
        // Use mutable containers for score and win status to modify in listeners
        final int[] currentQuizScore = {initialQuizScore};
        final boolean[] currentPlayerWon = {initialPlayerWon};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Résultat du QCM");
        
        String message = currentPlayerWon[0] 
            ? "Félicitations! Vous avez capturé la zone " + (poiName != null ? poiName : "inconnue") + " avec un score de " + currentQuizScore[0] + "!"
            : "Dommage! Votre score de " + currentQuizScore[0] + " n'est pas suffisant pour capturer la zone.";
        
        builder.setMessage(message + "\n\nVoulez-vous utiliser un bonus?");
        
        // Create custom layout for bonus options
        View bonusView = getLayoutInflater().inflate(R.layout.bonus_options_dialog, null);
        builder.setView(bonusView);
        
        Button freezeButton = bonusView.findViewById(R.id.button_freeze_bonus);
        Button boostButton = bonusView.findViewById(R.id.button_boost_bonus);
        Button continueButton = bonusView.findViewById(R.id.button_continue);
        
// Money and Score display TextViews
        TextView moneyDisplay = bonusView.findViewById(R.id.text_money_display);
        TextView scoreDisplay = bonusView.findViewById(R.id.text_score_display);
        TextView targetScoreDisplay = bonusView.findViewById(R.id.text_target_score_display);
        
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        
        // Method to update displays
        Runnable updateDisplays = new Runnable() {
            @Override
            public void run() {
                if (moneyDisplay != null) {
                    moneyDisplay.setText("Argent: " + userMoney + "€");
                }
                if (scoreDisplay != null) {
                    scoreDisplay.setText("Score: " + currentQuizScore[0]);
                }
                if (targetScoreDisplay != null) {
                    targetScoreDisplay.setText("Score cible de la zone: " + currentZoneScore);
                }
            }
        };
        
        // Initial display update
        updateDisplays.run();
        
        freezeButton.setOnClickListener(v -> {
            if (userMoney >= BonusType.FREEZE_SCORE.getCost()) {
                if (!poiScoreViewModel.isFreezeBonusActive()) {
                    poiScoreViewModel.activateFreezeBonus();
                    userMoney -= BonusType.FREEZE_SCORE.getCost();
                    poiScoreViewModel.setMoney(userMoney);
                    poiScoreViewModel.saveUserMoneyToFirebase(userMoney, requireContext());
                    
                    showBonusConfirmationDialog("Score figé!", "Le score de la zone a été figé pendant 1 heure.");
                    freezeButton.setEnabled(false); // Freeze bonus ne peut être utilisé qu'une fois
                    
                    // Update displays
                    updateDisplays.run();
                } else {
                    Toast.makeText(getContext(), "Bonus déjà actif!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Argent insuffisant!", Toast.LENGTH_SHORT).show();
            }
        });
        
        boostButton.setOnClickListener(v -> {
            if (userMoney >= BonusType.BOOST_SCORE.getCost()) {
                int newScore = currentQuizScore[0] + 5;
                userMoney -= BonusType.BOOST_SCORE.getCost();
                poiScoreViewModel.setMoney(userMoney);
                poiScoreViewModel.saveUserMoneyToFirebase(userMoney, requireContext());
                
                showBonusConfirmationDialog("Score augmenté!", "Votre score est passé de " + currentQuizScore[0] + " à " + newScore + ".");
                
                // Calculate if player would win with boosted score (without updating database yet)
                boolean playerWonWithBoost = newScore > currentZoneScore;
                if (playerWonWithBoost && !currentPlayerWon[0]) {
                    // Player now wins with the boost (will be recorded when clicking Continue)
                    poiOwningTeam = userTeamId;
                }
                
                // Update score for potential future use
                currentQuizScore[0] = newScore;
                currentPlayerWon[0] = playerWonWithBoost;
                
                // Update displays
                updateDisplays.run();
                
                // Ne désactive PAS le bouton boost - l'utilisateur peut l'utiliser plusieurs fois
            } else {
                Toast.makeText(getContext(), "Argent insuffisant!", Toast.LENGTH_SHORT).show();
            }
        });
        
        continueButton.setOnClickListener(v -> {
            dialog.dismiss();
            
            // Only update database if zone is captured with final score
            if (currentPlayerWon[0]) {
                android.util.Log.d("POI_SCORE", "Capturing zone with final score: " + currentQuizScore[0]);
                poiScoreViewModel.handleQcmResult(currentQuizScore[0], userTeamId);
            }
            
            // Proceed to quiz result with final scores
            callback.onBonusDialogCompleted(currentQuizScore[0], currentPlayerWon[0]);
        });
        
        dialog.show();
    }
    
    private void showBonusConfirmationDialog(String title, String message) {
        new AlertDialog.Builder(getContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void navigateBackToHome() {
        if (isAdded() && getView() != null) {
            try {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigateUp(); // Retour à la page principale
            } catch (IllegalStateException e) {
                Log.w("PoiScoreFragment", "Fragment not attached to activity, skipping navigation");
            }
        }
    }

    // Helper methods for API questions
    private List<String> getOptionsFromApiQuestion(QuizQuestion question) {
        List<String> options = new ArrayList<>();
        List<QuizQuestion.Answer> answers = question.getAnswers();
        
        if (answers != null) {
            for (QuizQuestion.Answer answer : answers) {
                options.add(answer.getAnswerText());
            }
        }
        
        return options;
    }

    private boolean isCorrectAnswer(QuizQuestion question, int selectedIndex) {
        List<QuizQuestion.Answer> answers = question.getAnswers();
        
        if (answers != null && selectedIndex < answers.size()) {
            return answers.get(selectedIndex).isValid();
        }
        
        return false;
    }

    // Helper class for default quiz questions (fallback)
    private static class DefaultQuizQuestion {
        private String question;
        private List<String> options;
        private int correctAnswerIndex;

        public DefaultQuizQuestion(String question, List<String> options, int correctAnswerIndex) {
            this.question = question;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public String getQuestion() { return question; }
        public List<String> getOptions() { return options; }
        public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    }

    private void showManualActionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choisissez une action");

        String[] actions = {"Je dépose les armes", "Je capture la zone"};
        builder.setItems(actions, (dialog, which) -> {
            String command = actions[which];
            handleVoiceCommand(command.toLowerCase());
        });
        builder.show();
    }
    
    private void navigateToCameraCapture() {
        try {
            if (!isAdded() || getView() == null) {
                Log.w("PoiScoreFragment", "Fragment not attached, cannot navigate to camera");
                return;
            }
            
            // Create camera capture fragment with POI data
            CameraCaptureFragment cameraFragment = CameraCaptureFragment.newInstance(poiName, poiId);
            
            // Navigate to camera fragment
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_poiScoreFragment_to_cameraCaptureFragment, 
                    cameraFragment.getArguments());
            
            Log.i("PoiScoreFragment", "Navigating to camera capture for POI: " + poiName);
        } catch (Exception e) {
            Log.e("PoiScoreFragment", "Error navigating to camera capture", e);
            Toast.makeText(getContext(), "Erreur lors de l'ouverture de la caméra", Toast.LENGTH_SHORT).show();
            // Fallback to quiz if camera fails
            showLoadingDialog();
            showQuizDialog();
        }
    }
    
    private void recordPoiVisit() {
        try {
            // Get user ID from SharedPreferences
            android.content.SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null && poiId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                
                // Get current user document
                db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, String>> visitedPois = (List<Map<String, String>>) documentSnapshot.get("visitedPois");
                            
                            if (visitedPois == null) {
                                visitedPois = new ArrayList<>();
                            }
                            
                            // Check if POI already exists and update visit date, or add new visit
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String today = dateFormat.format(new Date());
                            
                            boolean poiFound = false;
                            int existingIndex = -1;
                            
                            // First pass: find if POI exists and its index
                            for (int i = 0; i < visitedPois.size(); i++) {
                                Map<String, String> visit = visitedPois.get(i);
                                if (poiId.equals(visit.get("poiId"))) {
                                    existingIndex = i;
                                    poiFound = true;
                                    break;
                                }
                            }
                            
                            if (poiFound && existingIndex >= 0) {
                                // Update existing visit date
                                Map<String, String> existingVisit = visitedPois.get(existingIndex);
                                existingVisit.put("visitDate", today);
                            } else {
                                // Add new visit
                                Map<String, String> visit = new HashMap<>();
                                visit.put("poiId", poiId);
                                visit.put("visitDate", today);
                                visitedPois.add(visit);
                            }
                                                         
                            // Update Firebase
                            db.collection("users").document(userId)
                                .update("visitedPois", visitedPois);
                        }
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("POI_SCORE", "Error getting user document for visit recording", e);
                    });
            } else {
                android.util.Log.w("POI_SCORE", "No user ID or POI ID available for visit recording");
            }
        } catch (Exception e) {
            android.util.Log.e("POI_SCORE", "Exception recording POI visit", e);
        }
    }

    private String getTeamDisplayText(String teamId) {
        android.util.Log.d("TEAM_COLOR_DEBUG", "getTeamDisplayText() called with teamId: " + teamId);
        try {
            if (teamId == null || teamId.isEmpty()) {
                android.util.Log.d("TEAM_COLOR_DEBUG", "TeamId is null or empty, returning Zone neutre");
                return "Zone neutre";
            }
            
            // Get team name from cache or load from Firebase
            String teamName = getTeamNameFromCache(teamId);
            String result = "Équipe: " + teamName;
            android.util.Log.d("TEAM_COLOR_DEBUG", "getTeamDisplayText result: " + result);
            return result;
        } catch (Exception e) {
            android.util.Log.e("POI_SCORE", "Error in getTeamDisplayText: " + e.getMessage());
            return "Zone neutre";
        }
    }
    
    private void updateDisplayText(Integer score, Integer team) {        
        if (getContext() == null || binding == null || binding.textScore == null) {
            android.util.Log.w("POI_SCORE", "Cannot update display - context, binding, or textView is null");
            return;
        }
        
        // Get current values if not provided
        if (score == null) {
            score = poiScoreViewModel.getCurrentScore().getValue();
            android.util.Log.d("POI_SCORE", "Score was null, got from ViewModel: " + score);
        }
        if (team == null) {
            team = poiScoreViewModel.getOwningTeam().getValue();
            android.util.Log.d("POI_SCORE", "Team was null, got from ViewModel: " + team);
            
            // If ViewModel team is still null, use the team from arguments
            if (team == null && poiOwningTeam != null && !poiOwningTeam.isEmpty()) {
                // Use the string team directly
                team = 0; // Keep display compatibility with ViewModel
                android.util.Log.d("POI_SCORE", "Using team from arguments: " + team);
            }
        }
        
        // Update local owning team for quiz result
        if (team != null) {
            // Convert int team to string for consistency
            poiOwningTeam = team > 0 ? "team_" + team : null;
        }
        
        String displayText = poiName != null ? poiName : "Zone inconnue";
        // Use the string team for display
        String displayTeam = (team != null && team > 0) ? ("team_" + team) : (poiOwningTeam != null ? poiOwningTeam : null);
        android.util.Log.d("TEAM_COLOR_DEBUG", "About to call getTeamDisplayText with team: " + displayTeam);
        displayText += "\n" + getTeamDisplayText(displayTeam);
        displayText += "\nScore de la zone: " + (score != null ? score : 0);
        
        binding.textScore.setText(displayText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        // Hide back button when leaving fragment
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            if (isAdded() && getView() != null) {
                try {
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigateUp();
                } catch (IllegalStateException e) {
                    Log.w("PoiScoreFragment", "Fragment not attached to activity, skipping navigation");
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void initializeTeamNamesCache() {        
        // Initialize with default values
        teamNamesCache.put(0, "Neutre");
        teamNamesCache.put(1, "Les Conquérants");
        teamNamesCache.put(2, "Les Explorateurs");
        teamNamesCache.put(3, "Les Stratèges");
        teamNamesCache.put(4, "Les Gardiens");
        
        // Load real team names and colors from Firebase and update cache
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            android.util.Log.d("TEAM_DEBUG", "Creating Firebase query for 'teams' collection");
            
            db.collection("teams")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String teamId = document.getId();
                        android.util.Log.d("TEAM_DEBUG", "Processing team document: " + teamId);
                        android.util.Log.d("TEAM_DEBUG", "Team document data: " + document.getData());
                        
                        String teamName = document.getString("name");
                        String teamColor = document.getString("color");
                        String teamColorHex = document.getString("colorHex");
                        
                        android.util.Log.d("TEAM_DEBUG", "Extracted fields - name: " + teamName + ", color: " + teamColor + ", colorHex: " + teamColorHex);
                        
                        // Extract team number from "team_X" format
                        if (teamId.startsWith("team_")) {
                            try {
                                int teamNumber = Integer.parseInt(teamId.substring(5));
                                if (teamName != null) {
                                    teamNamesCache.put(teamNumber, teamName);
                                    
                                    // Refresh display if already showing
                                    updateDisplayText(null, null);
                                }

                            } catch (NumberFormatException e) {
                                android.util.Log.e("TEAM_DEBUG", "Error parsing team ID: " + teamId, e);
                            }
                        } else {
                            android.util.Log.w("TEAM_DEBUG", "Team ID doesn't start with 'team_': " + teamId);
                        }
                    }
                    
                    android.util.Log.d("TEAM_DEBUG", "Team names cache: " + teamNamesCache.toString());
                    
                    // Force refresh display if already showing
                    if (getContext() != null) {
                        android.util.Log.d("TEAM_DEBUG", "About to call updateDisplayText from Firebase success");
                        updateDisplayText(null, null);
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("TEAM_DEBUG", "ERROR loading teams from Firebase: " + e.getMessage());
                    android.util.Log.e("TEAM_DEBUG", "Firebase query failed");
                });
        } catch (Exception e) {
            android.util.Log.e("TEAM_DEBUG", "Exception in initializeTeamNamesCache", e);
        }
    }
    
    private String getTeamNameFromCache(String teamId) {
        // First try to get from cache (convert to int for cache lookup)
        int teamNumber = 0;
        if (teamId != null && teamId.startsWith("team_")) {
            try {
                teamNumber = Integer.parseInt(teamId.substring(5));
            } catch (NumberFormatException e) {
                teamNumber = 0;
            }
        }
        
        String teamName = teamNamesCache.get(teamNumber);
        if (teamName != null) {
            return teamName;
        }
        
        // If not in cache, return default based on teamId
        switch (teamId) {
            case "team_1": return "Les Conquérants";
            case "team_2": return "Les Explorateurs";
            case "team_3": return "Les Stratèges";
            case "team_4": return "Les Gardiens";
            default: return "Équipe inconnue";
        }
    }
}