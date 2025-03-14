package wifi.svdew.myapplication.datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "teams.db";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_TEAMS = "teams";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEAM_NAME = "team_name";
    private static final String COLUMN_WINS = "wins";
    private static final String COLUMN_LOSSES = "losses";
    private static final String COLUMN_MATCHES = "matches";

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
        db.execSQL(CREATE_TABLE_TEAMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            db.execSQL("ALTER TABLE " + TABLE_TEAMS + " ADD COLUMN " + COLUMN_MATCHES + " INTEGER DEFAULT 0;");
            db.execSQL("UPDATE " + TABLE_TEAMS + " SET " + COLUMN_MATCHES + " = " + COLUMN_WINS + " + " + COLUMN_LOSSES + ";");
        }
    }

    public boolean addTeam(String teamName, int wins, int losses) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEAMS + " WHERE " + COLUMN_TEAM_NAME + "=?", new String[]{teamName});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int currentWins = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WINS));
            int currentLosses = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOSSES));
            int newWins = currentWins + wins;
            int newLosses = currentLosses + losses;
            int newMatches = newWins + newLosses;

            ContentValues updateValues = new ContentValues();
            updateValues.put(COLUMN_WINS, newWins);
            updateValues.put(COLUMN_LOSSES, newLosses);
            updateValues.put(COLUMN_MATCHES, newMatches);

            int rowsUpdated = db.update(TABLE_TEAMS, updateValues, COLUMN_TEAM_NAME + "=?", new String[]{teamName});
            cursor.close();
            db.close();
            return rowsUpdated > 0;
        }
        cursor.close();

        int matches = wins + losses;

        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAM_NAME, teamName);
        values.put(COLUMN_WINS, wins);
        values.put(COLUMN_LOSSES, losses);
        values.put(COLUMN_MATCHES, matches); // Store matches

        long result = db.insert(TABLE_TEAMS, null, values);

        if (result == -1) {
            Log.e("DatabaseHelper", "Sikertelen beszúrás: " + teamName);
        } else {
            Log.d("DatabaseHelper", "Csapat sikeresen beszúrva: " + teamName + " (" + wins + " győzelem, " + losses + " vereség, " + matches + " mérkőzés)");
        }

        db.close();
        return result != -1;
    }

    public Cursor getAllTeams() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TEAMS + " ORDER BY " + COLUMN_WINS + " DESC", null);
    }

    public Cursor getAllTeamsSortedByWins() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_TEAM_NAME + ", " + COLUMN_WINS + ", " + COLUMN_LOSSES + ", " + COLUMN_MATCHES +
                           " FROM " + TABLE_TEAMS + " ORDER BY " + COLUMN_WINS + " DESC", null);
    }

    public void printTeamsSortedByWins() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = getAllTeamsSortedByWins();

        if (cursor.getCount() == 0) {
            Log.e("DatabaseHelper", "A tábla üres!");
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEAM_NAME));
                int wins = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WINS));
                int losses = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOSSES));
                int matches = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MATCHES));

                Log.d("Database Debug", "Lekérdezett csapat: " + name + ", Győzelmek: " + wins + ", Vereségek: " + losses + ", Mérkőzések: " + matches);
            }
        }
        cursor.close();
        db.close();
    }

    public void debugDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_TEAM_NAME + ", " + COLUMN_WINS + ", " + COLUMN_LOSSES + ", " + COLUMN_MATCHES +
                                    " FROM " + TABLE_TEAMS, null);

        if (cursor.getCount() == 0) {
            Log.e("DatabaseHelper", "Az adatbázis üres! Lehet, hogy nincs adat beszúrva.");
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEAM_NAME));
                int wins = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WINS));
                int losses = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOSSES));
                int matches = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MATCHES));

                Log.d("DatabaseHelper", "ID: " + id + ", Név: " + name + ", Győzelmek: " + wins + ", Vereségek: " + losses + ", Mérkőzések: " + matches);
            }
        }
        cursor.close();
        db.close();
    }
}