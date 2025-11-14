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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.vibing.databinding.FragmentScoreBinding;

import java.util.ArrayList;
import java.util.Locale;

public class ScoreFragment extends Fragment {

    private FragmentScoreBinding binding;
    private ScoreViewModel scoreViewModel;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        binding = FragmentScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textScore;
        scoreViewModel.getScore().observe(getViewLifecycleOwner(), score -> {
            textView.setText("Score de la zone: " + score);
        });

        // For now, using a hardcoded zone ID. This should come from navigation arguments or location.
        String currentZoneId = "zone_alpha";

        Button recordVoiceButton = binding.buttonRecordVoice;
        recordVoiceButton.setOnClickListener(v -> startVoiceRecognition());

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
        // This is where you'll recognize specific phrases
        if (command.contains("je dépose les armes")) {
            Toast.makeText(getContext(), "Commande reconnue: Je dépose les armes", Toast.LENGTH_LONG).show();
            // TODO: Implement bonus logic
        } else if (command.contains("je capture la zone")) {
            Toast.makeText(getContext(), "Commande reconnue: Je capture la zone", Toast.LENGTH_LONG).show();
            // TODO: Launch QCM or capture logic
        } else {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

}