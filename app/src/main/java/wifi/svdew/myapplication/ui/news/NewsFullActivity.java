package wifi.svdew.myapplication.ui.news;

// Activity to display a full news article in a WebView
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import wifi.svdew.myapplication.R;

public class NewsFullActivity extends AppCompatActivity {

    // WebView to load and display the news URL
    WebView webView;

    // Set up the WebView and load the URL from the intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_full_activity);

        String url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.web_view);
        WebSettings webSettings  = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    // Handle back navigation within the WebView
    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}
