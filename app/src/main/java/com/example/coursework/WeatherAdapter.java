package com.example.coursework;

import android.os.AsyncTask;

import com.android.volley.toolbox.HttpResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherAdapter extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String location = params[0];
        String apiKey = params[1];
        try {
            URL url = new URL("https://visual-crossing-weather.p.rapidapi.com/forecast?aggregateHours=24&location="+location+"&contentType=json&unitGroup=us&shortColumnNames=0");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-RapidAPI-Key", "feb7abc8a7msh50b4159db41f088p16428bjsn5ad7bf336048");
            connection.setRequestProperty("X-RapidAPI-Host", "visual-crossing-weather.p.rapidapi.com");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Error with weatherAdapter.");
            }
            Scanner scanner = new Scanner(connection.getInputStream());
            String data = scanner.useDelimiter("\\Z").next();
            scanner.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
