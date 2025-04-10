package wifi.svdew.myapplication.ui.news;

// Interface defining the structure of the News API call using Retrofit

import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Retrofit interface for fetching top news headlines
public interface NewsApiInterface {

    // GET request for top headlines based on category, query, and language
    @GET("top-headlines")
    Call<ArticleResponse> getTopHeadlines(
            @Query("category") String category,
            @Query("q") String query,
            @Query("language") String language,
            @Query("apiKey") String apiKey);
}
