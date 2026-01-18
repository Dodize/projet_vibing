package com.example.vibing.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;

import com.example.vibing.R;

/**
 * Dialog for displaying quiz results to users
 * Based on specification in spec/quiz_dialogue.md
 */
public class QuizResultDialog extends DialogFragment {

    public interface QuizResultListener {
        void onDialogClosed();
    }

    // Result types based on specification
    public enum ResultType {
        SUCCESS(true, false),    // User captured zone
        FAILURE(false, false),   // User failed quiz, loses money
        SURRENDER(false, true);  // User surrendered, gains small money

        private final boolean isSuccess;
        private final boolean hasMoneyBonus;

        ResultType(boolean isSuccess, boolean hasMoneyBonus) {
            this.isSuccess = isSuccess;
            this.hasMoneyBonus = hasMoneyBonus;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public boolean hasMoneyBonus() {
            return hasMoneyBonus;
        }
    }

    private String poiName;
    private ResultType resultType;
    private String teamId;
    private int quizScore;
    private int zoneScore;
    private QuizResultListener listener;

    public QuizResultDialog() {
        // Required empty constructor for DialogFragment
    }
    
    public void setQuizResultListener(QuizResultListener listener) {
        this.listener = listener;
    }

    public static QuizResultDialog newInstance(String poiName, 
                                         ResultType resultType, 
                                         String teamId,
                                         int quizScore, 
                                         int zoneScore) {
        QuizResultDialog dialog = new QuizResultDialog();
        dialog.poiName = poiName;
        dialog.resultType = resultType;
        dialog.teamId = teamId;
        dialog.quizScore = quizScore;
        dialog.zoneScore = zoneScore;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_quiz_result, container, false);
        
        initializeViews(view);
        setupClickListeners(view);
        updateContent(view);
        
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, 
                WindowManager.LayoutParams.MATCH_PARENT
            );
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void initializeViews(View view) {
        // Views will be initialized in updateContent()
    }

    private void setupClickListeners(View view) {
        Button closeButton = view.findViewById(R.id.btn_close_dialog);
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> closeDialog());
        }
    }

    private void updateContent(View view) {
        TextView titleText = view.findViewById(R.id.dialog_title);
        TextView messageText = view.findViewById(R.id.dialog_message);
        Button closeButton = view.findViewById(R.id.btn_close_dialog);

        if (titleText != null) {
            titleText.setText(generateTitle());
        }

        if (messageText != null) {
            messageText.setText(generateMessage());
        }

        if (closeButton != null) {
            closeButton.setText(generateButtonText());
        }
    }

    private String generateTitle() {
        switch (resultType) {
            case SUCCESS:
                return "Zone capturée avec succès ! 🎉";
            case FAILURE:
                return "Quiz échoué 😔";
            case SURRENDER:
                return "Dépôt des armes";
            default:
                return "Résultat du quiz";
        }
    }

    private String generateMessage() {
        StringBuilder message = new StringBuilder();
        
        switch (resultType) {
            case SUCCESS:
                message.append("Félicitations ! Vous avez capturé la zone ")
                       .append(poiName != null ? poiName : "inconnue")
                       .append(" pour votre équipe !\n\n")
                       .append("Votre score: ").append(quizScore)
                       .append(" | Score de la zone: ").append(zoneScore)
                       .append("\n\n")
                       .append("La zone est maintenant sous le contrôle de votre équipe.");
                break;
                
            case FAILURE:
                message.append("Dommage ! Vous n'avez pas réussi à capturer la zone ")
                       .append(poiName != null ? poiName : "inconnue")
                       .append(".\n\n")
                       .append("Votre score: ").append(quizScore)
                       .append(" | Score requis: ").append(zoneScore)
                       .append("\n\n")
                       .append("Une pénalité de 10€ a été appliquée pour l'échec du quiz.");
                break;
                
            case SURRENDER:
                message.append("Vous avez choisi de déposer les armes pour la zone ")
                       .append(poiName != null ? poiName : "inconnue")
                       .append(".\n\n")
                       .append("Un bonus de 25€ a été ajouté à votre argent virtuel.");
                break;
        }

        return message.toString();
    }

    private String generateButtonText() {
        return "Fermer";
    }

    private void closeDialog() {
        if (listener != null) {
            listener.onDialogClosed();
        }
        dismiss();
    }

    // Getters for testing
    public String getPoiName() {
        return poiName;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public String getTeamId() {
        return teamId;
    }

    public int getQuizScore() {
        return quizScore;
    }

    public int getZoneScore() {
        return zoneScore;
    }

    public boolean isSuccess() {
        return resultType.isSuccess();
    }

    public boolean hasMoneyBonus() {
        return resultType.hasMoneyBonus();
    }

    // No additional methods needed - testing will use the actual public interface
}