package wifi.svdew.myapplication.ui.tabella;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import wifi.svdew.myapplication.R;
import wifi.svdew.myapplication.databinding.FragmentTabellaBinding;
import wifi.svdew.myapplication.datenbank.DatabaseHelper;

public class TabellaFragment extends Fragment {

    private FragmentTabellaBinding binding;
    private TableLayout tableLayout;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTabellaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Button btn8 = binding.btn8;
        Button btn9 = binding.btn9;

        btn8.setOnClickListener(v -> {
            // A btn8 gombra kattintva történő művelet
            Log.d("TabellaFragment", "Button 8 clicked");

        });

        btn9.setOnClickListener(v -> {
            // A btn9 gombra kattintva történő művelet
            Log.d("TabellaFragment", "Button 9 clicked");
        });

        tableLayout = view.findViewById(R.id.teamTableLayout);
        databaseHelper = new DatabaseHelper(getContext());

        loadTableData();
        return view;
    }

    private void loadTableData() {
        Cursor cursor = databaseHelper.getAllTeams();

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
