package com.example.vibing.repository;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import com.example.vibing.api.OpenTriviaApiService;
import com.example.vibing.models.OpenTriviaResponse;
import com.example.vibing.models.QuizQuestion;
import com.example.vibing.services.MyMemoryTranslationService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizRepository {
    private static final String TAG = "QuizRepository";
    private OpenTriviaApiService openTriviaApiService;
    private MyMemoryTranslationService translationService;
    private Context context;

    public QuizRepository(Context context) {
        this.context = context;
        
        // Configuration Retrofit pour Open Trivia Database API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        openTriviaApiService = retrofit.create(OpenTriviaApiService.class);
        translationService = new MyMemoryTranslationService(context);
    }

    public interface QuizCallback {
        void onSuccess(List<QuizQuestion> questions);
        void onError(String errorMessage);
    }

    public void getQuizQuestions(QuizCallback callback) {
        Log.i(TAG, "Fetching questions from Open Trivia Database API");
        
        // Forcer l'utilisation des questions anglaises avec traduction
        // car les questions françaises ne sont pas vraiment disponibles
        getEnglishQuestionsAndTranslate(callback);
    }

    private void getEnglishQuestionsAndTranslate(QuizCallback callback) {
        Call<OpenTriviaResponse> call = openTriviaApiService.getRandomQuestions(10, "en");
        
        call.enqueue(new Callback<OpenTriviaResponse>() {
            @Override
            public void onResponse(Call<OpenTriviaResponse> call, Response<OpenTriviaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OpenTriviaResponse openTriviaResponse = response.body();
                    
                    if (openTriviaResponse.getResponse_code() == 0 && openTriviaResponse.getResults() != null) {
                        List<OpenTriviaResponse.OpenTriviaQuestion> triviaQuestions = openTriviaResponse.getResults();
                        Log.i(TAG, "English questions loaded, count: " + triviaQuestions.size());
                        
                        // Traduire les questions anglaises en français
                        translateAndConvertQuestions(triviaQuestions, callback);
                        return;
                    }
                }
                
                Log.e(TAG, "English questions also failed, using local questions");
                loadLocalFrenchQuestions(callback);
            }

            @Override
            public void onFailure(Call<OpenTriviaResponse> call, Throwable t) {
                Log.e(TAG, "English API also failed: " + t.getMessage());
                loadLocalFrenchQuestions(callback);
            }
        });
    }

    private void convertOpenTriviaQuestions(List<OpenTriviaResponse.OpenTriviaQuestion> triviaQuestions, QuizCallback callback) {
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        
        for (OpenTriviaResponse.OpenTriviaQuestion triviaQ : triviaQuestions) {
            QuizQuestion quizQ = new QuizQuestion();
            quizQ.setId("trivia_" + quizQuestions.size());
            
            // Créer le titre
            QuizQuestion.Title title = new QuizQuestion.Title();
            title.setFr(triviaQ.getQuestion()); // Utiliser la question comme française
            title.setEn(triviaQ.getQuestion()); // Garder l'original
            quizQ.setTitle(title);
            
            quizQ.setType(triviaQ.getType());
            
            // Convertir la difficulté
            int difficulty = convertDifficulty(triviaQ.getDifficulty());
            quizQ.setDifficulty(difficulty);

            // Créer les réponses
            List<QuizQuestion.Answer> answers = new ArrayList<>();
            
            // Ajouter la réponse correcte
            answers.add(createAnswer("correct_" + quizQuestions.size(), triviaQ.getCorrect_answer(), true));
            
            // Ajouter les réponses incorrectes
            for (int i = 0; i < triviaQ.getIncorrect_answers().size(); i++) {
                answers.add(createAnswer("wrong_" + quizQuestions.size() + "_" + i, triviaQ.getIncorrect_answers().get(i), false));
            }
            
            // Mélanger les réponses
            shuffleAnswers(answers);
            quizQ.setAnswers(answers);
            
            quizQuestions.add(quizQ);
        }
        
        Log.i(TAG, "Converted " + quizQuestions.size() + " questions successfully");
        callback.onSuccess(quizQuestions);
    }

    private void translateAndConvertQuestions(List<OpenTriviaResponse.OpenTriviaQuestion> triviaQuestions, QuizCallback callback) {
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        int[] translatedCount = {0};
        int totalCount = triviaQuestions.size();

        for (OpenTriviaResponse.OpenTriviaQuestion triviaQ : triviaQuestions) {
            QuizQuestion quizQ = new QuizQuestion();
            quizQ.setId("trivia_" + quizQuestions.size());
            quizQ.setType(triviaQ.getType());
            
            // Convertir la difficulté
            int difficulty = convertDifficulty(triviaQ.getDifficulty());
            quizQ.setDifficulty(difficulty);

            // Créer les réponses
            List<QuizQuestion.Answer> answers = new ArrayList<>();
            
            // Ajouter la réponse correcte
            answers.add(createAnswer("correct_" + quizQuestions.size(), triviaQ.getCorrect_answer(), true));
            
            // Ajouter les réponses incorrectes
            for (int i = 0; i < triviaQ.getIncorrect_answers().size(); i++) {
                answers.add(createAnswer("wrong_" + quizQuestions.size() + "_" + i, triviaQ.getIncorrect_answers().get(i), false));
            }
            
            // Mélanger les réponses
            shuffleAnswers(answers);
            quizQ.setAnswers(answers);

            // Décoder et traduire la question
            String originalQuestion = decodeHtmlEntities(triviaQ.getQuestion());
            translationService.translateText(originalQuestion, new MyMemoryTranslationService.TranslationCallback() {
                @Override
                public void onSuccess(String translatedText) {
                    QuizQuestion.Title title = new QuizQuestion.Title();
                    title.setFr(translatedText);
                    title.setEn(originalQuestion); // Garder l'original
                    quizQ.setTitle(title);
                    
                    // Traduire les réponses
                    translateAnswers(quizQ, triviaQ, callback, quizQuestions, translatedCount, totalCount);
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w(TAG, "Translation failed for question: " + errorMessage);
                    // Utiliser la question originale si la traduction échoue
                    QuizQuestion.Title title = new QuizQuestion.Title();
                    title.setFr(originalQuestion);
                    title.setEn(originalQuestion);
                    quizQ.setTitle(title);
                    
                    translateAnswers(quizQ, triviaQ, callback, quizQuestions, translatedCount, totalCount);
                }
            });
        }
    }

    private void translateAnswers(QuizQuestion quizQ, OpenTriviaResponse.OpenTriviaQuestion triviaQ, 
                                QuizCallback callback, List<QuizQuestion> quizQuestions, 
                                int[] translatedCount, int totalCount) {
        
        List<QuizQuestion.Answer> answers = quizQ.getAnswers();
        int[] translatedAnswers = {0};
        int totalAnswers = answers.size();

        // Préparer les textes à traduire
        String[] textsToTranslate = new String[totalAnswers];
        MyMemoryTranslationService.TranslationCallback[] callbacks = new MyMemoryTranslationService.TranslationCallback[totalAnswers];

        for (int i = 0; i < totalAnswers; i++) {
            final int index = i;
            String originalAnswer = decodeHtmlEntities(answers.get(i).getTitle().getFr()); // Décoder les entités HTML
            textsToTranslate[i] = originalAnswer;
            
            callbacks[i] = new MyMemoryTranslationService.TranslationCallback() {
                @Override
                public void onSuccess(String translatedText) {
                    QuizQuestion.Title title = new QuizQuestion.Title();
                    title.setFr(translatedText);
                    title.setEn(originalAnswer);
                    answers.get(index).setTitle(title);
                    
                    translatedAnswers[0]++;
                    checkAllAnswersTranslated(answers, translatedAnswers, totalAnswers, quizQ, quizQuestions, translatedCount, totalCount, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w(TAG, "Translation failed for answer: " + errorMessage);
                    // Utiliser la réponse originale si la traduction échoue
                    QuizQuestion.Title title = new QuizQuestion.Title();
                    title.setFr(originalAnswer);
                    title.setEn(originalAnswer);
                    answers.get(index).setTitle(title);
                    
                    translatedAnswers[0]++;
                    checkAllAnswersTranslated(answers, translatedAnswers, totalAnswers, quizQ, quizQuestions, translatedCount, totalCount, callback);
                }
            };
        }

        // Lancer les traductions en parallèle
        translationService.translateTexts(textsToTranslate, callbacks);
    }

    private void checkAllAnswersTranslated(List<QuizQuestion.Answer> answers, int[] translatedAnswers, int totalAnswers, 
                                       QuizQuestion quizQ, List<QuizQuestion> quizQuestions, 
                                       int[] translatedCount, int totalCount, QuizCallback callback) {
        if (translatedAnswers[0] == totalAnswers) {
            quizQ.setAnswers(answers);
            quizQuestions.add(quizQ);
            translatedCount[0]++;
            
            if (translatedCount[0] == totalCount) {
                Log.i(TAG, "All questions translated successfully");
                callback.onSuccess(quizQuestions);
            }
        }
    }

    private int convertDifficulty(String difficulty) {
        if (difficulty == null) return 2; // Medium par défaut
        
        switch (difficulty.toLowerCase()) {
            case "easy": return 1;
            case "medium": return 2;
            case "hard": return 3;
            default: return 2;
        }
    }

    private QuizQuestion.Answer createAnswer(String id, String text, boolean isCorrect) {
        QuizQuestion.Answer answer = new QuizQuestion.Answer();
        answer.setId(id);
        QuizQuestion.Title title = new QuizQuestion.Title();
        title.setFr(text);
        title.setEn(text);
        answer.setTitle(title);
        answer.setValid(isCorrect);
        return answer;
    }

    private void shuffleAnswers(List<QuizQuestion.Answer> answers) {
        Random random = new Random();
        for (int i = answers.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Échanger les réponses
            QuizQuestion.Answer temp = answers.get(i);
            answers.set(i, answers.get(j));
            answers.set(j, temp);
        }
    }

    // Questions françaises locales (fallback)
    private void loadLocalFrenchQuestions(QuizCallback callback) {
        List<QuizQuestion> frenchQuestions = new ArrayList<>();
        
        // Question 1: Géographie
        QuizQuestion q1 = new QuizQuestion();
        q1.setId("geo_1");
        QuizQuestion.Title title1 = new QuizQuestion.Title();
        title1.setFr("Quelle est la capitale de la France ?");
        q1.setTitle(title1);
        q1.setType("multiple");
        q1.setDifficulty(1);
        
        List<QuizQuestion.Answer> answers1 = new ArrayList<>();
        answers1.add(createAnswer("ans_1_1", "Paris", true));
        answers1.add(createAnswer("ans_1_2", "Lyon", false));
        answers1.add(createAnswer("ans_1_3", "Marseille", false));
        answers1.add(createAnswer("ans_1_4", "Bordeaux", false));
        q1.setAnswers(answers1);
        frenchQuestions.add(q1);
        
        // Question 2: Histoire
        QuizQuestion q2 = new QuizQuestion();
        q2.setId("hist_1");
        QuizQuestion.Title title2 = new QuizQuestion.Title();
        title2.setFr("En quelle année a commencé la Révolution française ?");
        q2.setTitle(title2);
        q2.setType("multiple");
        q2.setDifficulty(2);
        
        List<QuizQuestion.Answer> answers2 = new ArrayList<>();
        answers2.add(createAnswer("ans_2_1", "1789", true));
        answers2.add(createAnswer("ans_2_2", "1792", false));
        answers2.add(createAnswer("ans_2_3", "1804", false));
        answers2.add(createAnswer("ans_2_4", "1815", false));
        q2.setAnswers(answers2);
        frenchQuestions.add(q2);
        
        // Question 3: Sciences
        QuizQuestion q3 = new QuizQuestion();
        q3.setId("sci_1");
        QuizQuestion.Title title3 = new QuizQuestion.Title();
        title3.setFr("Quelle est la planète la plus proche du Soleil ?");
        q3.setTitle(title3);
        q3.setType("multiple");
        q3.setDifficulty(1);
        
        List<QuizQuestion.Answer> answers3 = new ArrayList<>();
        answers3.add(createAnswer("ans_3_1", "Mercure", true));
        answers3.add(createAnswer("ans_3_2", "Vénus", false));
        answers3.add(createAnswer("ans_3_3", "Terre", false));
        answers3.add(createAnswer("ans_3_4", "Mars", false));
        q3.setAnswers(answers3);
        frenchQuestions.add(q3);
        
        // Question 4: Littérature
        QuizQuestion q4 = new QuizQuestion();
        q4.setId("lit_1");
        QuizQuestion.Title title4 = new QuizQuestion.Title();
        title4.setFr("Qui a écrit 'Les Misérables' ?");
        q4.setTitle(title4);
        q4.setType("multiple");
        q4.setDifficulty(2);
        
        List<QuizQuestion.Answer> answers4 = new ArrayList<>();
        answers4.add(createAnswer("ans_4_1", "Victor Hugo", true));
        answers4.add(createAnswer("ans_4_2", "Émile Zola", false));
        answers4.add(createAnswer("ans_4_3", "Marcel Proust", false));
        answers4.add(createAnswer("ans_4_4", "Albert Camus", false));
        q4.setAnswers(answers4);
        frenchQuestions.add(q4);
        
        // Question 5: Sport
        QuizQuestion q5 = new QuizQuestion();
        q5.setId("sport_1");
        QuizQuestion.Title title5 = new QuizQuestion.Title();
        title5.setFr("Combien de joueurs y a-t-il dans une équipe de football ?");
        q5.setTitle(title5);
        q5.setType("multiple");
        q5.setDifficulty(1);
        
        List<QuizQuestion.Answer> answers5 = new ArrayList<>();
        answers5.add(createAnswer("ans_5_1", "11", true));
        answers5.add(createAnswer("ans_5_2", "9", false));
        answers5.add(createAnswer("ans_5_3", "7", false));
        answers5.add(createAnswer("ans_5_4", "15", false));
        q5.setAnswers(answers5);
        frenchQuestions.add(q5);
        
        Log.i(TAG, "Loaded " + frenchQuestions.size() + " local French questions");
        callback.onSuccess(frenchQuestions);
    }

    // Méthode utilitaire pour décoder les entités HTML
    private String decodeHtmlEntities(String text) {
        if (text == null) return "";
        
        Log.d(TAG, "Original text before decoding: " + text);
        
        // Décoder les entités HTML communes
        String decoded = text
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#039;", "'")
                .replace("&#x27;", "'")
                .replace("&#x2F;", "/")
                .replace("&#x3D;", "=")
                .replace("&#x60;", "`")
                .replace("&#34;", "\"")
                .replace("&#39;", "'")
                .replace("&#47;", "/")
                .replace("&#61;", "=")
                .replace("&#96;", "`");
        
        // Utiliser Html.fromHtml pour les entités plus complexes
        decoded = Html.fromHtml(decoded, Html.FROM_HTML_MODE_LEGACY).toString();
        
        Log.d(TAG, "Decoded text: " + decoded);
        return decoded;
    }

    // Questions par défaut en cas d'erreur API
    public List<QuizQuestion> getDefaultQuestions() {
        List<QuizQuestion> defaultQuestions = new ArrayList<>();
        return defaultQuestions;
    }
}