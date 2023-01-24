package com.example.coursework;

import static com.example.coursework.BuildConfig.MAPS_API_KEY;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.net.URL;

public class InformationFragment extends Fragment {


    private TextView txtLocationName;
    private TextView txtPlaceType;
    private TextView txtPriority;
    private TextView txtWeatherPreference;

    //Place Type
    private LinearLayout btnAttractions;
    private LinearLayout btnRestaurants;
    private LinearLayout btnBars;
    private LinearLayout btnNightClubs;

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

    //BottomSheetDialog
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog confirmBottomSheetDialog;
    private TextView txtErrorMessage;
    private Button btnAddToDatabase;
    private RelativeLayout btnSave;

    private ImageView imgWeatherPreference;
    private ImageView imgPriority;
    private ImageView imgPlaceType;

    private Button btnRemoveFromDatabase;




    private Toolbar toolbar;
    private Button btnEdit;
    private Button btnDelete;
    private LocationDatabase locationDatabase;

    private int id, priority;
    private String placeType, weatherPreference, locationName;
    private Double latitude, longitude;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationDatabase = new LocationDatabase(getContext());
        Bundle bundle = this.getArguments();
        id = bundle.getInt("id");
        Cursor cursor = locationDatabase.fetchRow(id);

        cursor.moveToNext();
        id = Integer.parseInt(cursor.getString(0));
        locationName = cursor.getString(1);
        placeType = cursor.getString(2);
        latitude = Double.parseDouble(cursor.getString(3));
        longitude = Double.parseDouble(cursor.getString(4));
        priority = Integer.parseInt(cursor.getString(5));
        weatherPreference = cursor.getString(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

//        String streetViewUrl = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location=" + latitude + "," + longitude + "&key=" + MAPS_API_KEY;
//
//        ImageView imageView = getView().findViewById(R.id.imgPhoto);
//        // Download the image from the URL
//        try {
//            URL url = new URL(streetViewUrl);
//            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            imageView.setImageBitmap(image);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //BottomSheetDialog
        confirmBottomSheetDialog = new BottomSheetDialog(getContext());
        View confirmBottomSheetDialogView = getLayoutInflater().inflate(R.layout.confirm_bottom_sheet_dialog, null);
        confirmBottomSheetDialog.setContentView(confirmBottomSheetDialogView);
        btnRemoveFromDatabase = confirmBottomSheetDialog.findViewById(R.id.btnRemoveFromDatabase);


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


        imgWeatherPreference = getView().findViewById(R.id.imgWeatherPreference);
        imgPriority = getView().findViewById(R.id.imgPriority);
        imgPlaceType = getView().findViewById(R.id.imgPlaceType);


        btnRemoveFromDatabase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                locationDatabase.deleteRow(id);
                MainActivity activity = (MainActivity) getActivity();
                Bundle bundle = new Bundle();
                activity.switchFragment(new SavedFragment(), bundle);
            }
        });

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
                    addToDatabase(id, priority, weatherPreference);
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

        txtLocationName  = getView().findViewById(R.id.locationName);
        txtPlaceType  = getView().findViewById(R.id.txtPlaceType);
        txtPriority  = getView().findViewById(R.id.txtPriority);
        txtWeatherPreference  = getView().findViewById(R.id.txtWeatherPreference);

        txtLocationName.setText(locationName);

        if(placeType.equals("tourist_attraction")){
            txtPlaceType.setText(Html.fromHtml("<b>Place type:</b>  Tourist attraction"));
            imgPlaceType.setImageResource(R.drawable.camera);
        }
        else if(placeType.equals("bar")){
            txtPlaceType.setText(Html.fromHtml("<b>Place type:</b>  Bar"));
            imgPlaceType.setImageResource(R.drawable.bar);
        }
        else if(placeType.equals("night_club")){
            txtPlaceType.setText(Html.fromHtml("<b>Place type:</b>  Night club"));
            imgPlaceType.setImageResource(R.drawable.discoball);
        }
        else if(placeType.equals("restaurant")){
            txtPlaceType.setText(Html.fromHtml("<b>Place type:</b>  Restaurant"));
            imgPlaceType.setImageResource(R.drawable.cutlery);
        }

        Toast.makeText(getContext(),Integer.toString(priority),Toast.LENGTH_SHORT).show();
        if(priority == 1){
            txtPriority.setText(Html.fromHtml("<b>Priority:</b> Low"));
            imgPriority.setImageResource(R.drawable.low);
        }
        else if(priority == 2){
            txtPriority.setText(Html.fromHtml("<b>Priority:</b> Medium"));
            imgPriority.setImageResource(R.drawable.medium);
        }
        else if (priority == 3){
            txtPriority.setText(Html.fromHtml("<b>Priority:</b> High"));
            imgPriority.setImageResource(R.drawable.high);
        }


        txtWeatherPreference.setText(weatherPreference);
        if(weatherPreference.equals("clear")){
            imgWeatherPreference.setImageResource(R.drawable.clear);
            txtWeatherPreference.setText(Html.fromHtml("<b>Weather preference:</b> Clear"));
        }
        else if(weatherPreference.equals("cloud")){
            imgWeatherPreference.setImageResource(R.drawable.cloud);
            txtWeatherPreference.setText(Html.fromHtml("<b>Weather preference:</b> Cloud"));
        }
        else{
            imgWeatherPreference.setImageResource(R.drawable.rain);
            txtWeatherPreference.setText(Html.fromHtml("<b>Weather preference:</b> Rain"));
        }

        btnEdit = getView().findViewById(R.id.btnEdit);
        btnDelete = getView().findViewById(R.id.btnDelete);

        btnEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                locationDatabase.editRow(id, priority, weatherPreference);
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

        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                confirmBottomSheetDialog.show();
            }
        });



        btnDelete = getView().findViewById(R.id.btnDelete);


        toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("Saved > " + locationName);
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack("saved", 1);
            }
        });
    }

    private void addToDatabase(int id, int priority, String weatherPreference) {
        LocationDatabase database = new LocationDatabase(getContext());
        database.editRow(id, priority, weatherPreference);
        Toast.makeText(getContext(),"changes made to database", Toast.LENGTH_SHORT).show();
    }
}