package wifi.svdew.myapplication.ui.tabella;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import wifi.svdew.myapplication.R;
import wifi.svdew.myapplication.databinding.FragmentTabellaBinding;
import wifi.svdew.myapplication.datenbank.DatabaseHelper;

public class TabellaFragment extends Fragment {

    private FragmentTabellaBinding binding;
    private TableLayout tableLayout1, tableLayout2;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTabellaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Button btnShowTable1 = binding.btn8;
        Button btnShowTable2 = binding.btn9;

        tableLayout1 = view.findViewById(R.id.teamTableLayout1);
        tableLayout2 = view.findViewById(R.id.teamTableLayout2);
        databaseHelper = new DatabaseHelper(getContext());

        // Gombok eseménykezelői, teszt Log.d-vel
        btnShowTable1.setOnClickListener(v -> {
            Log.d("BUTTON_CLICK", "btn8 (MB1) clicked");
            showTable(1); // Az első táblázatot mutatjuk
        });

        btnShowTable2.setOnClickListener(v -> {
            Log.d("BUTTON_CLICK", "btn9 (Euroleague) clicked");
            showTable(2); // A második táblázatot mutatjuk
        });

        // Alapértelmezett táblázat megjelenítése
        showTable(1);

        // Adatok betöltése mindkét táblához
        loadTeamsTable(tableLayout1);         // This loads 'teams' table
        loadEuroleagueTable(tableLayout2);    // This loads 'euroleague' table

        return view;
    }

    private void showTable(int tableNumber) {
        if (tableNumber == 1) {
            tableLayout1.setVisibility(View.VISIBLE);
            tableLayout2.setVisibility(View.GONE);
        } else {
            tableLayout1.setVisibility(View.GONE);
            tableLayout2.setVisibility(View.VISIBLE);
        }
    }

    private void loadTeamsTable(TableLayout tableLayout) {
        Cursor cursor = databaseHelper.getAllTeamsFromTeamsTable();
        loadCursorIntoTable(tableLayout, cursor);
    }

    private void loadEuroleagueTable(TableLayout tableLayout) {
        Cursor cursor = databaseHelper.getAllTeamsFromEuroleague();
        loadCursorIntoTable(tableLayout, cursor);
    }

    private void loadCursorIntoTable(TableLayout tableLayout, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            int index = 1;
            while (cursor.moveToNext()) {
                TableRow tableRow = new TableRow(getContext());

                tableRow.addView(createCell(String.valueOf(index)));
                tableRow.addView(createCell(cursor.getString(cursor.getColumnIndexOrThrow("team_name"))));
                tableRow.addView(createCell(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("matches")))));
                tableRow.addView(createCell(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("wins")))));
                tableRow.addView(createCell(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("losses")))));

                tableLayout.addView(tableRow);
                index++;
            }
            cursor.close();
        } else {
            Log.d("DASHBOARD", "No data found in the database.");
        }
    }

    private TextView createCell(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(12, 12, 12, 12);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setBackgroundResource(R.drawable.table_border);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
        return textView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}