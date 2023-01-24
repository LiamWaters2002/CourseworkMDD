package com.example.coursework;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                // Make use of the location object sent in the onLocationChanged(Location) callback.
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        String cityName = addresses.get(0).getLocality();
                        //Do what you want with cityName
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
// Register the listener with the Location Manager to receive location updates
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);






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

            WeatherTypes weatherTypes = new WeatherTypes();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            LocalDate today = LocalDate.now();


            for (int i = 0; i < values.length(); i++) {
                JSONObject day = values.getJSONObject(i);
                String dateTime = day.getString("datetimeStr");
                String conditions = day.getString("conditions");

                LocalDate localDateTime = LocalDate.parse(dateTime, formatter);

                if(localDateTime.isEqual(today)){
                    dateList.add(dateTime.toString());
                    String weatherPreference = conditions.toString();

                    if(weatherTypes.isItClearWeatherType(weatherPreference)){
                        weatherConditionsList.add("clear");
                    }
                    else if(weatherTypes.isItCloudWeatherType(weatherPreference)){
                        weatherConditionsList.add("cloud");
                    }
                    else{
                        weatherConditionsList.add("rain");
                    }
                }
                else{
                    break;
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
            public void onItemClick(String dateTime, String weatherPreference) {
                Fragment fragment = new SuggestFragment();
                MainActivity activity = (MainActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putString("time", dateTime);
                bundle.putString("weatherPreference", weatherPreference);
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


