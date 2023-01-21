package com.example.coursework;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class InformationFragment extends Fragment {


    private TextView txtLocationId;
    private TextView txtLocationName;
    private TextView txtPlaceType;
    private TextView txtPriority;
    private TextView txtWeatherPreference;


    private Toolbar toolbar;
    private Button btnEdit;
    private Button btnDelete;
    private LocationDatabase locationDatabase;

    private int id, priority;
    private String placeType, weatherPreference, locationName;
    private Double latitude, longitude;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationDatabase = new LocationDatabase(getContext());
        Bundle bundle = this.getArguments();
        id = bundle.getInt("id");
        Cursor cursor = locationDatabase.fetchRow(id);

        cursor.moveToNext();
        id = Integer.parseInt(cursor.getString(0));
        locationName = cursor.getString(1);
        placeType = cursor.getString(2);
        latitude = Double.parseDouble(cursor.getString(3));
        longitude = Double.parseDouble(cursor.getString(4));
        priority = Integer.parseInt(cursor.getString(5));
        weatherPreference = cursor.getString(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        txtLocationId = getView().findViewById(R.id.locationId);
        txtLocationName  = getView().findViewById(R.id.locationName);
        txtPlaceType  = getView().findViewById(R.id.placeType);
        txtPriority  = getView().findViewById(R.id.priority);
        txtWeatherPreference  = getView().findViewById(R.id.weatherPreference);

        txtLocationId.setText(Integer.toString(id));
        txtLocationName.setText(locationName);

        if(txtLocationName.equals("tourist_attraction")){
            txtPlaceType.setText("Tourist Attraction");
        }
        else if(txtLocationName.equals("bar")){
            txtPlaceType.setText("Bar");
        }
        else if(txtLocationName.equals("night_club")){
            txtPlaceType.setText("Night Club");
        }
        else if(txtLocationName.equals("restaurant")){
            txtPlaceType.setText("Restaurant");
        }
        txtPlaceType.setText(placeType);

        txtWeatherPreference.setText(weatherPreference);
        txtPriority.setText(Integer.toString(priority));

        btnEdit = getView().findViewById(R.id.btnEdit);
        btnDelete = getView().findViewById(R.id.btnDelete);

        btnEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                locationDatabase.editRow(id, priority, weatherPreference);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                locationDatabase.deleteRow(id);
                MainActivity activity = (MainActivity) getActivity();
                activity.switchFragment(new SavedFragment(), 0);
            }
        });



        btnDelete = getView().findViewById(R.id.btnDelete);


        toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("Saved > " + locationName);
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

    }
}