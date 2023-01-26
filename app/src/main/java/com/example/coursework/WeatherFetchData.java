package com.example.coursework;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherFetchData extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String location = params[0];
        String apiKey = params[1];
        try {

            URL url = new URL("https://visual-crossing-weather.p.rapidapi.com/forecast?aggregateHours=1&location="+location+"&contentType=json&unitGroup=us&shortColumnNames=0");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-RapidAPI-Key", "feb7abc8a7msh50b4159db41f088p16428bjsn5ad7bf336048");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Error with weatherAdapter.");
            }
            Scanner scanner = new Scanner(connection.getInputStream());
            String data = scanner.useDelimiter("\\Z").next();//end of input
            scanner.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
