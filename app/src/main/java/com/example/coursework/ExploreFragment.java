package com.example.coursework;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static com.example.coursework.BuildConfig.MAPS_API_KEY;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private String placeType;
    private TextView txtErrorMessage;
    private Marker markerSelected;
    private Button btnAddToDatabase;
    private RelativeLayout btnSave;

    //Place Type
    private LinearLayout btnAttractions;
    private LinearLayout btnRestaurants;
    private LinearLayout btnBars;
    private LinearLayout btnNightClubs;

    //Place Type Signposts
    private ImageView btnAttractionsSignpost;
    private ImageView btnRestaurantsSignpost;
    private ImageView btnBarsSignpost;
    private ImageView btnNightClubsSignpost;

    //Weather Type
    private LinearLayout btnClear;
    private LinearLayout btnCloud;
    private LinearLayout btnRain;

    //Weather Type Signposts
    private ImageView btnClearSignpost;
    private ImageView btnCloudSignpost;
    private ImageView btnRainSignpost;

    //Priority Rank
    private LinearLayout btnLow;
    private LinearLayout btnMedium;
    private LinearLayout btnHigh;

    //Priority Rank Signpost
    private ImageView btnLowSignpost;
    private ImageView btnMediumSignpost;
    private ImageView btnHighSignpost;

    private boolean clickedViewCurrentLocation;//Use this for button click

    private BottomSheetDialog bottomSheetDialog;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int APPROVED_REQUEST_CODE = 1000;

    private LocationRequest locationRequest;

    private static GoogleMap googleMap;

    private void GoogleMap(){}

    /**
     * When map is ready, add onClickListeners and start displaying user's location.
     * @param googleMap
     */
    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap){
            setupInteractiveMap(googleMap);
            displayUserCurrentLocation();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Places.initialize(getContext(), MAPS_API_KEY);

        clickedViewCurrentLocation = true;

        //BottomSheetDialog
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        btnAddToDatabase = bottomSheetDialog.findViewById(R.id.btnAddToDatabase);
        txtErrorMessage = bottomSheetDialog.findViewById(R.id.txtErrorMessage);
        btnSave = getView().findViewById(R.id.relative_layout_button_add);

        //Place Types
        btnAttractions = getView().findViewById(R.id.btnAttractions);
        btnRestaurants = getView().findViewById(R.id.btnRestaurants);
        btnBars = getView().findViewById(R.id.btnBars);
        btnNightClubs = getView().findViewById(R.id.btnNightClubs);

        //Place Types Signposts
        btnAttractionsSignpost = getView().findViewById(R.id.btnAttractionsSignpost);
        btnRestaurantsSignpost = getView().findViewById(R.id.btnRestaurantsSignpost);
        btnBarsSignpost = getView().findViewById(R.id.btnBarsSignpost);
        btnNightClubsSignpost = getView().findViewById(R.id.btnNightClubsSignpost);

        //Set Place Type Signposts invisible
        btnAttractionsSignpost.setVisibility(View.INVISIBLE);
        btnRestaurantsSignpost.setVisibility(View.INVISIBLE);
        btnBarsSignpost.setVisibility(View.INVISIBLE);
        btnNightClubsSignpost.setVisibility(View.INVISIBLE);

        //Weather Types
        btnClear = bottomSheetDialog.findViewById(R.id.btnClear);
        btnCloud = bottomSheetDialog.findViewById(R.id.btnCloud);
        btnRain = bottomSheetDialog.findViewById(R.id.btnRain);

        //Weather Types Signpost
        btnClearSignpost = bottomSheetDialog.findViewById(R.id.btnClearSignpost);
        btnCloudSignpost = bottomSheetDialog.findViewById(R.id.btnCloudSignpost);
        btnRainSignpost = bottomSheetDialog.findViewById(R.id.btnRainSignpost);

        //Set Weather Type Signposts invisible
        btnClearSignpost.setVisibility(View.INVISIBLE);
        btnCloudSignpost.setVisibility(View.INVISIBLE);
        btnRainSignpost.setVisibility(View.INVISIBLE);

        //Priority Ranks
        btnLow = bottomSheetDialog.findViewById(R.id.btnLow);
        btnMedium = bottomSheetDialog.findViewById(R.id.btnMedium);
        btnHigh = bottomSheetDialog.findViewById(R.id.btnHigh);

        //Weather Types Signpost
        btnLowSignpost = bottomSheetDialog.findViewById(R.id.btnLowSignpost);
        btnMediumSignpost = bottomSheetDialog.findViewById(R.id.btnMediumSignpost);
        btnHighSignpost = bottomSheetDialog.findViewById(R.id.btnHighSignpost);

        //Set Weather Type Signposts invisible
        btnLowSignpost.setVisibility(View.INVISIBLE);
        btnMediumSignpost.setVisibility(View.INVISIBLE);
        btnHighSignpost.setVisibility(View.INVISIBLE);


//        btnRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickedRemove();
//            }
//        });

        btnAddToDatabase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String errorMessage = "";
                String weatherPreference = "";
                int priority = 0;

                if(btnLowSignpost.getVisibility() == View.VISIBLE){
                    priority = 1;
                }
                else if(btnMediumSignpost.getVisibility() == View.VISIBLE){
                    priority = 2;
                }
                else if(btnHighSignpost.getVisibility() == View.VISIBLE){
                    priority = 3;
                }
                else{
                    errorMessage = "Please select a priority level.";
                }

                if(btnClearSignpost.getVisibility() == View.VISIBLE){
                    weatherPreference = "clear";
                }
                else if(btnCloudSignpost.getVisibility() == View.VISIBLE){
                    weatherPreference = "cloud";
                }
                else if(btnRainSignpost.getVisibility() == View.VISIBLE){
                    weatherPreference = "rain";
                }
                else{
                    if(errorMessage.equals("")) {
                        errorMessage = "Please select a weather preference.";
                    }
                    else{
                        errorMessage = "Please select a priority level and a weather preference";
                    }
                }

                if(errorMessage.equals("")){
                    addToDatabase(markerSelected, priority, weatherPreference, placeType);
                }
                else{
                    txtErrorMessage.setText(errorMessage);
                }


            }
        });

        btnHigh.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                btnHighSignpost.setVisibility(View.VISIBLE);
                btnMediumSignpost.setVisibility(View.INVISIBLE);
                btnLowSignpost.setVisibility(View.INVISIBLE);
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                btnHighSignpost.setVisibility(View.INVISIBLE);
                btnMediumSignpost.setVisibility(View.VISIBLE);
                btnLowSignpost.setVisibility(View.INVISIBLE);
            }
        });

        btnLow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                btnHighSignpost.setVisibility(View.INVISIBLE);
                btnMediumSignpost.setVisibility(View.INVISIBLE);
                btnLowSignpost.setVisibility(View.VISIBLE);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                btnClearSignpost.setVisibility(View.VISIBLE);
                btnCloudSignpost.setVisibility(View.INVISIBLE);
                btnRainSignpost.setVisibility(View.INVISIBLE);
            }
        });

        btnCloud.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                btnClearSignpost.setVisibility(View.INVISIBLE);
                btnCloudSignpost.setVisibility(View.VISIBLE);
                btnRainSignpost.setVisibility(View.INVISIBLE);
            }
        });

        btnRain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                btnClearSignpost.setVisibility(View.INVISIBLE);
                btnCloudSignpost.setVisibility(View.INVISIBLE);
                btnRainSignpost.setVisibility(View.VISIBLE);
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Reset selection
                btnClearSignpost.setVisibility(View.INVISIBLE);
                btnCloudSignpost.setVisibility(View.INVISIBLE);
                btnRainSignpost.setVisibility(View.INVISIBLE);

                btnHighSignpost.setVisibility(View.INVISIBLE);
                btnMediumSignpost.setVisibility(View.INVISIBLE);
                btnLowSignpost.setVisibility(View.INVISIBLE);
                txtErrorMessage.setText("");


                bottomSheetDialog.show();
            }
        });

        btnAttractions.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                clickedPlaceType("tourist_attraction");
            }
        });

        btnBars.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                clickedPlaceType("bar");
            }
        });

        btnRestaurants.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                clickedPlaceType("restaurant");
            }
        });

        btnNightClubs.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                clickedPlaceType("night_club");
            }
        });

