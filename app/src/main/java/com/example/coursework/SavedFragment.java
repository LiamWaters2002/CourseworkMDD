package com.example.coursework;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.databinding.FragmentSavedBinding;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    RecyclerView recyclerView;
    SavedRecyclerViewAdapter savedRecyclerViewAdapter;

    LinearLayout btnAttractions;
    LinearLayout btnRestaraunts;
    LinearLayout btnBars;
    LinearLayout btnNightClubs;

    LocationDatabase locationDatabase;

    ArrayList<Integer> idList;
    ArrayList<String> locationNameList;
    ArrayList<String> placeTypeList;
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
        placeTypeList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        priorityList = new ArrayList<>();
        weatherPreferenceList = new ArrayList<>();

        recyclerView = binding.recyclerViewDisplay;

        displayDatabase();

        SavedRecyclerViewAdapter.CardViewClickListener cardViewClickListener = new SavedRecyclerViewAdapter.CardViewClickListener() {
            @Override
            public void onItemClick(Object id) {
                Fragment fragment = new InformationFragment();
                MainActivity activity = (MainActivity) getActivity();
                activity.switchFragment(fragment, Integer.parseInt(id.toString()));
            }
        };

        savedRecyclerViewAdapter = new SavedRecyclerViewAdapter(cardViewClickListener, getContext(), idList, locationNameList, latitudeList, longitudeList, priorityList, weatherPreferenceList, placeTypeList);



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

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        btnAttractions = getView().findViewById(R.id.btnAttractions);
        btnRestaraunts = getView().findViewById(R.id.btnRestaurants);
        btnBars = getView().findViewById(R.id.btnBars);
        btnNightClubs = getView().findViewById(R.id.btnNightClubs);

        btnAttractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<CardView> filteredList = filterByType(savedRecyclerViewAdapter.getData(), type);
//                savedRecyclerViewAdapter.setData(filteredList);
                savedRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    void displayDatabase(){
        Cursor cursor = locationDatabase.readDatabase();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Database is empty", Toast.LENGTH_SHORT).show();
        }
        while(cursor.moveToNext()){
            if(true){//...........................................................................................................Filter here...
                idList.add(Integer.parseInt(cursor.getString(0)));
                locationNameList.add(cursor.getString(1));
                placeTypeList.add(cursor.getString(2));
                latitudeList.add(Double.parseDouble(cursor.getString(3)));
                longitudeList.add(Double.parseDouble(cursor.getString(4)));
                priorityList.add(Integer.parseInt(cursor.getString(5)));
                weatherPreferenceList.add(cursor.getString(6));
            }

        }
    }

}