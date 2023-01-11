package com.example.coursework;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

import static androidx.core.app.ActivityCompat.requestPermissions;

import static com.example.coursework.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;

public class Map extends Fragment {
    private View view;
    private Location lastCameraLocation;
    private Activity activity;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Context fragmentcontext;
    private Context requireActivityContext;
    private boolean clickedViewCurrentLocation;//Use this for button click

    private static GoogleMap googleMap;
    private void GoogleMap(){}

    private Marker markerSelected;
    private LocationRequest locationRequest;
    private static final int APPROVED_REQUEST_CODE = 1000;

    public Map(Context fragmentContext, Context requireActivityContext, Activity activity, View view){
        this.fragmentcontext = fragmentContext;
        this.requireActivityContext = requireActivityContext;
        this.activity = activity;
        this.view = view;
    }

    /**
     * When map is ready, add onClickListeners and start displaying user's location.
     * @param googleMap
     */
    protected OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap){
            //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(fragmentcontext);
            clickedViewCurrentLocation = true;
            Places.initialize(fragmentcontext, MAPS_API_KEY);
            setupInteractiveMap(googleMap);
            if(activity != null){
                displayUserCurrentLocation();
            }
            else{
                Toast.makeText(fragmentcontext, "Empty Activity", Toast.LENGTH_SHORT).show();
            }
            displayUserCurrentLocation();
        }
    };

    /**
     * Add OnClickListeners for the map's marker
     * @param googleMap
     */
    private void setupInteractiveMap(GoogleMap googleMap) {
        this.googleMap = googleMap;

        /**
         * Detects when user clicks on a map marker, save it's location
         */
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                markerSelected = marker;
                marker.showInfoWindow();
                Toast.makeText(fragmentcontext, "Marker Selected", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    /**
     * Displays user's location if permission is granted.
     */
    @SuppressLint("MissingPermission")
    void displayUserCurrentLocation() {
        try{
            if(ContextCompat.checkSelfPermission(requireActivityContext, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                //googleMap.clear();
                Toast.makeText(fragmentcontext, "Got Got GO todagadfgr", Toast.LENGTH_SHORT).show();
                if(locationRequest == null){
                    locationRequest = LocationRequest.create();
                    if(locationRequest != null){
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        LocationCallback locationCallback = new LocationCallback() {

                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //super.onLocationResult(locationResult);
                                if(locationResult != null){
                                    displayUserCurrentLocation();
                                }
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
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, APPROVED_REQUEST_CODE);
            }
        }
        catch(IllegalStateException illegalStateException){ //Occurs when changing fragments, can be ignored
            Toast.makeText(fragmentcontext, "Illegal state exception", Toast.LENGTH_SHORT).show();
        }
        catch(NullPointerException nullPointerException){
            Toast.makeText(fragmentcontext, "NullPointerException", Toast.LENGTH_SHORT).show();
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                // Check if the location has changed
                if (location != null && location != lastCameraLocation) {
                    animateCamera();
                }
            }
        }
    };

    public void animateCamera(){
        LatLng latLng = new LatLng(lastCameraLocation.getLatitude(), lastCameraLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);
        googleMap.animateCamera(cameraUpdate);
    }


    public void setCameraPosition(LatLng latLng, Address address){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);//zoom in to that distance
        googleMap.moveCamera(cameraUpdate);

        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        lastCameraLocation = location;


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
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected GoogleMap getGoogleMap(){
        return googleMap;
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
                displayUserCurrentLocation();
            }
            else{
                //Toast.makeText("ERROR", "Permission to access user location was denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
