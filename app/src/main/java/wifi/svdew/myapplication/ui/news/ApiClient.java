// ApiClient sets up a Retrofit instance with a custom OkHttp client and User-Agent header
package wifi.svdew.myapplication.ui.news;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Interceptor;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {
    // Singleton Retrofit instance
    private static Retrofit retrofit = null;

    // Returns the Retrofit instance, initializing it if necessary
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Configure OkHttpClient with an interceptor to add a custom User-Agent header
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            // Configure Retrofit with base URL, custom client, and Gson converter
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")  // API alap URL
                    .client(client)  // A kliens hozzáadása a Retrofit-hez
                    .addConverterFactory(GsonConverterFactory.create()) // Gson konverter
                    .build();
        }
        return retrofit;
    }
}