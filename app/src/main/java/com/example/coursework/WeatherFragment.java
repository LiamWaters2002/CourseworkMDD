package com.example.coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.databinding.FragmentWeatherBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;
    private FetchUrlData fetchUrlData;
    private ArrayList<String> weatherConditionsList, dateList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        weatherConditionsList = new ArrayList<>();
        dateList = new ArrayList<>();

        super.onViewCreated(view, savedInstanceState);

        WeatherFetchData weatherFetchData = new WeatherFetchData();

        //location and key
        weatherFetchData.execute("London", "feb7abc8a7msh50b4159db41f088p16428bjsn5ad7bf336048");

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(weatherFetchData.get().toString());

//            TextView txtDate = getView().findViewById(R.id.text_date); //Fix
//            TextView txtConditions = getView().findViewById(R.id.text_conditions);

            JSONObject locations = jsonObject.getJSONObject("locations");


            JSONObject london = locations.getJSONObject("London");

            JSONArray values = london.getJSONArray("values");

            for (int i = 0; i < values.length(); i++) {
                JSONObject day = values.getJSONObject(i);
                String dateTime = day.getString("datetimeStr");
                String conditions = day.getString("conditions");
//                if(){
//
//                }
                dateList.add(dateTime.toString());
                weatherConditionsList.add(conditions.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WeatherRecyclerViewAdapter.CardViewClickListener cardViewClickListener = new WeatherRecyclerViewAdapter.CardViewClickListener() {
            @Override
            public void onItemClick(Object id) {
                Fragment fragment = new InformationFragment();
                MainActivity activity = (MainActivity) getActivity();
                activity.switchFragment(fragment, Integer.parseInt(id.toString()));
            }
        };

        WeatherRecyclerViewAdapter weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(cardViewClickListener, getContext(), weatherConditionsList, dateList);

        RecyclerView weatherRecyclerView = getView().findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setAdapter(weatherRecyclerViewAdapter);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}



