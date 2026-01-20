package com.example.vibing.ui.dashboard;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class DashboardViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DashboardViewModel dashboardViewModel;

    @Before
    public void setUp() {
        dashboardViewModel = new DashboardViewModel();
    }

    @Test
    public void testInitialText() {
        String initialText = dashboardViewModel.getText().getValue();
        assertEquals("This is dashboard fragment", initialText);
    }

    @Test
    public void testGetText() {
        String text = dashboardViewModel.getText().getValue();
        assertNotNull(text);
        assertEquals("This is dashboard fragment", text);
    }

    @Test
    public void testTextLiveData() {
        // Test that the LiveData is properly initialized
        assertNotNull(dashboardViewModel.getText());
        
        // Test that the LiveData has the expected initial value
        String text = dashboardViewModel.getText().getValue();
        assertNotNull(text);
        assertFalse(text.isEmpty());
        assertEquals("This is dashboard fragment", text);
    }

    @Test
    public void testViewModelCreation() {
        // Test that ViewModel can be created without errors
        DashboardViewModel newViewModel = new DashboardViewModel();
        assertNotNull(newViewModel);
        assertNotNull(newViewModel.getText());
    }
}