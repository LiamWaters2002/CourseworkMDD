package com.example.coursework;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.databinding.FragmentSavedBinding;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    RecyclerView recyclerView;
    SavedRecyclerViewAdapter savedRecyclerViewAdapter;

    LinearLayout btnSavedAttractions;
    LinearLayout btnSavedRestaraunts;
    LinearLayout btnSavedBars;
    LinearLayout btnSavedNightClubs;

    LocationDatabase locationDatabase;

    ArrayList<Integer> idList;
    ArrayList<String> locationNameList;
    ArrayList<Double> latitudeList;
    ArrayList<Double> longitudeList;
    ArrayList<Integer> priorityList;
    ArrayList<String> weatherPreferenceList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);

        locationDatabase = new LocationDatabase(getContext());

        idList = new ArrayList<>();
        locationNameList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        priorityList = new ArrayList<>();
        weatherPreferenceList = new ArrayList<>();

        recyclerView = binding.recyclerViewDisplay;

//        btnSavedAttractions = getView().findViewById(R.id.btnSavedAttractions);
//        btnSavedRestaraunts = getView().findViewById(R.id.btnSavedRestaurants);
//        btnSavedBars = getView().findViewById(R.id.btnSavedBars);
//        btnSavedNightClubs = getView().findViewById(R.id.btnSavedNightClubs);

//        btnAttractions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterType = 1; // Type 1
//                updateRecyclerView();//3rd item in cgpte
//            }
//        });

        displayDatabase();

        SavedRecyclerViewAdapter.CardViewClickListener cardViewClickListener = new SavedRecyclerViewAdapter.CardViewClickListener() {
            @Override
            public void onItemClick(Object id) {
                Fragment fragment = new InformationFragment();
                MainActivity activity = (MainActivity) getActivity();
                activity.switchFragment(fragment, Integer.parseInt(id.toString()));
            }
        };

        savedRecyclerViewAdapter = new SavedRecyclerViewAdapter(cardViewClickListener, getContext(), idList, locationNameList, latitudeList, longitudeList, priorityList, weatherPreferenceList);



        recyclerView.setAdapter(savedRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void displayDatabase(){
        Cursor cursor = locationDatabase.readDatabase();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Database is empty", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                idList.add(Integer.parseInt(cursor.getString(0)));
                locationNameList.add(cursor.getString(1));
                latitudeList.add(Double.parseDouble(cursor.getString(2)));
                longitudeList.add(Double.parseDouble(cursor.getString(3)));
                priorityList.add(Integer.parseInt(cursor.getString(4)));
                weatherPreferenceList.add(cursor.getString(5));
            }
        }
    }

}