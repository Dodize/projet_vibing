package com.example.vibing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibing.R;
import com.example.vibing.models.Team;

import java.util.List;

public class SimpleTeamAdapter extends RecyclerView.Adapter<SimpleTeamAdapter.SimpleViewHolder> {

    private List<Team> teamList;

    public SimpleTeamAdapter(List<Team> teamList) {
        this.teamList = teamList;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Créer un TextView simple au lieu d'utiliser le layout complexe
        TextView textView = new TextView(parent.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setPadding(16, 16, 16, 16);
        return new SimpleViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        Team team = teamList.get(position);
        holder.textView.setText(team.getName());
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}