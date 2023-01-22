package com.example.coursework;

import java.util.ArrayList;

public class WeatherTypes {

    private ArrayList<String> cloudWeatherTypes;
    private ArrayList<String> rainWeatherTypes;
    private ArrayList<String> clearWeatherTypes;

    public WeatherTypes(){
        cloudWeatherTypes = new ArrayList<>();
        clearWeatherTypes = new ArrayList<>();

        cloudWeatherTypes.add("Overcast");
        cloudWeatherTypes.add("Partially cloudy");
        cloudWeatherTypes.add("Fog");
        cloudWeatherTypes.add("Dust storm");
        cloudWeatherTypes.add("Smoke Or Haze");
        cloudWeatherTypes.add("Sky Coverage Increasing");
        cloudWeatherTypes.add("Sky Unchanged");


        clearWeatherTypes.add("Clear");

        //The API is able to return43 different types of weather. I am not adding all rain types here...
    }

    public boolean isItCloudWeatherType(String weatherCondition) {
        if(cloudWeatherTypes.contains(weatherCondition)){
            return true;
        }
        return false;
    }
//
//    public boolean isItRainWeatherType(String weatherCondition) {
//        if(rainWeatherTypes.contains(weatherCondition)){
//            return true;
//        }
//        return false;
//    }

    public boolean isItClearWeatherType(String weatherCondition) {
        if(clearWeatherTypes.contains(weatherCondition)){
            return true;
        }
        return false;
    }
}
