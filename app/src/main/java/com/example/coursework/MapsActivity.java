package com.example.coursework;

import static com.example.coursework.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.coursework.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ActivityMapsBinding binding;

    private EditText txtSearch;
    private View view;
    private boolean clickedViewCurrentLocation;//Use this for button click

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int APPROVED_REQUEST_CODE = 1000;

    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(getApplicationContext(), MAPS_API_KEY);

        clickedViewCurrentLocation = true;

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txtSearch = (EditText) findViewById(R.id.txtSearch);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupLocationServices();
    }

    /**
     * When user clicks on "My Current Location" button
     * @param view
     */
    public void clickedViewCurrentLocation(View view){
        if(clickedViewCurrentLocation){
            clickedViewCurrentLocation = false;
        }
        else{
            clickedViewCurrentLocation = true;
        }

        Toast.makeText(this, Boolean.toString(clickedViewCurrentLocation), Toast.LENGTH_SHORT).show();
    }

    /**
     * Listener which checks whether
     */
    private void searchInputEvent(){
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH //search operation
                        || actionId == EditorInfo.IME_ACTION_DONE //Done operation
                        || keyEvent.getAction()== KeyEvent.ACTION_DOWN
                        || keyEvent.getAction()== KeyEvent.KEYCODE_ENTER){

                    //Execute searching method here...
                    getLocationFromSearch();


                }
                return false;
            }
        });
    }

    private void getLocationFromSearch(){
        String searchInput = txtSearch.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchInput, 1);
        }
        catch(IOException e){
            Log.e("Error", e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);//Get first location from the list

            Log.d("Result", "Found a location: " + address.toString());
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            clickedViewCurrentLocation = false;
            setCameraPosition(latLng, address.getAddressLine(0));

        }



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        displayUserCurrentLocation();
        searchInputEvent();
    }

    /**
     * Method requests permission to access a user's location.
     *
     */
    private void getPermissionToAccessLocation() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, APPROVED_REQUEST_CODE);
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == APPROVED_REQUEST_CODE){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                displayUserCurrentLocation();
            }
            else{
                Toast.makeText(this, "Permission to access user location was denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     */
    @SuppressLint("MissingPermission")
    private void displayUserCurrentLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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

                            displayUserCurrentLocation();
                        }
                    };

                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                }
            }
            googleMap.setMyLocationEnabled(true);
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
                        Toast.makeText(MapsActivity.this, "Unable to get your location. Try again", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else{
            getPermissionToAccessLocation();
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
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.txtSearch).getWindowToken(), 0);

    }



    /**
     *
     */
    private void setupLocationServices(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

}