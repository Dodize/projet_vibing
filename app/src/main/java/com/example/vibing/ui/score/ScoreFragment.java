package com.example.vibing.ui.score;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
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
import com.example.vibing.databinding.FragmentScoreBinding;
import com.example.vibing.models.QuizQuestion;
import com.example.vibing.repository.QuizRepository;

import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ScoreFragment extends Fragment {

    private FragmentScoreBinding binding;
    private ScoreViewModel scoreViewModel;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private QuizRepository quizRepository;
    private AlertDialog loadingDialog;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i("ScoreFragment", "onCreateView() called");
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        quizRepository = new QuizRepository(requireContext());
        Log.i("ScoreFragment", "QuizRepository created");

        binding = FragmentScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        // Enable back button in toolbar
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView textView = binding.textScore;
        scoreViewModel.getScore().observe(getViewLifecycleOwner(), score -> {
            textView.setText("Score de la zone: " + score);
        });

        final TextView moneyTextView = binding.textMoneyScore;
        scoreViewModel.getMoneyScore().observe(getViewLifecycleOwner(), money -> {
            moneyTextView.setText("Argent: " + money + "€");
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
        Log.i("ScoreFragment", "handleVoiceCommand() called with: " + command);
        // This is where you'll recognize specific phrases
        if (command.contains("je dépose les armes")) {
            Log.i("ScoreFragment", "Command: déposer les armes");
            scoreViewModel.addMoneyBonus(25); // Bonus de 25€ pour déposer les armes
            Toast.makeText(getContext(), "Commande reconnue: Je dépose les armes - Bonus de 25€ ajouté!", Toast.LENGTH_LONG).show();
        } else if (command.contains("je capture la zone")) {
            Log.i("ScoreFragment", "Command: je capture la zone - calling showQuizDialog()");
            Toast.makeText(getContext(), "Commande reconnue: Je capture la zone", Toast.LENGTH_SHORT).show();
            showLoadingDialog();
            showQuizDialog();
        } else {
            Log.i("ScoreFragment", "Commande non reconnue: " + command);
            Toast.makeText(getContext(), "Commande non reconnue: " + command, Toast.LENGTH_LONG).show();
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
        // When the fragment becomes visible, update the score based on time elapsed
        String currentZoneId = "zone_alpha"; // Use the same hardcoded ID for now
        scoreViewModel.updateScoreBasedOnTime(currentZoneId);
    }

    private void showQuizDialog() {
        Log.i("ScoreFragment", "showQuizDialog() called");
        
        // Récupérer les questions depuis l'API QuizAPI
        quizRepository.getQuizQuestions(new QuizRepository.QuizCallback() {
            @Override
            public void onSuccess(List<QuizQuestion> questions) {
                Log.i("ScoreFragment", "API success with " + questions.size() + " questions");
                hideLoadingDialog();
                // Utiliser les questions de l'API
                showApiQuestionDialog(questions, 0, 0);
            }

            @Override
            public void onError(String errorMessage) {
                hideLoadingDialog();
                // En cas d'erreur, utiliser les questions par défaut
                Log.w("ScoreFragment", "Erreur API: " + errorMessage + ", utilisation des questions par défaut");
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
        Integer currentZoneScore = scoreViewModel.getScore().getValue();
        if (currentZoneScore != null && quizScore > currentZoneScore) {
            scoreViewModel.setZoneScore(quizScore);
            Toast.makeText(getContext(), "Félicitations! Vous avez capturé la zone avec un score de " + quizScore + "!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Quiz terminé! Votre score: " + quizScore + ". Score insuffisant pour capturer la zone.", Toast.LENGTH_LONG).show();
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
                    Log.w("ScoreFragment", "Fragment not attached to activity, skipping navigation");
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}