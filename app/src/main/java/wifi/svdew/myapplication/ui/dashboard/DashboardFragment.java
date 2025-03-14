package wifi.svdew.myapplication.ui.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import wifi.svdew.myapplication.R;
import wifi.svdew.myapplication.databinding.FragmentDashboardBinding;
import wifi.svdew.myapplication.datenbank.DatabaseHelper;
import wifi.svdew.myapplication.datenbank.TeamAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private RecyclerView recyclerView;
    private TeamAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.teamRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true); // Optimization for better performance.

        databaseHelper = new DatabaseHelper(getContext());

        // Insert default data if the table is empty
        if (databaseHelper.getAllTeams().getCount() == 0) {
            databaseHelper.addTeam("FALCO-VULCANO ENERGIA KC SZOMBATHELY", 10, 2);
            databaseHelper.addTeam("ATOMERŐMŰ SE", 8, 4);
            databaseHelper.addTeam("NHSZ-SZOLNOKI OLAJBÁNYÁSZ", 6, 6);
            databaseHelper.addTeam("ZALAKERÁMIA ZTE KK", 11, 9);
            databaseHelper.addTeam("DEAC", 11, 9);
            databaseHelper.addTeam("Egis Körmend", 11, 9);
            databaseHelper.addTeam("Alba Fehérvár", 11, 9);
            databaseHelper.addTeam("NKA Universitas Pécs", 11, 9);
            databaseHelper.addTeam("Sopron KC", 11, 9);
            databaseHelper.addTeam("Endo Plus Service Honvéd", 11, 9);
            databaseHelper.addTeam("MVM-OSE LIONS", 11, 9);
            databaseHelper.addTeam("SZTE-SZEDEÁK", 11, 9);
            databaseHelper.addTeam("DUNA ASZFALT-DTKH KECSKEMÉT", 11, 9);
            databaseHelper.addTeam("PVSK-VEOLIA", 11, 9);
        }

        Cursor cursor = databaseHelper.getAllTeams();
        if (cursor != null && cursor.getCount() > 0) {
            adapter = new TeamAdapter(getContext(), cursor);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged(); // Notify adapter about data changes
        } else {
            Log.d("DASHBOARD", "No data found in the database.");
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
