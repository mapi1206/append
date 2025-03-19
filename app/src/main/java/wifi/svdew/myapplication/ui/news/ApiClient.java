package wifi.svdew.myapplication.ui.news;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Interceptor;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // OkHttpClient hozzáadása User-Agent fejléccel
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

            // Retrofit konfigurálása az OkHttpClient-tel
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")  // API alap URL
                    .client(client)  // A kliens hozzáadása a Retrofit-hez
                    .addConverterFactory(GsonConverterFactory.create()) // Gson konverter
                    .build();
        }
        return retrofit;
    }
}