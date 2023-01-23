package com.example.coursework;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.databinding.FragmentSavedBinding;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    private RecyclerView recyclerView;
    private SavedRecyclerViewAdapter savedRecyclerViewAdapter;

    //Place Type
    private LinearLayout btnAttractions;
    private LinearLayout btnRestaurants;
    private LinearLayout btnBars;
    private LinearLayout btnNightClubs;

    private LocationDatabase locationDatabase;

    private ArrayList<Integer> idList;
    private ArrayList<String> locationNameList;
    private ArrayList<String> placeTypeList;
    private ArrayList<Double> latitudeList;
    private ArrayList<Double> longitudeList;
    private ArrayList<Integer> priorityList;
    private ArrayList<String> weatherPreferenceList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);

        locationDatabase = new LocationDatabase(getContext());

        idList = new ArrayList<>();
        locationNameList = new ArrayList<>();
        placeTypeList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        priorityList = new ArrayList<>();
        weatherPreferenceList = new ArrayList<>();

        recyclerView = binding.recyclerView;


        SavedRecyclerViewAdapter.CardViewClickListener cardViewClickListener = new SavedRecyclerViewAdapter.CardViewClickListener() {
            @Override
            public void onItemClick(int id) {
                Fragment fragment = new InformationFragment();
                MainActivity activity = (MainActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("id", id); //Store object id into bundle.
                activity.switchFragment(fragment, bundle);
            }
        };

        savedRecyclerViewAdapter = new SavedRecyclerViewAdapter(cardViewClickListener, getContext(), idList, locationNameList, latitudeList, longitudeList, priorityList, weatherPreferenceList, placeTypeList);



        recyclerView.setAdapter(savedRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        btnAttractions = getView().findViewById(R.id.btnAttractions);
        btnRestaurants = getView().findViewById(R.id.btnRestaurants);
        btnBars = getView().findViewById(R.id.btnBars);
        btnNightClubs = getView().findViewById(R.id.btnNightClubs);

        btnAttractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatabase("tourist_attraction");
            }
        });

        btnRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatabase("restaurant");
            }
        });

        btnBars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatabase("bar");
            }
        });

        btnNightClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatabase("night_club");
            }
        });

        displayDatabase("all");
    }

    void displayDatabase(String placeType){
        savedRecyclerViewAdapter.clearAll();
        Cursor cursor;
        if(placeType.equals("all")){
            cursor = locationDatabase.readDatabase();
        }else{
            cursor = locationDatabase.readDatabase(placeType);
        }

        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Database is empty", Toast.LENGTH_SHORT).show();
        }
        while(cursor.moveToNext()){
            if(placeType.equals("all") || cursor.getString(2).equals(placeType)){
                idList.add(Integer.parseInt(cursor.getString(0)));
                locationNameList.add(cursor.getString(1));
                placeTypeList.add(cursor.getString(2));
                latitudeList.add(Double.parseDouble(cursor.getString(3)));
                longitudeList.add(Double.parseDouble(cursor.getString(4)));
                priorityList.add(Integer.parseInt(cursor.getString(5)));
                weatherPreferenceList.add(cursor.getString(6));
            }
        }
        savedRecyclerViewAdapter.notifyDataSetChanged();
    }

}