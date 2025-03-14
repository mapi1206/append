package wifi.svdew.myapplication.ui.news;

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

import java.util.ArrayList;
import java.util.List;

import wifi.svdew.myapplication.databinding.FragmentNewsBinding;

public class NewsFragment extends Fragment implements View.OnClickListener {

    private FragmentNewsBinding binding;
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecycleAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
    androidx.appcompat.widget.SearchView searchView;

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

        // SearchView beállítása
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setupRecyclerView();
        getNews("GENERAL", null);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecycleAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    void changeInProgress(boolean show) {
        if (show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    void getNews(String category, String query) {
        changeInProgress(true);  // Töltősáv megjelenítése
        NewsApiClient newsApiClient = new NewsApiClient("a8d52530b011b567636aefa7c18437a8"); // Fixing typo in client initialization
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category)
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        requireActivity().runOnUiThread(() -> {
                            changeInProgress(false);  // Töltősáv eltűnik
                            Log.d("NewsApi", "Received articles: " + response.getArticles());
                            if (response.getArticles() != null && !response.getArticles().isEmpty()) {
                                adapter.updateData(response.getArticles());
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("NewsApi", "No articles found.");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("NewsApi", "API request failed: " + throwable.getMessage());
                        Log.e("NewsApi", "Error details: " + throwable.getLocalizedMessage());
                        changeInProgress(false);  // Töltősáv eltűnik hiba esetén
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String category = btn.getText().toString();
        getNews(category, null);
    }
}