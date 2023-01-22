package com.example.coursework.unused;

import androidx.fragment.app.Fragment;

public class TravelFragment extends Fragment {

//    private EditText txtSearch;
//    private Marker markerSelected;
//    private Button btnSwitchView;
//    private RelativeLayout btnAdd;
//    private LinearLayout btnAttractions;
//    private LinearLayout btnRestaurants;
//    private LinearLayout btnBars;
//    private LinearLayout btnNightClubs;
//    private ImageView btnRemove;
//
//    private boolean clickedViewCurrentLocation;//Use this for button click
//
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private static final int APPROVED_REQUEST_CODE = 1000;
//
//    private LocationRequest locationRequest;
//
//    private static GoogleMap googleMap;
//
//    private void GoogleMap(){}
//
//    /**
//     * When map is ready, add onClickListeners and start displaying user's location.
//     * @param googleMap
//     */
//    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
//        @Override
//        public void onMapReady(GoogleMap googleMap){
//            setupInteractiveMap(googleMap);
//            displayUserCurrentLocation();
//        }
//    };
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_explore, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Places.initialize(getContext(), MAPS_API_KEY);
//
//        clickedViewCurrentLocation = true;
//
//        btnAdd = getView().findViewById(R.id.relative_layout_button_add);
//        btnAttractions = getView().findViewById(R.id.btnAttractions);
//        btnRestaurants = getView().findViewById(R.id.btnRestaurants);
//        btnBars = getView().findViewById(R.id.btnBars);
//        btnNightClubs = getView().findViewById(R.id.btnNightClub);
//        btnRemove = getView().findViewById(R.id.ic_remove);
//
//        btnRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickedRemove();
//            }
//        });
//
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
//                View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
//                bottomSheetDialog.setContentView(bottomSheetDialogView);
//                bottomSheetDialog.show();
//
//                if(markerSelected != null){
//                    addToDatabase(markerSelected);
//                }
//
//            }
//        });
//
//        btnAttractions.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                clickedPlaceType("tourist_attraction");
//            }
//        });
//
//        btnBars.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                clickedPlaceType("bar");
//            }
//        });
//
//        btnRestaurants.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                clickedPlaceType("restaurant");
//            }
//        });
//
//        btnNightClubs.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                clickedPlaceType("night_club");
//            }
//        });
//
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
//
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//       if (mapFragment != null) {
//            mapFragment.getMapAsync(onMapReadyCallback);
//        }
//        setupLocationServices();
//    }
//
//
//    private void addToDatabase(Marker markerSelected) {
//        LocationDatabase database = new LocationDatabase(getContext());
//
//        LatLng position = markerSelected.getPosition();
//
//        String name = markerSelected.getTitle();
//        double latitude = position.latitude;
//        double longitude = position.longitude;
//        String weatherPreference = "Weather preference needs to be implemented";
//        int priority = 0;
//        database.addLocation(name, latitude, longitude, weatherPreference, priority);
//    }
//
//    /**
//     * When user clicks on "My Current Location" button
//     */
//    public void clickedRemove(){
//            clickedViewCurrentLocation = true;
//            googleMap.clear();
//            txtSearch.setText("");
//            hideKeyboard();
//
//        Toast.makeText(getContext(), Boolean.toString(clickedViewCurrentLocation), Toast.LENGTH_SHORT).show();
//    }
//
//
//
//    /**
//     * Display user's location if permission for the app to receive location details is granted.
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(requestCode == APPROVED_REQUEST_CODE){
//            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                displayUserCurrentLocation();
//            }
//            else{
//                //Toast.makeText("ERROR", "Permission to access user location was denied.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    /**
//     * Add OnClickListeners for the map's marker
//     * @param googleMap
//     */
//    private void setupInteractiveMap(GoogleMap googleMap){
//        this.googleMap = googleMap;
//
//        /**
//         * Detects when user clicks on a map marker, save it's location
//         */
//        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
//
//            @Override
//            public boolean onMarkerClick(@NonNull Marker marker) {
//                markerSelected = marker;
//                marker.showInfoWindow();
//                Toast.makeText(getContext(), "Marker Selected", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//
//        /**
//         * Detects when user clicks on the map, off from the marker.
//         */
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
//
//            @Override
//            public void onMapClick(@NonNull LatLng latLng) {
////                if(!markerSelected.equals(null)){
////                    Toast.makeText(getContext(), "Marker Deselected", Toast.LENGTH_SHORT).show();
////                    markerSelected.hideInfoWindow();
////                }
//
//            }
//        });
//    }
//
//
//    /**
//     * Displays user's location if permission is granted.
//     */
//    @SuppressLint("MissingPermission")
//    private void displayUserCurrentLocation() {
//        try{
//            if(ContextCompat.checkSelfPermission(requireActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                //googleMap.clear();
//                if(locationRequest == null){
//                    locationRequest = LocationRequest.create();
//                    if(locationRequest != null){
//                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                        locationRequest.setInterval(1);
//                        LocationCallback locationCallback = new LocationCallback() {
//
//                            @Override
//                            public void onLocationResult(@NonNull LocationResult locationResult) {
//                                super.onLocationResult(locationResult);
//                                displayUserCurrentLocation();
//                            }
//                        };
//
//                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
//
//                    }
//                }
//                googleMap.setMyLocationEnabled(true);//Display location
//                googleMap.getUiSettings().setMyLocationButtonEnabled(false); //Button has bad UI design, so removed
//                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        Location location = task.getResult();
//                        if(location != null){
//                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                            if(clickedViewCurrentLocation){
//                                setCameraPosition(latLng, null);
//                            }
//                        }
//                        else{
//                            //Cannot use just "this", it would refer to the OnCompleteListener class and not MapsActivity.
//                            //Toast.makeText(MapActivity.this, "Unable to get your location. Try again", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//            }
//            else{
//                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, APPROVED_REQUEST_CODE);
//            }
//        }
//        catch(IllegalStateException illegalStateException){ //Occurs when changing fragments, can be ignored
//            //Log.e("Catched Error",  String.valueOf(illegalStateException));
//        }
//
//    }
//
//    /**
//     * Used when a location has been
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 100 && resultCode == RESULT_OK){
//            Place place = Autocomplete.getPlaceFromIntent(data);
//            txtSearch.setText(place.getAddress());
//            getLocationFromSearch();
//            //.setText(String.format("Name" + place.getName()) // Gets name of place
//            //.setText(String.valueOf(place.getLatLng()))  //Gets latlng
//        }
//    }
//
//
//    private void getLocationFromSearch() {
//        String searchInput = txtSearch.getText().toString();
//        Geocoder geocoder = new Geocoder(getContext());
//        List<Address> addressList = new ArrayList<>();
//        try {
//            addressList = geocoder.getFromLocationName(searchInput, 1);
//        } catch (IOException e) {
//            Log.e("Error", e.getMessage());
//        }
//
//        if (addressList.size() > 0) {
//            Address address = addressList.get(0);//Get first location from the addressList
//
//            Log.d("Result", "Found a location: " + address.toString());
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            clickedViewCurrentLocation = false;
//            //setCameraPosition(latLng, address.getAddressLine(0));
//            setCameraPosition(latLng, address);
//        }
//    }
//
//    public void setCameraPosition(LatLng latLng, Address address){
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);//zoom in to that distance
//        googleMap.moveCamera(cameraUpdate);
//        if(address != null){
//            googleMap.clear();
//            hideKeyboard();
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng).title(address.getAddressLine(0));
//            markerOptions.position(latLng).snippet(
//                    "Administrative Area: " + address.getAdminArea() + "\n" +
//                            "City: " + address.getLocality() + "\n" +
//                            "Post Code:" + address.getPostalCode()  + "\n" +
//                            "Phone:" + address.getPostalCode()  + "\n" +
//                            "Website:" + address.getPostalCode()
//            );
//            googleMap.addMarker(markerOptions);
//
//        }
//    }
//
//
//    public void setMarkerPosition(LatLng latLng, String address){
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng).title(address);
//        markerOptions.position(latLng).snippet(
//                "Test"
//        );
//        googleMap.addMarker(markerOptions);
//    }
//
//
//    private void hideKeyboard(){
//        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
//    }
//
//    private void setupLocationServices(){
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
//    }
//
//    @SuppressLint("MissingPermission")
//    private void clickedPlaceType(String type) {
//        googleMap.clear();
//
//        Place.Type placeType;
//
//        CameraPosition cameraPosition = googleMap.getCameraPosition();
//        LatLng latLng = cameraPosition.target;
//
//
//        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        stringBuilder.append("location=" + latLng.latitude + "," + latLng.longitude);
//        stringBuilder.append("&radius=1000");
//        stringBuilder.append("&type=" + type.toLowerCase());
//        stringBuilder.append("&sensor=true");
//        stringBuilder.append("&key=" + MAPS_API_KEY);
//
//        String url = stringBuilder.toString();
//        Object dataFetch[] = new Object[2];
//        dataFetch[0] = googleMap;
//        dataFetch[1] = url;
//
//        FetchUrlData fetchUrlData = new FetchUrlData();
//        fetchUrlData.execute(dataFetch);
//    }
}