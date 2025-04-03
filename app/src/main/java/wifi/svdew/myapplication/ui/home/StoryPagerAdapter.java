package wifi.svdew.myapplication.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wifi.svdew.myapplication.R;

public class StoryPagerAdapter extends RecyclerView.Adapter<StoryPagerAdapter.StoryViewHolder> {

    private final List<Team> teams;

    public StoryPagerAdapter(List<Team> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_story_page, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Team team = teams.get(position);
        Picasso.get().load(team.getPlayerImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.storyImageView);
        }
    }
}
