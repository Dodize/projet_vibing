package com.example.vibing.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        private Button teamButton;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            teamButton = itemView.findViewById(R.id.teamButton);
        }

        public void bind(Team team) {
            teamButton.setText(team.getName());
            
            try {
                if (team.getColor() != null && !team.getColor().isEmpty()) {
                    teamButton.setBackgroundColor(Color.parseColor(team.getColor()));
                } else if (team.getColorHex() != null && !team.getColorHex().isEmpty()) {
                    teamButton.setBackgroundColor(Color.parseColor(team.getColorHex()));
                } else {
                    teamButton.setBackgroundColor(Color.parseColor("#2196F3"));
                }
            } catch (IllegalArgumentException e) {
                teamButton.setBackgroundColor(Color.parseColor("#2196F3"));
            }
            
            if (selectedTeam != null && selectedTeam.getId().equals(team.getId())) {
                teamButton.setSelected(true);
                teamButton.setAlpha(1.0f);
            } else {
                teamButton.setSelected(false);
                teamButton.setAlpha(0.8f);
            }
            
            teamButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTeamClick(team);
                }
            });
        }
    }
}