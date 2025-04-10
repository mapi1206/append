package wifi.svdew.myapplication.ui.news;

// SplashActivity displays a splash screen and then loads the NewsFragment after a delay

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentTransaction;
import wifi.svdew.myapplication.R;

public class SplashActivity extends AppCompatActivity {

    // Set content view and start delayed transition to NewsFragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Begin fragment transaction to load the NewsFragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                NewsFragment fragment = new NewsFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        }, 3000);
    }
}