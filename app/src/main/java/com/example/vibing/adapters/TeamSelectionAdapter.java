package com.example.vibing.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.widget.AppCompatButton;
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

    public interface OnTeamClickListener {
        void onTeamClick(Team team);
    }

    public TeamSelectionAdapter(List<Team> teamList, OnTeamClickListener listener) {
        this.teamList = teamList;
        this.listener = listener;
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

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            teamButton = itemView.findViewById(R.id.teamButton);
        }

        public void bind(Team team) {
            teamButton.setText(team.getName());
            
            String teamColor = "#2196F3"; // Couleur par défaut
            try {
                if (team.getColor() != null && !team.getColor().isEmpty()) {
                    teamColor = team.getColor();
                } else if (team.getColorHex() != null && !team.getColorHex().isEmpty()) {
                    teamColor = team.getColorHex();
                }
            } catch (Exception e) {
                teamColor = "#2196F3";
            }
            
            if (selectedTeam != null && selectedTeam.getId().equals(team.getId())) {
                // Sélectionné : fond couleur équipe, texte blanc
                teamButton.setSelected(true);
                teamButton.setTextColor(Color.WHITE);
                
                // Créer un background avec la couleur de l'équipe
                android.graphics.drawable.GradientDrawable selectedDrawable = new android.graphics.drawable.GradientDrawable();
                selectedDrawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                selectedDrawable.setCornerRadius(8f);
                selectedDrawable.setColor(Color.parseColor(teamColor));
                teamButton.setBackground(selectedDrawable);
            } else {
                // Non sélectionné : fond blanc, contour couleur équipe, texte couleur équipe
                teamButton.setSelected(false);
                teamButton.setTextColor(Color.parseColor(teamColor));
                
                // Créer un background blanc avec contour couleur équipe
                android.graphics.drawable.GradientDrawable unselectedDrawable = new android.graphics.drawable.GradientDrawable();
                unselectedDrawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                unselectedDrawable.setCornerRadius(8f);
                unselectedDrawable.setColor(Color.WHITE);
                unselectedDrawable.setStroke(4, Color.parseColor(teamColor));
                teamButton.setBackground(unselectedDrawable);
            }
            
            teamButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTeamClick(team);
                }
            });
        }
    }
}