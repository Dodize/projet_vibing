package com.example.vibing.ui.score;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.vibing.ui.quiz.QuizResultDialog;

@RunWith(AndroidJUnit4.class)
public class QuizResultDialogTest {

    private Context context;
    private FragmentManager fragmentManager;

    @Mock
    private QuizResultDialog.QuizResultListener mockListener;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        context = ApplicationProvider.getApplicationContext();
        fragmentManager = mock(FragmentManager.class);
    }

    @Test
    public void testDialogCreation() {
        // Test that dialog can be created without errors
        QuizResultDialog dialog = QuizResultDialog.newInstance(
            "Test Zone",
            QuizResultDialog.ResultType.SUCCESS,
            "team_1",
            85,
            100
        );
        
        assertNotNull(dialog);
        assertEquals("Test Zone", dialog.getPoiName());
        assertEquals(QuizResultDialog.ResultType.SUCCESS, dialog.getResultType());
        assertEquals("team_1", dialog.getTeamId());
        assertEquals(85, dialog.getQuizScore());
        assertEquals(100, dialog.getZoneScore());
    }

    @Test
    public void testSuccessListener() {
        // Test success dialog creation
        QuizResultDialog dialog = QuizResultDialog.newInstance(
            "Capitole",
            QuizResultDialog.ResultType.SUCCESS,
            "team_2",
            120,
            100
        );
        
        assertEquals(QuizResultDialog.ResultType.SUCCESS, dialog.getResultType());
        assertTrue("Success dialog should have positive outcome", dialog.isSuccess());
    }

    @Test
    public void testFailureListener() {
        // Test failure dialog creation
        QuizResultDialog dialog = QuizResultDialog.newInstance(
            "Jardin Royal",
            QuizResultDialog.ResultType.FAILURE,
            "team_1",
            60,
            100
        );
        
        assertEquals(QuizResultDialog.ResultType.FAILURE, dialog.getResultType());
        assertFalse("Failure dialog should have negative outcome", dialog.isSuccess());
    }

    @Test
    public void testSurrenderListener() {
        // Test surrender dialog creation
        QuizResultDialog dialog = QuizResultDialog.newInstance(
            "Pont Neuf",
            QuizResultDialog.ResultType.SURRENDER,
            "team_3",
            0,
            50
        );
        
        assertEquals(QuizResultDialog.ResultType.SURRENDER, dialog.getResultType());
        assertFalse("Surrender dialog should have negative outcome", dialog.isSuccess());
        assertTrue("Surrender should give money bonus", dialog.hasMoneyBonus());
    }

    @Test
    public void testDialogArguments() {
        // Test that arguments are properly passed
        QuizResultDialog dialog = QuizResultDialog.newInstance(
            "Zone Test",
            QuizResultDialog.ResultType.SUCCESS,
            "team_4",
            95,
            80
        );
        
        Bundle args = dialog.getArguments();
        assertNotNull(args);
        assertEquals("Zone Test", args.getString("poi_name"));
        assertEquals("success", args.getString("result_type"));
        assertEquals("team_4", args.getString("team_id"));
        assertEquals(95, args.getInt("quiz_score"));
        assertEquals(80, args.getInt("zone_score"));
    }

    @Test
    public void testResultTypeEnum() {
        // Test that all result types are properly defined
        assertEquals(3, QuizResultDialog.ResultType.values().length);
        
        assertTrue(QuizResultDialog.ResultType.SUCCESS.isSuccess());
        assertFalse(QuizResultDialog.ResultType.SUCCESS.hasMoneyBonus());
        
        assertFalse(QuizResultDialog.ResultType.FAILURE.isSuccess());
        assertFalse(QuizResultDialog.ResultType.FAILURE.hasMoneyBonus());
        
        assertFalse(QuizResultDialog.ResultType.SURRENDER.isSuccess());
        assertTrue(QuizResultDialog.ResultType.SURRENDER.hasMoneyBonus());
    }

    @Test
    public void testDialogContentGeneration() {
        // Test that dialog content is generated correctly
        QuizResultDialog successDialog = QuizResultDialog.newInstance(
            "Test Zone",
            QuizResultDialog.ResultType.SUCCESS,
            "team_1",
            110,
            100
        );
        
        String title = successDialog.generateTitle();
        String message = successDialog.generateMessage();
        
        assertTrue("Success title should contain success", title.toLowerCase().contains("succès") || title.toLowerCase().contains("capture"));
        assertTrue("Success message should contain team", message.contains("équipe"));
        assertTrue("Success message should mention daily limit", message.contains("jour"));
        
        QuizResultDialog failureDialog = QuizResultDialog.newInstance(
            "Test Zone",
            QuizResultDialog.ResultType.FAILURE,
            "team_1",
            70,
            100
        );
        
        String failureMessage = failureDialog.generateMessage();
        assertTrue("Failure message should mention money loss", failureMessage.contains("argent") || failureMessage.contains("perdu"));
        assertTrue("Failure message should mention daily limit", failureMessage.contains("jour"));
        
        QuizResultDialog surrenderDialog = QuizResultDialog.newInstance(
            "Test Zone",
            QuizResultDialog.ResultType.SURRENDER,
            "team_1",
            0,
            50
        );
        
        String surrenderMessage = surrenderDialog.generateMessage();
        assertTrue("Surrender message should mention money gain", surrenderMessage.contains("argent") || surrenderMessage.contains("gagn"));
        assertTrue("Surrender message should mention daily limit", surrenderMessage.contains("jour"));
    }
}