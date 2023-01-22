package com.example.coursework;

import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SuggestFragment extends Fragment {

    private Toolbar toolbar;
    private SuggestRecyclerViewAdapter suggestRecyclerViewAdapter;
    private LocationDatabase locationDatabase;
    private ScheduleDatabase scheduleDatabase;
    private RecyclerView recyclerView;

    private ArrayList<Integer> idList;
    private ArrayList<String> locationNameList;
    private ArrayList<String> placeTypeList;
    private ArrayList<Double> latitudeList;
    private ArrayList<Double> longitudeList;
    private ArrayList<Integer> priorityList;
    private ArrayList<String> weatherPreferenceList;


    public SuggestFragment() {
        // Required empty public constructor
    }

    public static SuggestFragment newInstance(String param1, String param2) {
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
        return inflater.inflate(R.layout.fragment_suggest, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        scheduleDatabase = new ScheduleDatabase(getContext());
        locationDatabase = new LocationDatabase(getContext());
        recyclerView = getView().findViewById(R.id.suggestRecyclerView);

        toolbar = getView().findViewById(R.id.suggestToolbar);
        Bundle bundle = this.getArguments();
        String time = bundle.get("time").toString();
        String weatherPreference = bundle.get("weatherPreference").toString();
        toolbar.setTitle("Scheduler > " + time);
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack("weather", 1);

            }
        });

        SuggestRecyclerViewAdapter.CardViewClickListener cardViewClickListener = new SuggestRecyclerViewAdapter.CardViewClickListener() {
            @Override
            public void onItemClick(int itemId) {
                int id = itemId - 1;
                scheduleDatabase.addLocation(locationNameList.get(id), latitudeList.get(id), longitudeList.get(id), time, placeTypeList.get(id));
                Toast.makeText(getContext(),"added to database", Toast.LENGTH_SHORT).show();
            }
        };

        suggestRecyclerViewAdapter = new SuggestRecyclerViewAdapter(cardViewClickListener, getContext(),idList, locationNameList, latitudeList, longitudeList, priorityList, weatherPreferenceList, placeTypeList);

        recyclerView.setAdapter(suggestRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        displayDatabase("all", weatherPreference);

    }

    void displayDatabase(String placeType, String weatherPreference){
        suggestRecyclerViewAdapter.clearAll();
        Cursor cursor;

        WeatherTypes weatherTypes = new WeatherTypes();

        Toast.makeText(getContext(), weatherPreference, Toast.LENGTH_SHORT).show();
        cursor = locationDatabase.readDatabaseByWeather(weatherPreference);
//            cursor = locationDatabase.readDatabaseByWeatherAndType(placeType, weatherPreference);




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
        suggestRecyclerViewAdapter.notifyDataSetChanged();
    }
}