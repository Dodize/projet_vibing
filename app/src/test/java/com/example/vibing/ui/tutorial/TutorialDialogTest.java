package com.example.vibing.ui.tutorial;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vibing.R;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class TutorialDialogTest {

    @Mock
    private Context mockContext;
    
    @Mock
    private TutorialDialog.TutorialDialogListener mockListener;

    private TutorialDialog tutorialDialog;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockContext.getString(R.string.app_name)).thenReturn("Vibing");
        tutorialDialog = new TutorialDialog(mockContext, mockListener);
    }

    @Test
    public void testTutorialDialogInitialization() {
        assertNotNull(tutorialDialog);
        assertEquals(0, tutorialDialog.getCurrentStep());
        assertEquals(6, tutorialDialog.getTotalSteps());
    }

    @Test
    public void testStepNavigation() {
        // Test initial step
        assertEquals(0, tutorialDialog.getCurrentStep());
        
        // Test next step
        tutorialDialog.showNextStep();
        assertEquals(1, tutorialDialog.getCurrentStep());
        
        // Test previous step
        tutorialDialog.showPreviousStep();
        assertEquals(0, tutorialDialog.getCurrentStep());
    }

    @Test
    public void testStepBoundaries() {
        // Test going previous from first step (should stay at 0)
        tutorialDialog.showPreviousStep();
        assertEquals(0, tutorialDialog.getCurrentStep());
        
        // Test going to last step
        for (int i = 0; i < 6; i++) {
            tutorialDialog.showNextStep();
        }
        assertEquals(5, tutorialDialog.getCurrentStep()); // Should be at last step (index 5)
        
        // Test going next from last step (should stay at last)
        tutorialDialog.showNextStep();
        assertEquals(5, tutorialDialog.getCurrentStep());
    }

    @Test
    public void testStepContent() {
        // Test that each step has appropriate content
        String[] stepTitles = tutorialDialog.getStepTitles();
        String[] stepContents = tutorialDialog.getStepContents();
        
        assertEquals(6, stepTitles.length);
        assertEquals(6, stepContents.length);
        
        // Verify first step content (welcome)
        assertTrue(stepTitles[0].contains("Bienvenue"));
        assertTrue(stepContents[0].contains("conquête"));
        
        // Verify last step content (conclusion)
        assertTrue(stepTitles[5].contains("Bonne chance"));
        assertTrue(stepContents[5].contains("courage"));
    }

    @Test
    public void testListenerCallbacks() {
        // Test onTutorialStarted callback
        tutorialDialog.show();
        verify(mockListener, times(1)).onTutorialStarted();
        
        // Test onTutorialFinished callback when completing all steps
        for (int i = 0; i < 6; i++) {
            tutorialDialog.showNextStep();
        }
        verify(mockListener, times(1)).onTutorialFinished();
        
        // Test onTutorialClosed callback
        tutorialDialog.dismiss();
        verify(mockListener, times(1)).onTutorialClosed();
    }

    @Test
    public void testStepSpecificFeatures() {
        // Test step 2 (POI zoom feature)
        tutorialDialog.setCurrentStep(1); // Step 2 (0-indexed)
        assertTrue(tutorialDialog.shouldShowMapZoom());
        
        // Test step 5 (money highlight feature)
        tutorialDialog.setCurrentStep(4); // Step 5 (0-indexed)
        assertTrue(tutorialDialog.shouldHighlightMoneySection());
        
        // Test other steps don't have these features
        tutorialDialog.setCurrentStep(0); // Step 1
        assertFalse(tutorialDialog.shouldShowMapZoom());
        assertFalse(tutorialDialog.shouldHighlightMoneySection());
    }

    @Test
    public void testDialogDismissal() {
        // Test that dialog can be dismissed properly
        tutorialDialog.show();
        assertTrue(tutorialDialog.isShowing());
        
        tutorialDialog.dismiss();
        assertFalse(tutorialDialog.isShowing());
    }

    @Test
    public void testButtonStates() {
        // Test that previous button is disabled on first step
        tutorialDialog.setCurrentStep(0);
        assertFalse(tutorialDialog.isPreviousButtonEnabled());
        assertTrue(tutorialDialog.isNextButtonEnabled());
        
        // Test that next button shows "Terminer" on last step
        tutorialDialog.setCurrentStep(5);
        assertTrue(tutorialDialog.isPreviousButtonEnabled());
        assertTrue(tutorialDialog.isNextButtonEnabled());
        assertEquals("Terminer", tutorialDialog.getNextButtonText());
        
        // Test middle steps
        tutorialDialog.setCurrentStep(2);
        assertTrue(tutorialDialog.isPreviousButtonEnabled());
        assertTrue(tutorialDialog.isNextButtonEnabled());
        assertEquals("Suivant", tutorialDialog.getNextButtonText());
    }
}