package wifi.svdew.myapplication.ui.home;

// Fragment to display full-screen image stories with automatic progress and navigation.

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import wifi.svdew.myapplication.R;

// Displays a fullscreen story viewer with team name, logo, and swipeable stories
public class StoryViewerFragment extends Fragment {

    // List of teams to be shown in the story viewer
    private ArrayList<Team> teamList;

    // Index of the team to start the story from
    private int startIndex;

    // Handler and animation-related components for managing story timer and UI
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable storyRunnable;
    private ObjectAnimator animator;
    private ProgressBar progressBar;
    private ViewPager2 viewPager;

    // Inflate the layout for the story viewer fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_viewer, container, false);
    }

    // Initialize story UI and set up interactions
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        }

        viewPager = view.findViewById(R.id.storyViewPager);
        progressBar = view.findViewById(R.id.storyProgressBar);
        TextView teamNameText = view.findViewById(R.id.teamNameText);
        ImageView teamLogoImage = view.findViewById(R.id.teamLogoImage);
        ImageButton closeButton = view.findViewById(R.id.closeButton);

        Bundle args = getArguments();
        if (args != null) {
            teamList = args.getParcelableArrayList("team_list");
            startIndex = args.getInt("start_index", 0);
        }

        if (teamList != null) {
            StoryPagerAdapter adapter = new StoryPagerAdapter(teamList);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(startIndex, false);
            teamNameText.setText(teamList.get(startIndex).getName());

            teamLogoImage.setImageResource(teamList.get(startIndex).getPlayerImageUrl());

            startStoryTimer(startIndex);

            // Update team name and logo when page changes, and restart story timer
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    teamNameText.setText(teamList.get(position).getName());
                    teamLogoImage.setImageResource(teamList.get(position).getLogo());  // Replace with getLogo() for the team logo
                    startStoryTimer(position);
                }
            });
        }

        // Close the story viewer and navigate back
        closeButton.setOnClickListener(v ->
                NavHostFragment.findNavController(StoryViewerFragment.this).popBackStack()
        );
    }

    // Restore the action bar when the fragment is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        }
    }

    // Start or restart the timer for the current story, and handle navigation to the next one
    private void startStoryTimer(int position) {
        if (!isAdded()) return;
        if (animator != null) animator.cancel();
        if (storyRunnable != null) handler.removeCallbacks(storyRunnable);

        // Animate the progress bar for the story duration
        progressBar.setProgress(0);
        animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 1000);
        animator.setDuration(8000);
        animator.start();

        // Automatically move to next story after delay, or return home when finished
        storyRunnable = () -> {
            if (!isAdded()) return;
            int nextItem = position + 1;
            if (nextItem < teamList.size()) {
                viewPager.setCurrentItem(nextItem, true);
            } else {
                NavHostFragment.findNavController(StoryViewerFragment.this)
                        .navigate(R.id.action_storyViewerFragment_to_navigation_home);
            }
        };
        handler.postDelayed(storyRunnable, 8000);
    }
}
