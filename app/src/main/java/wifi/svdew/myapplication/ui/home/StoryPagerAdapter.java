package wifi.svdew.myapplication.ui.home;

// Adapter for displaying a full-screen image story for each team using a RecyclerView.

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wifi.svdew.myapplication.R;

// RecyclerView Adapter for image-based team stories
public class StoryPagerAdapter extends RecyclerView.Adapter<StoryPagerAdapter.StoryViewHolder> {

    // List of teams to display in the story pager
    private final List<Team> teams;

    // Constructor to initialize the list of teams
    public StoryPagerAdapter(List<Team> teams) {
        this.teams = teams;
    }

    // Inflate the layout for a single story page
    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_story_page, parent, false);
        return new StoryViewHolder(view);
    }

    // Load the player's story image into the ImageView using Picasso
    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Team team = teams.get(position);
        Picasso.get().load(team.getPlayerImageUrl()).into(holder.imageView);
    }

    // Return the number of teams in the list
    @Override
    public int getItemCount() {
        return teams.size();
    }

    // ViewHolder for a single story page
    static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize ImageView for displaying the story image
            imageView = itemView.findViewById(R.id.storyImageView);
        }
    }
}
