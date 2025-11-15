package com.example.vibing.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibing.R;
import com.example.vibing.models.Team;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private List<Team> teamList;
    private OnTeamClickListener listener;

    public interface OnTeamClickListener {
        void onTeamClick(Team team);
    }

    public TeamAdapter(List<Team> teamList, OnTeamClickListener listener) {
        this.teamList = teamList;
        this.listener = listener;
        android.util.Log.d("TeamAdapter", "TeamAdapter created with " + teamList.size() + " teams");
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.util.Log.d("TeamAdapter", "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        android.util.Log.d("TeamAdapter", "onBindViewHolder called for position " + position);
        Team team = teamList.get(position);
        holder.textViewTeamName.setText(team.getName());
        
        try {
            holder.viewTeamColor.setBackgroundColor(Color.parseColor(team.getColor()));
        } catch (IllegalArgumentException e) {
            holder.viewTeamColor.setBackgroundColor(Color.GRAY);
        }
        
        holder.itemView.setOnClickListener(v -> {
            android.util.Log.d("TeamAdapter", "Team clicked: " + team.getName());
            if (listener != null) {
                listener.onTeamClick(team);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTeamName;
        View viewTeamColor;

        TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTeamName = itemView.findViewById(R.id.text_view_team_name);
            viewTeamColor = itemView.findViewById(R.id.viewTeamColor);
        }
    }
}