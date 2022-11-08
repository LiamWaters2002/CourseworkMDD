package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ActivityMapsBinding binding;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int APPROVED_REQUEST_CODE = 1000;

    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupLocationServices();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        displayUserCurrentLocation();
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
//            googleMap.clear();
//            if(locationRequest == null){
//                locationRequest = LocationRequest.create();
//                if(locationRequest != null){
//                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                    locationRequest.setInterval(10000);
//                    locationRequest.setFastestInterval(500);
//
//                    LocationCallback locationCallback = new LocationCallback() {
//
//                        @Override
//                        public void onLocationResult(@NonNull LocationResult locationResult) {
//                            super.onLocationResult(locationResult);
//
//                            displayUserCurrentLocation();
//                        }
//                    };
//
//                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
//
//                }
//            }
            googleMap.setMyLocationEnabled(true);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null){
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Your current location"))

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);//zoom in to that distance
                        googleMap.moveCamera(cameraUpdate);
                        displayUserCurrentLocation();
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

    /**
     *
     */
    private void setupLocationServices(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

}