package wifi.svdew.myapplication.ui.home;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;


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

    private LinearLayout tableContainer;
    private LinearLayout newsContainer;
    private DatabaseHelper dbHelper;

    private static final String API_KEY = "f58a9be9e97c4238993864b43e768db1";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        dbHelper = new DatabaseHelper(requireContext());

        tableContainer = view.findViewById(R.id.tableContainer);
        newsContainer = view.findViewById(R.id.newsContainer);

        loadTables();
        loadTopNews();

        Button moreNewsButton = view.findViewById(R.id.moreNewsButton);
        moreNewsButton.setOnClickListener(v ->
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.navigation_notifications));
    }

    private void loadTables() {
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

            for (int i = 0; i < 3; i++) {
                TableRow row = new TableRow(requireContext());
                row.addView(makeCell(String.valueOf(i + 1), false));
                row.addView(makeCell("Team " + (i + 1 + (t - 1) * 3), false));
                row.addView(makeCell("30", false));
                row.addView(makeCell("18", false));
                row.addView(makeCell("12", false));
                table.addView(row);
            }

            tableContainer.addView(table);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadTopNews() {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setNestedScrollingEnabled(false);

        NewsApiInterface apiInterface = ApiClient.getRetrofitInstance().create(NewsApiInterface.class);

        Call<ArticleResponse> call = apiInterface.getTopHeadlines("general", null, "en", API_KEY);
        call.enqueue(new Callback<ArticleResponse>() {
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
