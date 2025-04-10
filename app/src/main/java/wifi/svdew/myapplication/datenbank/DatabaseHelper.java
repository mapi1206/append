package wifi.svdew.myapplication.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Handles database creation, upgrade, and basic operations for Euroleague and Teams tables.
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "teams.db";
    private static final int DATABASE_VERSION = 13; // Database version
    // Table names
    private static final String TABLE_EUROLEAGUE = "euroleague";
    private static final String TABLE_TEAMS = "teams";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEAM_NAME = "team_name";
    private static final String COLUMN_WINS = "wins";
    private static final String COLUMN_LOSSES = "losses";
    private static final String COLUMN_MATCHES = "matches";
    // SQL table creation statements
    private static final String CREATE_TABLE_EUROLEAGUE = "CREATE TABLE " + TABLE_EUROLEAGUE + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEAM_NAME + " TEXT NOT NULL, "
            + COLUMN_WINS + " INTEGER DEFAULT 0, "
            + COLUMN_LOSSES + " INTEGER DEFAULT 0, "
            + COLUMN_MATCHES + " INTEGER DEFAULT 0);";
    // SQL table creation statements
    private static final String CREATE_TABLE_TEAMS = "CREATE TABLE " + TABLE_TEAMS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEAM_NAME + " TEXT NOT NULL, "
            + COLUMN_WINS + " INTEGER DEFAULT 0, "
            + COLUMN_LOSSES + " INTEGER DEFAULT 0, "
            + COLUMN_MATCHES + " INTEGER DEFAULT 0);";
    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Called when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "onCreate called!");
        db.execSQL(CREATE_TABLE_EUROLEAGUE);
        db.execSQL(CREATE_TABLE_TEAMS);

        insertTeam(db, "Real Madrid", 12, 2, 14);
        insertTeam(db, "Barcelona", 10, 5, 15);

        insertTeamToEuroleague(db, "Real Madrid", 12, 2, 14);
        insertTeamToEuroleague(db, "Barcelona", 10, 5, 15);
    }
    // Called when the database version is increased
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "onUpgrade called! OldVersion: " + oldVersion + ", NewVersion: " + newVersion);

        // Az euroleague táblák törlése és újralétrehozása
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EUROLEAGUE);
        db.execSQL(CREATE_TABLE_EUROLEAGUE);

        // Újra feltöltjük az euroleague táblát a 2024-25 szezon adataival
        insertTeamToEuroleague(db, "Olympiakos Piraeus", 22, 8, 30);
        insertTeamToEuroleague(db, "Fenerbahçe Beko Istanbul", 20, 10, 30);
        insertTeamToEuroleague(db, "Panathinaikos AKTOR Athens", 19, 11, 30);
        insertTeamToEuroleague(db, "AS Monaco", 18, 12, 30);
        insertTeamToEuroleague(db, "FC Barcelona", 17, 13, 30);
        insertTeamToEuroleague(db, "Crvena Zvezda Meridianbet Belgrade", 17, 13, 30);
        insertTeamToEuroleague(db, "Paris Basketball", 17, 13, 30);
        insertTeamToEuroleague(db, "FC Bayern München", 17, 13, 30);
        insertTeamToEuroleague(db, "Anadolu Efes Istanbul", 16, 14, 30);
        insertTeamToEuroleague(db, "Real Madrid", 16, 14, 30);
        insertTeamToEuroleague(db, "EA7 Emporio Armani Milano", 16, 14, 30);
        insertTeamToEuroleague(db, "Partizan Mozzart Bet Belgrade", 15, 15, 30);
        insertTeamToEuroleague(db, "Žalgiris Kaunas", 14, 16, 30);
        insertTeamToEuroleague(db, "Cazoo Baskonia Vitoria-Gasteiz", 13, 17, 30);
        insertTeamToEuroleague(db, "LDLC ASVEL Villeurbanne", 11, 19, 30);
        insertTeamToEuroleague(db, "Maccabi Playtika Tel Aviv", 10, 20, 30);
        insertTeamToEuroleague(db, "Virtus Segafredo Bologna", 7, 23, 30);
        insertTeamToEuroleague(db, "Alba Berlin", 5, 25, 30);

        // Újra feltöltjük a teams táblát az alapértelmezett adatokkal
        insertTeam(db, "Real Madrid", 12, 2, 14);
        insertTeam(db, "Barcelona", 10, 5, 15);
    }
    // Get all teams ordered by wins
    public Cursor getAllTeamsFromEuroleague() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EUROLEAGUE + " ORDER BY " + COLUMN_WINS + " DESC", null);
    }
    // Get all teams ordered by wins
    public Cursor getAllTeamsFromTeamsTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TEAMS + " ORDER BY " + COLUMN_WINS + " DESC", null);
    }
    // Insert a team into the Teams table
    public void insertTeam(SQLiteDatabase db, String teamName, int wins, int losses, int matches) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAM_NAME, teamName);
        values.put(COLUMN_WINS, wins);
        values.put(COLUMN_LOSSES, losses);
        values.put(COLUMN_MATCHES, matches);

        long result = db.insert(TABLE_TEAMS, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert team: " + teamName);
        } else {
            Log.d("DatabaseHelper", "Successfully inserted team: " + teamName);
        }
    }
    // Insert a team into the Euroleague table
    public void insertTeamToEuroleague(SQLiteDatabase db, String teamName, int wins, int losses, int matches) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAM_NAME, teamName);
        values.put(COLUMN_WINS, wins);
        values.put(COLUMN_LOSSES, losses);
        values.put(COLUMN_MATCHES, matches);

        long result = db.insert(TABLE_EUROLEAGUE, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert team into euroleague: " + teamName);
        } else {
            Log.d("DatabaseHelper", "Successfully inserted team into euroleague: " + teamName);
        }
    }
}