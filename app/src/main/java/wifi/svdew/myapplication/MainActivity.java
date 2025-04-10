package wifi.svdew.myapplication;

// MainActivity sets up navigation components, database helper, and enforces dark mode
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import wifi.svdew.myapplication.databinding.ActivityMainBinding;
import wifi.svdew.myapplication.datenbank.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    // View binding for the activity layout
    private ActivityMainBinding binding;

    // Helper class for SQLite database operations
    private DatabaseHelper databaseHelper;

    // Called when the activity is starting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force dark mode if current mode is not night mode
        int currentNightMode = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // Set up view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Configure AppBar with top-level destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.tabella, R.id.navigation_notifications)
                .build();

        // Get NavHostFragment and NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = null;
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Set up navigation UI components if NavController is available
        if (navController != null) {
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        } else {
            // Throw error if NavController is missing
            throw new IllegalStateException("NavController not found. Check the nav_host_fragment_activity_main ID.");
        }

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);
    }
}