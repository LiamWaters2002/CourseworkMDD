package com.example.coursework;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchUrlData extends AsyncTask<Object, String, String> {
    String googleNearByPlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected void onPostExecute(String s){
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            //loop, getting name, location and creating a marker.
            for(int i=0; i<jsonArray.length();i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                JSONObject geometry = jsonObj.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");

                String latitude = location.getString("lat");
                String longitude = location.getString("lng");

                JSONObject getName = jsonArray.getJSONObject(i);
                String name = getName.getString("name");

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Object... objects) {

        googleMap=(GoogleMap) objects[0];
        url = (String) objects[1];
        ConnectUrl useUrl = new ConnectUrl();
        try {
            googleNearByPlacesData = useUrl.retrieveURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleNearByPlacesData;
    }
}
