package wifi.svdew.myapplication.ui.news;

import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {
    @GET("top-headlines")
    Call<ArticleResponse> getTopHeadlines(
            @Query("category") String category,
            @Query("q") String query,
            @Query("language") String language,
            @Query("apiKey") String apiKey);
}
