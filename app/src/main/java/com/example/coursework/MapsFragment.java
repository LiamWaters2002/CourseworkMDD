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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.coursework.R;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.example.coursework.LocationDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment {

    private EditText txtSearch;
    private Marker markerSelected;
    private Button btnCurrentUserLocation;
    private RelativeLayout btnAdd;
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
        btnAdd = getView().findViewById(R.id.relative_layout_button_add);

        btnCurrentUserLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedViewCurrentLocation();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(markerSelected != null){
                    addToDatabase(markerSelected);
                }

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

    private void addToDatabase(Marker markerSelected) {
        LocationDatabase database = new LocationDatabase(getContext());

        LatLng position = markerSelected.getPosition();

        String name = markerSelected.getTitle();
        double latitude = position.latitude;
        double longitude = position.longitude;
        String weatherPreference = "This needs to be implemented";
        int priceRange = 0;
        database.addLocation(name, latitude, longitude, weatherPreference, priceRange);
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

        /**
         * Detects when user clicks on a map marker, save it's location
         */
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                markerSelected = marker;
                marker.showInfoWindow();
                Toast.makeText(getContext(), "Marker Selected", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        /**
         * Detects when user clicks on the map, off from the marker.
         */
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if(!markerSelected.equals(null)){
                    Toast.makeText(getContext(), "Marker Deselected", Toast.LENGTH_SHORT).show();
                    markerSelected.hideInfoWindow();
                }

            }
        });


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
                            setCameraPosition(latLng, null);
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
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocationName(searchInput, 1);
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }

        if (addressList.size() > 0) {
            Address address = addressList.get(0);//Get first location from the addressList

            Log.d("Result", "Found a location: " + address.toString());
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            clickedViewCurrentLocation = false;
            //setCameraPosition(latLng, address.getAddressLine(0));
            setCameraPosition(latLng, address);
        }
    }

    public void setCameraPosition(LatLng latLng, Address address){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);//zoom in to that distance
        googleMap.moveCamera(cameraUpdate);
        if(address != null){
            googleMap.clear();
            hideKeyboard();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng).title(address.getAddressLine(0));
            markerOptions.position(latLng).snippet(
                    "Administrative Area: " + address.getAdminArea() + "\n" +
                            "City: " + address.getLocality() + "\n" +
                            "Post Code:" + address.getPostalCode()  + "\n" +
                            "Phone:" + address.getPostalCode()  + "\n" +
                            "Website:" + address.getPostalCode()
            );
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