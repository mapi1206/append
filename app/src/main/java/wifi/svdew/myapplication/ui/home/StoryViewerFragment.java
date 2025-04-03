package wifi.svdew.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import wifi.svdew.myapplication.R;

public class StoryViewerFragment extends Fragment {

    private ArrayList<Team> teamList;
    private int startIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.storyViewPager);
        TextView teamNameText = view.findViewById(R.id.teamNameText);
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

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    teamNameText.setText(teamList.get(position).getName());
                }
            });
        }

        closeButton.setOnClickListener(v ->
                NavHostFragment.findNavController(StoryViewerFragment.this).popBackStack()
        );
    }
}