//        txtSearch = (EditText) getView().findViewById(R.id.txtSearch);
//        txtSearch.setFocusable(false);
//        txtSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Place.Field> placeList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
//
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeList).build(getActivity());
//
//                startActivityForResult(intent, 100);
//            }
//        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(onMapReadyCallback);
        }
        setupLocationServices();
    }


    private void addToDatabase(Marker markerSelected, int priority, String weatherPreference, String placeType) {
        LocationDatabase database = new LocationDatabase(getContext());

        LatLng position = markerSelected.getPosition();

        String name = markerSelected.getTitle();
        double latitude = position.latitude;
        double longitude = position.longitude;
        database.addLocation(name, latitude, longitude, weatherPreference, priority, placeType);
        Toast.makeText(getContext(),"added to database", Toast.LENGTH_SHORT).show();
    }

    /**
     * When user clicks on "My Current Location" button
     */
    public void clickedRemove(){
        clickedViewCurrentLocation = true;
        googleMap.clear();
//        txtSearch.setText("");
        hideKeyboard();

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
                displayUserCurrentLocation();
            }
            else{
                //Toast.makeText("ERROR", "Permission to access user location was denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Add OnClickListeners for the map's marker
     * @param googleMap
     */
    private void setupInteractiveMap(GoogleMap googleMap){
        this.googleMap = googleMap;

        /**
         * Detects when user clicks on a map marker, save it's location
         */
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                markerSelected = marker;
                marker.showInfoWindow();
//                Toast.makeText(getContext(), "Marker Selected", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        /**
         * Detects when user clicks on the map, off from the marker.
         */
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(@NonNull LatLng latLng) {
//                if(!markerSelected.equals(null)){
//                    Toast.makeText(getContext(), "Marker Deselected", Toast.LENGTH_SHORT).show();
//                    markerSelected.hideInfoWindow();
//                }

            }
        });
    }


    /**
     * Displays user's location if permission is granted.
     */
    @SuppressLint("MissingPermission")
    private void displayUserCurrentLocation() {
        try{
            if(ContextCompat.checkSelfPermission(requireActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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
                                //Default place type display
                                if(btnAttractionsSignpost.getVisibility() == View.INVISIBLE &&
                                        btnRestaurantsSignpost.getVisibility() == View.INVISIBLE &&
                                        btnBarsSignpost.getVisibility() == View.INVISIBLE &&
                                        btnNightClubsSignpost.getVisibility() == View.INVISIBLE){
                                    clickedPlaceType("tourist_attraction");
                                }
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
            //Log.e("Catched Error",  String.valueOf(illegalStateException));
        }

    }

    /**
     * Used when a location has been
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
//            txtSearch.setText(place.getAddress());
            getLocationFromSearch();
            //.setText(String.format("Name" + place.getName()) // Gets name of place
            //.setText(String.valueOf(place.getLatLng()))  //Gets latlng
        }
    }


    private void getLocationFromSearch() {
        String searchInput = ""; //temp
//        String searchInput = txtSearch.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocationName(searchInput, 1);
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }

        if (addressList.size() > 0) {
            Address address = addressList.get(0);//Get first location from the addressList
            address.getCountryName();

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
        googleMap.animateCamera(cameraUpdate);
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


    public void setMarkerPosition(LatLng latLng, String address){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(address);
        markerOptions.position(latLng).snippet(
                "Test"
        );
        googleMap.addMarker(markerOptions);
    }


    private void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void setupLocationServices(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @SuppressLint("MissingPermission")
    private void clickedPlaceType(String type) {
        googleMap.clear();

        btnAttractionsSignpost.setVisibility(View.INVISIBLE);
        btnRestaurantsSignpost.setVisibility(View.INVISIBLE);
        btnBarsSignpost.setVisibility(View.INVISIBLE);
        btnNightClubsSignpost.setVisibility(View.INVISIBLE);

        if(type == "tourist_attraction"){
            btnAttractionsSignpost.setVisibility(View.VISIBLE);
            placeType = type;
        }
        else if(type == "restaurant"){
            btnRestaurantsSignpost.setVisibility(View.VISIBLE);
            placeType = type;
        }
        else if(type == "bar"){
            btnBarsSignpost.setVisibility(View.VISIBLE);
            placeType = type;
        }
        else if(type == "night_club"){
            btnNightClubsSignpost.setVisibility(View.VISIBLE);
            placeType = type;
        }

        Place.Type placeType;

        CameraPosition cameraPosition = googleMap.getCameraPosition();
        LatLng latLng = cameraPosition.target;


        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location=" + latLng.latitude + "," + latLng.longitude);
        stringBuilder.append("&radius=1000");
        stringBuilder.append("&type=" + type.toLowerCase());
        stringBuilder.append("&sensor=true");
        stringBuilder.append("&key=" + MAPS_API_KEY);

        String url = stringBuilder.toString();
        Object dataFetch[] = new Object[2];
        dataFetch[0] = googleMap;
        dataFetch[1] = url;

        FetchUrlData fetchUrlData = new FetchUrlData();
        fetchUrlData.execute(dataFetch);
    }
}