package wifi.svdew.myapplication.ui.home;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wifi.svdew.myapplication.R;
import wifi.svdew.myapplication.datenbank.DatabaseHelper;
import wifi.svdew.myapplication.ui.news.ApiClient;
import wifi.svdew.myapplication.ui.news.NewsApiInterface;
import wifi.svdew.myapplication.ui.news.NewsRecycleAdapter;

public class HomeFragment extends Fragment {

    private ViewPager2 tablePager;
    private LinearLayout newsContainer;

    private static final String API_KEY = "f58a9be9e97c4238993864b43e768db1";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        tablePager = view.findViewById(R.id.tablePager);
        newsContainer = view.findViewById(R.id.newsContainer);

        RecyclerView topButtonRecycler = view.findViewById(R.id.topButtonRecycler);
        topButtonRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        loadMockTopButtons(topButtonRecycler);

        loadTables();
        loadTopNews();

        Button moreNewsButton = view.findViewById(R.id.moreNewsButton);
        moreNewsButton.setOnClickListener(v -> {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, true)
                    .build();
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.navigation_notifications, null, navOptions);
        });

        Button moreTablesButton = view.findViewById(R.id.moreTablesButton);
        moreTablesButton.setOnClickListener(v -> {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, true)
                    .build();
        NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.tabella, null, navOptions);
        });
    }

    private void loadTables() {
        DatabaseHelper db = new DatabaseHelper(getContext());

        List<TableLayout> tableLayouts = new ArrayList<>();

        for (int t = 1; t <= 2; t++) {
            TableLayout table = new TableLayout(requireContext());
            table.setStretchAllColumns(true);
            table.setPadding(16, 8, 16, 8);

            TableRow header = new TableRow(requireContext());
            header.setBackgroundColor(Color.DKGRAY);
            header.addView(makeCell("H", true));
            header.addView(makeCell("Team", true));
            header.addView(makeCell("M", true));
            header.addView(makeCell("W", true));
            header.addView(makeCell("L", true));
            table.addView(header);

            String tableName = (t == 1) ? "teams" : "euroleague";
            Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + tableName + " LIMIT 4", null);
            if (cursor.moveToFirst()) {
                int index = 1;
                do {
                    TableRow row = new TableRow(requireContext());
                    row.addView(makeCell(String.valueOf(index), false));
                    row.addView(makeCell(cursor.getString(cursor.getColumnIndexOrThrow("team_name")), false));
                    row.addView(makeCell(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("matches"))), false));
                    row.addView(makeCell(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("wins"))), false));
                    row.addView(makeCell(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("losses"))), false));
                    table.addView(row);
                    index++;
                } while (cursor.moveToNext());
                cursor.close();
            }

            tableLayouts.add(table);
        }

        tablePager.setAdapter(new RecyclerView.Adapter<>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                FrameLayout frame = new FrameLayout(parent.getContext());
                frame.setLayoutParams(new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                return new RecyclerView.ViewHolder(frame) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                FrameLayout frame = (FrameLayout) holder.itemView;
                frame.removeAllViews();
                frame.addView(tableLayouts.get(position));
            }

            @Override
            public int getItemCount() {
                return tableLayouts.size();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadTopNews() {
        RecyclerView recyclerView = requireView().findViewById(R.id.newsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setNestedScrollingEnabled(false);

        NewsApiInterface apiInterface = ApiClient.getRetrofitInstance().create(NewsApiInterface.class);

        Call<ArticleResponse> call = apiInterface.getTopHeadlines("general", null, "en", API_KEY);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull Response<ArticleResponse> response) {
                if (response.body() != null && response.body().getArticles() != null) {
                    List<Article> allArticles = response.body().getArticles();
                    List<Article> topArticles = allArticles.size() > 6 ? allArticles.subList(0, 6) : allArticles;

                    NewsRecycleAdapter adapter = new NewsRecycleAdapter(topArticles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {
                Log.e("HomeFragment", "Hírek betöltése sikertelen", t);
            }
        });
    }

    private void loadMockTopButtons(RecyclerView recyclerView) {
        List<Team> mockTeams = new ArrayList<>();
        mockTeams.add(new Team(1, "Egis Körmend", "https://upload.wikimedia.org/wikipedia/en/d/d3/Körmendi_KC_logo.png", "https://i.imgur.com/tGbaZCY.jpg"));
        mockTeams.add(new Team(2, "Szolnoki Olajbányász", "https://upload.wikimedia.org/wikipedia/en/a/af/Szolnoki_Olajbányász_KK_logo.png", "https://example.com/player2.jpg"));
        mockTeams.add(new Team(3, "AS Monaco", "https://upload.wikimedia.org/wikipedia/en/d/d5/AS_Monaco_Basket_Logo.png", "https://example.com/player3.jpg"));
        mockTeams.add(new Team(4, "Falco", "https://static.szombathelypont.hu/image/2021/09//thax2-falcovulcanoenergiakcszombathely-5cf61f.jpg", "https://example.com/player4.jpg"));
        mockTeams.add(new Team(5, "Panathinaikos", "https://upload.wikimedia.org/wikipedia/en/2/2b/Panathinaikos_BC_logo.png", "https://example.com/player5.jpg"));
        mockTeams.add(new Team(6, "Partizan Belgrade", "https://images.seeklogo.com/logo-png/53/1/partizan-logo-png_seeklogo-531300.png", "https://example.com/player6.jpg"));
        mockTeams.add(new Team(7, "Red Stars Belgrade", "https://upload.wikimedia.org/wikipedia/en/thumb/0/01/KK_Crvena_zvezda_logo.svg/800px-KK_Crvena_zvezda_logo.svg.png", "https://example.com/player7.jpg"));
        mockTeams.add(new Team(8, "Real Madrid", "https://upload.wikimedia.org/wikipedia/en/b/be/Real_Madrid_Baloncesto.png", "https://example.com/player8.jpg"));
        mockTeams.add(new Team(9, "Barcelona", "https://images.seeklogo.com/logo-png/35/1/fc-barcelona-basketball-logo-png_seeklogo-352154.png?v=1955019485352702584", "https://example.com/player9.jpg"));

        TeamButtonAdapter adapter = new TeamButtonAdapter(mockTeams, team -> {
            int position = mockTeams.indexOf(team);
            Bundle args = new Bundle();
            args.putParcelableArrayList("team_list", new ArrayList<>(mockTeams));
            args.putInt("start_index", position);
            NavController navController = NavHostFragment.findNavController(HomeFragment.this);
            navController.navigate(R.id.action_navigation_home_to_storyViewerFragment, args);
        });
        recyclerView.setAdapter(adapter);
    }

    private TextView makeCell(String text, boolean header) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(12, 12, 12, 12);
        tv.setTextSize(header ? 16 : 14);
        tv.setBackgroundColor(header ? Color.DKGRAY : Color.TRANSPARENT);
        return tv;
    }
}
