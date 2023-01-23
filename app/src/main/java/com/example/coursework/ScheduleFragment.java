package com.example.coursework;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment {

    private Toolbar toolbar;
    private ScheduleRecyclerViewAdapter scheduleRecyclerViewAdapter;
    private ScheduleDatabase scheduleDatabase;
    private RecyclerView recyclerView;

    private ArrayList<Integer> idList;
    private ArrayList<String> locationNameList;
    private ArrayList<String> placeTypeList;
    private ArrayList<Double> latitudeList;
    private ArrayList<Double> longitudeList;
    private ArrayList<Integer> priorityList;
    private ArrayList<String> weatherPreferenceList;


    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String param1, String param2) {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        idList = new ArrayList<>();
        locationNameList = new ArrayList<>();
        placeTypeList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        priorityList = new ArrayList<>();
        weatherPreferenceList = new ArrayList<>();
        recyclerView = new RecyclerView(getContext());
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        scheduleDatabase = new ScheduleDatabase(getContext());
        recyclerView = getView().findViewById(R.id.scheduleRecyclerView);





//        ScheduleRecyclerViewAdapter.CardViewClickListener cardViewClickListener = new ScheduleRecyclerViewAdapter.CardViewClickListener() {
//            @Override
//            public void onItemClick(int id, String weatherPreference) {
//
//            }
//        };

        scheduleRecyclerViewAdapter = new ScheduleRecyclerViewAdapter(getContext(),idList, locationNameList, latitudeList, longitudeList, priorityList, weatherPreferenceList, placeTypeList);

        recyclerView.setAdapter(scheduleRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        displayDatabase("clear");

    }

    void displayDatabase(String weatherCondition){
        scheduleRecyclerViewAdapter.clearAll();
        Cursor cursor;
        cursor = scheduleDatabase.readDatabase();

        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Database is empty", Toast.LENGTH_SHORT).show();
        }
        while(cursor.moveToNext()){
            idList.add(Integer.parseInt(cursor.getString(0)));
            locationNameList.add(cursor.getString(1));
            placeTypeList.add(cursor.getString(2));
            latitudeList.add(Double.parseDouble(cursor.getString(3)));
            longitudeList.add(Double.parseDouble(cursor.getString(4)));
            longitudeList.add(Double.parseDouble(cursor.getString(4)));
        }
        scheduleRecyclerViewAdapter.notifyDataSetChanged();
    }
}