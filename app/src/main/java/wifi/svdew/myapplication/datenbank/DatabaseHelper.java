package wifi.svdew.myapplication.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "teams.db";
    private static final int DATABASE_VERSION = 9; // Frissített verziószám

    private static final String TABLE_EUROLEAGUE = "euroleague"; // Euroleague tábla neve
    private static final String TABLE_TEAMS = "teams"; // Teams tábla neve
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEAM_NAME = "team_name";
    private static final String COLUMN_WINS = "wins";
    private static final String COLUMN_LOSSES = "losses";
    private static final String COLUMN_MATCHES = "matches";

    // SQL parancsok a táblák létrehozásához
    private static final String CREATE_TABLE_EUROLEAGUE = "CREATE TABLE " + TABLE_EUROLEAGUE + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEAM_NAME + " TEXT NOT NULL, "
            + COLUMN_WINS + " INTEGER DEFAULT 0, "
            + COLUMN_LOSSES + " INTEGER DEFAULT 0, "
            + COLUMN_MATCHES + " INTEGER DEFAULT 0);";

    private static final String CREATE_TABLE_TEAMS = "CREATE TABLE " + TABLE_TEAMS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEAM_NAME + " TEXT NOT NULL, "
            + COLUMN_WINS + " INTEGER DEFAULT 0, "
            + COLUMN_LOSSES + " INTEGER DEFAULT 0, "
            + COLUMN_MATCHES + " INTEGER DEFAULT 0);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EUROLEAGUE);
        db.execSQL(CREATE_TABLE_TEAMS);  // Create teams table

        // Manually insert the first team "Real Madrid" with 12 wins, 2 losses, and 14 matches
        insertTeam(db, "Real Madrid", 12, 2, 14); // Insert into teams table

        // Manually insert the second team "Barcelona" with 10 wins, 5 losses, and 15 matches into teams table
        insertTeam(db, "Barcelona", 10, 5, 15); // Insert into teams table

        // Insert teams into the euroleague table
        insertTeamToEuroleague(db, "Real Madrid", 12, 2, 14);  // Insert into euroleague table
        insertTeamToEuroleague(db, "Barcelona", 10, 5, 15);   // Insert into euroleague table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 9) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EUROLEAGUE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TEAM_NAME + " TEXT NOT NULL, "
                + COLUMN_WINS + " INTEGER DEFAULT 0, "
                + COLUMN_LOSSES + " INTEGER DEFAULT 0, "
                + COLUMN_MATCHES + " INTEGER DEFAULT 0);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TEAMS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TEAM_NAME + " TEXT NOT NULL, "
                + COLUMN_WINS + " INTEGER DEFAULT 0, "
                + COLUMN_LOSSES + " INTEGER DEFAULT 0, "
                + COLUMN_MATCHES + " INTEGER DEFAULT 0);");
        }
    }

    // Method to get all teams from the euroleague
    public Cursor getAllTeamsFromEuroleague() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EUROLEAGUE + " ORDER BY " + COLUMN_WINS + " DESC", null);
    }

    // Method to get all teams from the teams table
    public Cursor getAllTeamsFromTeamsTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TEAMS + " ORDER BY " + COLUMN_WINS + " DESC", null);
    }

    // Method to insert a team into the teams table
    public void insertTeam(SQLiteDatabase db, String teamName, int wins, int losses, int matches) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAM_NAME, teamName);
        values.put(COLUMN_WINS, wins);
        values.put(COLUMN_LOSSES, losses);
        values.put(COLUMN_MATCHES, matches);

        long result = db.insert(TABLE_TEAMS, null, values); // Insert the team into the teams table

        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert team: " + teamName);
        } else {
            Log.d("DatabaseHelper", "Successfully inserted team: " + teamName);
        }
    }

    // Method to insert a team into the euroleague table
    public void insertTeamToEuroleague(SQLiteDatabase db, String teamName, int wins, int losses, int matches) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAM_NAME, teamName);
        values.put(COLUMN_WINS, wins);
        values.put(COLUMN_LOSSES, losses);
        values.put(COLUMN_MATCHES, matches);

        long result = db.insert(TABLE_EUROLEAGUE, null, values); // Insert the team into euroleague table

        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert team into euroleague: " + teamName);
        } else {
            Log.d("DatabaseHelper", "Successfully inserted team into euroleague: " + teamName);
        }
    }
}