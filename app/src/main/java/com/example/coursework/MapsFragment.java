package com.example.coursework;





import static android.app.Activity.RESULT_OK;
import static com.example.coursework.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment {

    private EditText txtSearch;
    private Button btnCurrentUserLocation;
    private boolean clickedViewCurrentLocation;//Use this for button click

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int APPROVED_REQUEST_CODE = 1000;

    private LocationRequest locationRequest;

    private GoogleMap googleMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * When map is ready, start displaying user's location.
         * @param googleMap
         */
        @Override
        public void onMapReady(GoogleMap googleMap){
            displayUserCurrentLocation(googleMap);
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Places.initialize(getContext(), MAPS_API_KEY);

        clickedViewCurrentLocation = false;

        btnCurrentUserLocation = getView().findViewById(R.id.current_user_location);
        btnCurrentUserLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedViewCurrentLocation();
            }
        });

        txtSearch = (EditText) getView().findViewById(R.id.txtSearch);
        txtSearch.setFocusable(false);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> placeList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeList).build(getActivity());

                startActivityForResult(intent, 100);
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
       if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        setupLocationServices();
    }

    /**
     * When user clicks on "My Current Location" button
     */
    public void clickedViewCurrentLocation(){
        if(clickedViewCurrentLocation){
            clickedViewCurrentLocation = false;
        }
        else{
            clickedViewCurrentLocation = true;
        }

        Toast.makeText(getContext(), Boolean.toString(clickedViewCurrentLocation), Toast.LENGTH_SHORT).show();
    }


    /**
     * Display user's location if permission for the app to receive location details is granted.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == APPROVED_REQUEST_CODE){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                displayUserCurrentLocation(googleMap);
            }
            else{
                //Toast.makeText("ERROR", "Permission to access user location was denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Displays user's location if permission is granted.
     */
    @SuppressLint("MissingPermission")
    private void displayUserCurrentLocation(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //googleMap.clear();
            if(locationRequest == null){
                locationRequest = LocationRequest.create();
                if(locationRequest != null){
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(1);
                    LocationCallback locationCallback = new LocationCallback() {

                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            displayUserCurrentLocation(googleMap);
                        }
                    };

                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                }
            }
            googleMap.setMyLocationEnabled(true);//Display location
            googleMap.getUiSettings().setMyLocationButtonEnabled(false); //Button has bad UI design, so removed
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null){
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        if(clickedViewCurrentLocation){
                            setCameraPosition(latLng, "");
                        }
                    }
                    else{
                        //Cannot use just "this", it would refer to the OnCompleteListener class and not MapsActivity.
                        //Toast.makeText(MapActivity.this, "Unable to get your location. Try again", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, APPROVED_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            txtSearch.setText(place.getAddress());
            getLocationFromSearch();
            //.setText(String.format("Name" + place.getName()) // Gets name of place
            //.setText(String.valueOf(place.getLatLng()))  //Gets latlng
        }
    }

    private void getLocationFromSearch() {
        String searchInput = txtSearch.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchInput, 1);
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);//Get first location from the list

            Log.d("Result", "Found a location: " + address.toString());
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            clickedViewCurrentLocation = false;
            setCameraPosition(latLng, address.getAddressLine(0));

        }
    }

    public void setCameraPosition(LatLng latLng, String title){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);//zoom in to that distance
        googleMap.moveCamera(cameraUpdate);
        if(!title.equals("")){
            googleMap.clear();
            hideKeyboard();
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            googleMap.addMarker(markerOptions);
        }
    }

    private void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void setupLocationServices(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
    }
}