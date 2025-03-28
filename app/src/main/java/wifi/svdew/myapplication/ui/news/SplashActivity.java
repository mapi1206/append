package wifi.svdew.myapplication.ui.news;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentTransaction;
import wifi.svdew.myapplication.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                NewsFragment fragment = new NewsFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        }, 3000);
    }
}