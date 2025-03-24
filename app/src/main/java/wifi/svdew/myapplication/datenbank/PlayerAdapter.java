package wifi.svdew.myapplication.datenbank;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import wifi.svdew.myapplication.R;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public PlayerAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.player_item, parent, false);
        return new ViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void updateCursor(Cursor newCursor) {
        if (newCursor != null) {
            this.cursor = newCursor;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView, playerAgeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerName);
            playerAgeTextView = itemView.findViewById(R.id.playerAge);
        }
    }
}
