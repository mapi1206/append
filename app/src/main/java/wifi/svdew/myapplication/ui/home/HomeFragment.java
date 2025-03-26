package wifi.svdew.myapplication.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import wifi.svdew.myapplication.R;
import wifi.svdew.myapplication.datenbank.DatabaseHelper;

public class HomeFragment extends Fragment {

    private LinearLayout tableContainer;
    private LinearLayout newsContainer;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        dbHelper = new DatabaseHelper(requireContext());

        tableContainer = view.findViewById(R.id.tableContainer);
        newsContainer = view.findViewById(R.id.newsContainer);

        loadTables();
        loadTopNews();

        Button moreNewsButton = view.findViewById(R.id.moreNewsButton);
        moreNewsButton.setOnClickListener(v ->
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.navigation_notifications));
    }

    private void loadTables() {
        // Itt 2 minta "mini" táblát töltünk be
        for (int t = 1; t <= 2; t++) {
            TableLayout table = new TableLayout(requireContext());
            table.setStretchAllColumns(true);
            table.setPadding(16, 8, 16, 8);

            TableRow header = new TableRow(requireContext());
            header.setBackgroundColor(Color.DKGRAY);
            header.addView(makeCell("H", true));
            header.addView(makeCell("Team", true));
            header.addView(makeCell("M", true));
            header.addView(makeCell("W", true));
            header.addView(makeCell("L", true));
            table.addView(header);

            // Itt jöhetne DB-ből is, most minta adatok
            for (int i = 0; i < 3; i++) {
                TableRow row = new TableRow(requireContext());
                row.addView(makeCell(String.valueOf(i + 1), false));
                row.addView(makeCell("Team " + (i + 1 + (t - 1) * 3), false));
                row.addView(makeCell("30", false));
                row.addView(makeCell("18", false));
                row.addView(makeCell("12", false));
                table.addView(row);
            }

            tableContainer.addView(table);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadTopNews() {
        // Itt 6 hír jelenik meg (később DB-ből lehet)
        for (int i = 1; i <= 6; i++) {
            LinearLayout newsItem = new LinearLayout(requireContext());
            newsItem.setOrientation(LinearLayout.VERTICAL);
            newsItem.setPadding(24, 16, 24, 16);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 24);
            newsItem.setLayoutParams(params);
            newsItem.setBackgroundResource(R.drawable.button_white_border); // fehér keret

            TextView title = new TextView(requireContext());
            title.setText("Hír címe " + i);
            title.setTextColor(Color.WHITE);
            title.setTextSize(16);
            title.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            newsItem.addView(title);

            TextView content = new TextView(requireContext());
            content.setText("Ez egy rövid hírleírás, csak tesztszöveg.");
            content.setTextColor(Color.LTGRAY);
            content.setTextSize(14);
            content.setPadding(0, 8, 0, 0);
            newsItem.addView(content);

            newsContainer.addView(newsItem);
        }
    }

    private TextView makeCell(String text, boolean header) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(12, 12, 12, 12);
        tv.setTextSize(header ? 16 : 14);
        tv.setBackgroundColor(header ? Color.DKGRAY : Color.TRANSPARENT);
        return tv;
    }
}