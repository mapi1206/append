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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
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
                    .navigate(R.id.navigation_dashboard, null, navOptions);
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
        RecyclerView recyclerView = new RecyclerView(requireContext());
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
                    newsContainer.addView(recyclerView);
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
        mockTeams.add(new Team(1, "Egis Körend", "https://upload.wikimedia.org/wikipedia/en/d/d3/Körmendi_KC_logo.png" , "https://www.proballers.com/media/cache/resize_600_png/https---www.proballers.com/ul/player/ferencz-csaba-1f00347f-380f-6de2-b278-8da28cb2205e.png"));
        mockTeams.add(new Team(2, "Szolnoki Olajbányász", "https://upload.wikimedia.org/wikipedia/en/a/af/Szolnoki_Olajbányász_KK_logo.png", "https://i.imgur.com/tGbaZCY.jpg"));
        mockTeams.add(new Team(3, "AS Monaco", "https://upload.wikimedia.org/wikipedia/en/d/d5/AS_Monaco_Basket_Logo.png", "https://upload.wikimedia.org/wikipedia/commons/c/c6/Mike_James_%28basketball%2C_born_1990%29_55_AS_Monaco_Basket_EuroLeague_20241212_%286%29_%28cropped%29.jpg"));
        TeamButtonAdapter adapter = new TeamButtonAdapter(mockTeams, team -> {
            showFullScreenImage(team.getPlayerImageUrl());
        });
        recyclerView.setAdapter(adapter);
    }

    private void showFullScreenImage(String imageUrl) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ImageView imageView = new ImageView(requireContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(requireContext()).load(imageUrl).into(imageView);
        builder.setView(imageView);
        builder.setCancelable(true);
        builder.show();
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
