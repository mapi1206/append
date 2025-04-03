package wifi.svdew.myapplication.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import java.util.List;

import wifi.svdew.myapplication.R;

public class TeamButtonAdapter extends RecyclerView.Adapter<TeamButtonAdapter.TeamViewHolder> {

    public interface OnTeamClickListener {
        void onTeamClick(Team team);
    }

    private List<Team> teams;
    private OnTeamClickListener listener;

    public TeamButtonAdapter(List<Team> teams, OnTeamClickListener listener) {
        this.teams = teams;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_button, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.name.setText(team.name);
        Glide.with(holder.itemView.getContext())
                .load(team.logoUrl)
                .into(holder.logo);

        holder.itemView.setOnClickListener(v -> listener.onTeamClick(team));
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView logo;
        TextView name;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.teamLogo);
            name = itemView.findViewById(R.id.teamName);
        }
    }
}
