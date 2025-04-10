package wifi.svdew.myapplication.ui.news;

// Fragment for displaying top news headlines and handling search and category filters
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wifi.svdew.myapplication.databinding.FragmentNewsBinding;

public class NewsFragment extends Fragment implements View.OnClickListener {

    // View binding object for accessing UI elements
    private FragmentNewsBinding binding;

    // RecyclerView, adapter, progress indicator, category buttons, and search view
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecycleAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
    androidx.appcompat.widget.SearchView searchView;

    // API key for accessing the news service
    private static final String API_KEY = "f58a9be9e97c4238993864b43e768db1"; // Az API kulcs

    // Inflate the layout, initialize UI components, and set up listeners
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.newsRecyclerView;
        progressIndicator = binding.progressBar;
        searchView = binding.searchView;
        btn1 = binding.btn1;
        btn2 = binding.btn2;
        btn3 = binding.btn3;
        btn4 = binding.btn4;
        btn5 = binding.btn5;
        btn6 = binding.btn6;
        btn7 = binding.btn7;

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);

        // Set listener for search queries
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("general", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setupRecyclerView();
        getNews("general", null);

        return root;
    }

    // Clear binding when view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Set up the RecyclerView with layout manager and adapter
    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecycleAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    // Show or hide the progress indicator
    void changeInProgress(boolean show) {
        if (show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    // Fetch news articles using the News API based on category and query
    void getNews(String category, String query) {
        changeInProgress(true);  // Töltősáv megjelenítése
        NewsApiInterface apiInterface = ApiClient.getRetrofitInstance().create(NewsApiInterface.class);

        Call<ArticleResponse> call = apiInterface.getTopHeadlines(category, query, "en", API_KEY);

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                // Handle API response on UI thread
                requireActivity().runOnUiThread(() -> {
                    changeInProgress(false);  // Töltősáv eltűnik
                    if (response.isSuccessful() && response.body() != null) {
                        ArticleResponse articleResponse = response.body();
                        List<Article> articles = articleResponse.getArticles();
                        if (articles != null && !articles.isEmpty()) {
                            adapter.updateData(articles);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("NewsApi", "No articles found.");
                        }
                    } else {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("NewsApi", "Failed to retrieve articles: " + errorResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                // Handle API request failure
                Log.e("NewsApi", "API request failed: " + t.getMessage());
                changeInProgress(false);  // Töltősáv eltűnik hiba esetén
            }
        });
    }

    // Handle category button clicks
    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String category = btn.getText().toString();
        getNews(category, null);
    }
}