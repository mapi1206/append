package wifi.svdew.myapplication.datenbank;

// Adapter for binding player data from a Cursor to a RecyclerView.

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import wifi.svdew.myapplication.R;

// RecyclerView Adapter class for player data
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    // Context for inflating layouts and Cursor to hold player data
    private Context context;
    private Cursor cursor;

    // Constructor initializes context and cursor
    public PlayerAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    // Inflates the layout for a single player item and returns a ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.player_item, parent, false);
        return new ViewHolder(view);
    }

    // Binds player name and age from the cursor to the TextViews
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String playerName = cursor.getString(cursor.getColumnIndexOrThrow("player_name"));
        int playerAge = cursor.getInt(cursor.getColumnIndexOrThrow("player_age"));

        holder.playerNameTextView.setText(playerName);
        holder.playerAgeTextView.setText("Age: " + playerAge);
    }

    // Returns the total number of items in the cursor
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    // Updates the cursor and refreshes the RecyclerView
    public void updateCursor(Cursor newCursor) {
        if (newCursor != null) {
            this.cursor = newCursor;
            notifyDataSetChanged();
        }
    }

    // ViewHolder for holding references to player name and age views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView, playerAgeTextView;

        // Initialize TextView references for player name and age
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerName);
            playerAgeTextView = itemView.findViewById(R.id.playerAge);
        }
    }
}
