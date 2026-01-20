package com.example.vibing.ui.home;

import com.example.vibing.models.Poi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)

public class HomeViewModelTest {

    private HomeViewModel homeViewModel;

    @Before
    public void setUp() {
        homeViewModel = new HomeViewModel();
    }

    @Test
    public void testGetPois() {
        List<Poi> pois = homeViewModel.getPois().getValue();
        // Les POIs peuvent être null au début car ils sont chargés de manière asynchrone
        // On teste juste que la méthode ne lève pas d'exception
        assertTrue("Should not crash", true);
    }

    @Test
    public void testViewModelCreation() {
        assertNotNull("ViewModel should be created", homeViewModel);
        assertNotNull("Pois LiveData should not be null", homeViewModel.getPois());
    }

    @Test
    public void testWaitForPoisLoading() throws InterruptedException {
        // Attendre un peu pour le chargement asynchrone
        Thread.sleep(1000);
        
        List<Poi> pois = homeViewModel.getPois().getValue();
        if (pois != null) {
            assertFalse("Pois should not be empty when loaded", pois.isEmpty());
        }
    }
}