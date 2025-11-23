package com.example.vibing.ui.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibing.MainActivity;
import com.example.vibing.R;
import com.example.vibing.adapters.TeamSelectionAdapter;
import com.example.vibing.models.Team;
import com.example.vibing.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamSelectionActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private RecyclerView teamsRecyclerView;
    private ProgressBar progressBar;
    private Button continueButton;
    private TeamSelectionAdapter teamAdapter;
    private List<Team> teamList;
    private FirebaseFirestore db;
    private SharedPreferences prefs;
    private Team selectedTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);

        usernameEditText = findViewById(R.id.usernameEditText);
        teamsRecyclerView = findViewById(R.id.teamsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        continueButton = findViewById(R.id.continueButton);

        db = FirebaseFirestore.getInstance();
        prefs = getSharedPreferences("VibingPrefs", MODE_PRIVATE);

        setupRecyclerView();
        loadTeams();
        
        continueButton.setOnClickListener(v -> handleContinue());
        continueButton.setEnabled(false);
        continueButton.setAlpha(0.5f);
        
        usernameEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateContinueButtonState();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupRecyclerView() {
        teamList = new ArrayList<>();
        teamAdapter = new TeamSelectionAdapter(teamList, team -> {
            selectedTeam = team;
            teamAdapter.setSelectedTeam(team);
            updateContinueButtonState();
        });
        
        teamsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        teamsRecyclerView.setAdapter(teamAdapter);
    }

    private void loadTeams() {
        progressBar.setVisibility(View.VISIBLE);
        
        db.collection("teams")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    teamList.clear();
                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Team team = document.toObject(Team.class);
                        team.setId(document.getId());
                        teamList.add(team);
                    }
                    teamAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors du chargement des équipes: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void handleContinue() {
        String username = usernameEditText.getText().toString().trim();
        
        if (username.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un nom d'utilisateur", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedTeam == null) {
            Toast.makeText(this, "Veuillez sélectionner une équipe", Toast.LENGTH_SHORT).show();
            return;
        }
        
        saveUserToDatabase(username, selectedTeam);
    }
    
    private void updateContinueButtonState() {
        String username = usernameEditText.getText().toString().trim();
        boolean isValid = !username.isEmpty() && selectedTeam != null;
        continueButton.setEnabled(isValid);
        continueButton.setAlpha(isValid ? 1.0f : 0.5f);
    }

    private void saveUserToDatabase(String username, Team team) {
        progressBar.setVisibility(View.VISIBLE);
        
        User user = new User(username, team.getId(), team.getName(), team.getColor());
        user.setId(UUID.randomUUID().toString());
        
        db.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    saveUserPreferences(user);
                    progressBar.setVisibility(View.GONE);
                    
                    Intent intent = new Intent(TeamSelectionActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de l'enregistrement: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void saveUserPreferences(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("team_id", user.getTeamId());
        editor.putString("team_name", user.getTeamName());
        editor.putString("team_color", user.getTeamColor());
        editor.putBoolean("is_first_launch", false);
        editor.apply();
    }
}