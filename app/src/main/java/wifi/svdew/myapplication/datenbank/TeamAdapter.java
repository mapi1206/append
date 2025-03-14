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

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public TeamAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.team_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String teamName = cursor.getString(cursor.getColumnIndexOrThrow("team_name"));
        int wins = cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
        int losses = cursor.getInt(cursor.getColumnIndexOrThrow("losses"));
        int matches = cursor.getInt(cursor.getColumnIndexOrThrow("matches"));

        holder.teamNameTextView.setText(teamName);
        holder.winsTextView.setText("Győzelmek: " + wins);
        holder.lossesTextView.setText("Vereségek: " + losses);
        holder.matchesTextView.setText("Mérkőzések: " + matches);
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
        TextView teamNameTextView, winsTextView, lossesTextView, matchesTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamNameTextView = itemView.findViewById(R.id.teamName);
            winsTextView = itemView.findViewById(R.id.teamWins);
            lossesTextView = itemView.findViewById(R.id.teamLosses);
            matchesTextView = itemView.findViewById(R.id.teamMatches);
        }
    }
}
