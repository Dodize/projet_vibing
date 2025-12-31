package com.example.vibing.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.widget.AppCompatButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibing.R;
import com.example.vibing.models.Team;

import java.util.List;

public class TeamSelectionAdapter extends RecyclerView.Adapter<TeamSelectionAdapter.TeamViewHolder> {

    private List<Team> teamList;
    private Team selectedTeam;
    private OnTeamClickListener listener;
    private int totalPlayers;

    public interface OnTeamClickListener {
        void onTeamClick(Team team);
    }

public TeamSelectionAdapter(List<Team> teamList, OnTeamClickListener listener) {
        this.teamList = teamList;
        this.listener = listener;
        this.totalPlayers = 0;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_selection, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teamList.get(position);
        holder.bind(team);
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public void setSelectedTeam(Team team) {
        this.selectedTeam = team;
        notifyDataSetChanged();
    }

class TeamViewHolder extends RecyclerView.ViewHolder {
        private AppCompatButton teamButton;
        private TextView teamInfoText;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            teamButton = itemView.findViewById(R.id.teamButton);
            teamInfoText = itemView.findViewById(R.id.teamInfoText);
        }

public void bind(Team team) {
            teamButton.setText(team.getName());
            
            String teamColor = null;
            try {
                // Priorité à colorHex qui est le champ utilisé dans Firebase
                if (team.getColorHex() != null && !team.getColorHex().isEmpty()) {
                    teamColor = team.getColorHex();
                } else if (team.getColor() != null && !team.getColor().isEmpty()) {
                    teamColor = team.getColor();
                }
            } catch (Exception e) {
                teamColor = null;
            }
            
            // Si aucune couleur n'est trouvée, utiliser une couleur par défaut différente pour chaque équipe
            if (teamColor == null) {
                String[] defaultColors = {"#FF0000", "#0000FF", "#00FF00", "#FFA500", "#800080"};
                int position = teamList.indexOf(team);
                teamColor = defaultColors[position % defaultColors.length];
            }
            
            // Mettre à jour le texte d'information
            if (totalPlayers > 0) {
                int remainingSlots = team.getRemainingSlots(teamList.size(), totalPlayers);
                String infoText;
                if (remainingSlots == Integer.MAX_VALUE) {
                    infoText = "Places illimitées";
                } else if (remainingSlots > 0) {
                    infoText = remainingSlots + " place(s) restante(s)";
                } else {
                    infoText = "Équipe complète";
                }
                teamInfoText.setText(infoText);
                
                // Désactiver le bouton si l'équipe est complète
                teamButton.setEnabled(remainingSlots > 0);
                if (remainingSlots == 0) {
                    teamButton.setAlpha(0.5f);
                } else {
                    teamButton.setAlpha(1.0f);
                }
            } else {
                teamInfoText.setText("Chargement...");
                teamButton.setEnabled(true);
                teamButton.setAlpha(1.0f);
            }
            
            // Appliquer la couleur dynamique avec dégradé
            teamButton.setTextColor(Color.WHITE);
            
            // Créer un dégradé à partir de la couleur de l'équipe
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
            gradientDrawable.setCornerRadius(24f);
            
            // Créer un dégradé à partir de la couleur récupérée
            int baseColor = Color.parseColor(teamColor);
            int lighterColor = lightenColor(baseColor, 0.3f); // Version plus claire de la couleur
            
            gradientDrawable.setColors(new int[]{baseColor, lighterColor});
            gradientDrawable.setGradientType(android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setOrientation(android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT);
            
            teamButton.setBackground(gradientDrawable);
            
            teamButton.setOnClickListener(v -> {
                if (listener != null && teamButton.isEnabled()) {
                    listener.onTeamClick(team);
                }
            });
        }
    }
    
    /**
     * Éclaircit une couleur en augmentant ses composantes RGB
     * @param color Couleur d'origine
     * @param factor Facteur d'éclaircissement (0.0 = aucune modification, 1.0 = blanc)
     * @return Couleur éclaircie
     */
    private int lightenColor(int color, float factor) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        
        red = (int) Math.min(255, red + (255 - red) * factor);
        green = (int) Math.min(255, green + (255 - green) * factor);
        blue = (int) Math.min(255, blue + (255 - blue) * factor);
        
        return Color.rgb(red, green, blue);
    }
}