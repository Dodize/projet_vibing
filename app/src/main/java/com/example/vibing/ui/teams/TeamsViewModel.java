package com.example.vibing.ui.teams;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vibing.models.Team;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeamsViewModel extends ViewModel {

    private MutableLiveData<List<Team>> teams;
    private FirebaseFirestore db;

    public TeamsViewModel() {
        teams = new MutableLiveData<>();
        
        try {
            db = FirebaseFirestore.getInstance();
            loadTeams();
        } catch (Exception e) {
            // Handle exception silently
        }
    }

    public LiveData<List<Team>> getTeams() {
        return teams;
    }

    private void loadTeams() {
        db.collection("teams")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Team> teamList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Team team = document.toObject(Team.class);
                        team.setId(document.getId());
                        teamList.add(team);
                    }
                    teams.setValue(teamList);
                });
    }
}