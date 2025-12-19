package com.example.vibing.ui.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        updateContinueButtonState();
        
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
            if (canJoinTeam(team)) {
                selectedTeam = team;
                teamAdapter.setSelectedTeam(team);
                updateContinueButtonState();
            } else {
                Toast.makeText(this, "Cette équipe est complète ou ne respecte pas l'équilibre", Toast.LENGTH_SHORT).show();
            }
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
                    loadTeamMemberCounts();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors du chargement des équipes: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void loadTeamMemberCounts() {
        if (teamList.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Compter le nombre total de joueurs
        db.collection("users")
                .get()
                .addOnSuccessListener(userSnapshots -> {
                    int totalPlayers = userSnapshots.size();
                    int totalPlayersWithNewUser = totalPlayers + 1;
                    
                    // Utiliser un compteur pour savoir quand toutes les équipes ont été traitées
                    final int[] teamsProcessed = {0};
                    
                    // Compter les membres par équipe
                    for (Team team : teamList) {
                        final String teamId = team.getId();
                        db.collection("users")
                                .whereEqualTo("teamId", teamId)
                                .get()
                                .addOnSuccessListener(teamUserSnapshots -> {
                                    team.setMemberCount(teamUserSnapshots.size());
                                    teamsProcessed[0]++;
                                    
                                    // Mettre à jour l'adapter uniquement quand toutes les équipes ont été traitées
                                    if (teamsProcessed[0] == teamList.size()) {
                                        teamAdapter.setTotalPlayers(totalPlayersWithNewUser);
                                        teamAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    teamsProcessed[0]++;
                                    if (teamsProcessed[0] == teamList.size()) {
                                        teamAdapter.setTotalPlayers(totalPlayersWithNewUser);
                                        teamAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors du chargement des joueurs: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        
        if (isValid) {
            continueButton.setAlpha(1.0f);
            continueButton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple, null));
        } else {
            continueButton.setAlpha(0.3f);
            continueButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray, null));
        }
    }

    private void saveUserToDatabase(String username, Team team) {
        progressBar.setVisibility(View.VISIBLE);
        
        User user = new User(username, team.getId(), team.getName());
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
        editor.putInt("money", user.getMoney());
        editor.apply();
        
        // Create first launch file to indicate user has been created
        createFirstLaunchFile();
    }
    
    private void createFirstLaunchFile() {
        try {
            File firstLaunchFile = new File(getFilesDir(), "first_launch.txt");
            FileOutputStream fos = new FileOutputStream(firstLaunchFile);
            fos.write("first_launch_completed".getBytes());
            fos.close();
        } catch (IOException e) {
            // Log error but don't fail the user creation process
            android.util.Log.e("TeamSelectionActivity", "Error creating first launch file", e);
        }
    }

    private boolean canJoinTeam(Team team) {
        if (teamList.isEmpty()) return true;
        
        int totalTeams = teamList.size();
        int currentTotalPlayers = 0;
        
        // Calculer le nombre total de joueurs actuel
        for (Team t : teamList) {
            currentTotalPlayers += t.getMemberCount();
        }
        
        // Utiliser la même logique que l'affichage : totalPlayers + 1 pour le nouvel utilisateur
        int totalPlayersWithNewUser = currentTotalPlayers + 1;
        
        // Utiliser la même méthode que l'affichage pour la cohérence
        return team.getRemainingSlots(totalTeams, totalPlayersWithNewUser) > 0;
    }
}