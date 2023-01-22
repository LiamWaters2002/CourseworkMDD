package com.example.coursework;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.databinding.FragmentWeatherBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
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


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            LocalDate today = LocalDate.now().plusDays(1);


            for (int i = 0; i < values.length(); i++) {
                JSONObject day = values.getJSONObject(i);
                String dateTime = day.getString("datetimeStr");
                String conditions = day.getString("conditions");

                LocalDate localDateTime = LocalDate.parse(dateTime, formatter);

                if(localDateTime.isEqual(today)){
                    dateList.add(dateTime.toString());
                    weatherConditionsList.add(conditions.toString());
                }

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
                Fragment fragment = new SuggestFragment();
                MainActivity activity = (MainActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("id", 0);
                activity.switchFragment(fragment, bundle);
            }
        };

        WeatherRecyclerViewAdapter weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(cardViewClickListener, getContext(), weatherConditionsList, dateList);

        RecyclerView weatherRecyclerView = getView().findViewById(R.id.recyclerView);
        weatherRecyclerView.setAdapter(weatherRecyclerViewAdapter);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}



