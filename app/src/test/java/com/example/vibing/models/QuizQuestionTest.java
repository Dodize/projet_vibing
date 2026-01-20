package com.example.vibing.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class QuizQuestionTest {
    
    private QuizQuestion quizQuestion;
    private QuizQuestion.Title title;
    private QuizQuestion.Theme theme;
    private List<QuizQuestion.Answer> answers;
    
    @Before
    public void setUp() {
        // Créer le titre
        title = new QuizQuestion.Title();
        title.setFr("Question française ?");
        title.setEn("English question?");
        
        // Créer le thème
        theme = new QuizQuestion.Theme();
        theme.setTitle("Science");
        theme.setCode("SCI");
        
        // Créer les réponses
        QuizQuestion.Answer answer1 = new QuizQuestion.Answer();
        answer1.setId("ans1");
        QuizQuestion.Title answerTitle1 = new QuizQuestion.Title();
        answerTitle1.setFr("Réponse 1");
        answerTitle1.setEn("Answer 1");
        answer1.setTitle(answerTitle1);
        answer1.setValid(true);
        
        QuizQuestion.Answer answer2 = new QuizQuestion.Answer();
        answer2.setId("ans2");
        QuizQuestion.Title answerTitle2 = new QuizQuestion.Title();
        answerTitle2.setFr("Réponse 2");
        answerTitle2.setEn("Answer 2");
        answer2.setTitle(answerTitle2);
        answer2.setValid(false);
        
        answers = Arrays.asList(answer1, answer2);
        
        // Créer la question
        quizQuestion = new QuizQuestion();
        quizQuestion.setId("q1");
        quizQuestion.setType("multiple");
        quizQuestion.setTitle(title);
        quizQuestion.setDescription("Description test");
        quizQuestion.setDifficulty(1);
        quizQuestion.setTheme(theme);
        quizQuestion.setAnswers(answers);
    }
    
    @Test
    public void testGettersAndSetters() {
        assertEquals("q1", quizQuestion.getId());
        assertEquals("multiple", quizQuestion.getType());
        assertEquals(title, quizQuestion.getTitle());
        assertEquals("Description test", quizQuestion.getDescription());
        assertEquals(1, quizQuestion.getDifficulty());
        assertEquals(theme, quizQuestion.getTheme());
        assertEquals(answers, quizQuestion.getAnswers());
    }
    
    @Test
    public void testGetQuestion() {
        assertEquals("Question française ?", quizQuestion.getQuestion());
        
        // Test avec titre null
        quizQuestion.setTitle(null);
        assertEquals("", quizQuestion.getQuestion());
        
        // Test avec titre français null
        QuizQuestion.Title titleWithNullFr = new QuizQuestion.Title();
        titleWithNullFr.setEn("English only");
        quizQuestion.setTitle(titleWithNullFr);
        assertEquals("", quizQuestion.getQuestion());
    }
    
    @Test
    public void testTitleClass() {
        assertEquals("Question française ?", title.getFr());
        assertEquals("English question?", title.getEn());
        
        title.setFr("Nouvelle question");
        title.setEn("New question");
        
        assertEquals("Nouvelle question", title.getFr());
        assertEquals("New question", title.getEn());
    }
    
    @Test
    public void testThemeClass() {
        assertEquals("Science", theme.getTitle());
        assertEquals("SCI", theme.getCode());
        
        theme.setTitle("Mathematics");
        theme.setCode("MAT");
        
        assertEquals("Mathematics", theme.getTitle());
        assertEquals("MAT", theme.getCode());
    }
    
    @Test
    public void testAnswerClass() {
        QuizQuestion.Answer answer = answers.get(0);
        
        assertEquals("ans1", answer.getId());
        assertEquals(true, answer.isValid());
        assertEquals("Réponse 1", answer.getAnswerText());
        
        // Test avec titre null
        answer.setTitle(null);
        assertEquals("", answer.getAnswerText());
        
        // Test avec titre français null
        QuizQuestion.Title titleWithNullFr = new QuizQuestion.Title();
        titleWithNullFr.setEn("English only");
        answer.setTitle(titleWithNullFr);
        assertEquals("", answer.getAnswerText());
    }
    
    @Test
    public void testAnswerValidation() {
        QuizQuestion.Answer validAnswer = answers.get(0);
        QuizQuestion.Answer invalidAnswer = answers.get(1);
        
        assertTrue(validAnswer.isValid());
        assertFalse(invalidAnswer.isValid());
        
        invalidAnswer.setValid(true);
        assertTrue(invalidAnswer.isValid());
    }
    
    @Test
    public void testAnswerText() {
        QuizQuestion.Answer answer = answers.get(0);
        
        assertEquals("Réponse 1", answer.getAnswerText());
        
        // Modifier le texte
        answer.getTitle().setFr("Nouvelle réponse");
        assertEquals("Nouvelle réponse", answer.getAnswerText());
    }
}