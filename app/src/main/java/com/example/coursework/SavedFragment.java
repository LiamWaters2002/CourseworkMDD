package com.example.coursework;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.databinding.FragmentSavedBinding;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    RecyclerView recyclerView;

    LocationDatabase locationDatabase;

    ArrayList<String> idList;
    ArrayList<String> locationNameList;
    ArrayList<String> latitudeList;
    ArrayList<String> longitudeList;
    ArrayList<String> timePreferenceList;
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
        timePreferenceList = new ArrayList<>();
        weatherPreferenceList = new ArrayList<>();


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void displayDatabase(){
        Cursor cursor = locationDatabase.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Database is empty", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                idList.add(cursor.getString(0));
                locationNameList.add(cursor.getString(1));
                latitudeList.add(cursor.getString(2));
                longitudeList.add(cursor.getString(3));
                timePreferenceList.add(cursor.getString(4));
                weatherPreferenceList.add(cursor.getString(5));

            }


        }
    }

}