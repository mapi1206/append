package wifi.svdew.myapplication.ui.news;


import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import wifi.svdew.myapplication.R;

public class NewsFullActivity extends AppCompatActivity {

        WebView webView;

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

        @Override
        public void onBackPressed() {
            if(webView.canGoBack())
                webView.goBack();
            else
                super.onBackPressed();
        }
    }

