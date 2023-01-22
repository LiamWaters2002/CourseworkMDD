package com.example.coursework;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherRecyclerViewHolder> {

    private Context context;
    private ArrayList<String> weatherCondition, date;
    private WeatherRecyclerViewAdapter.CardViewClickListener cardViewClickListener;

    public WeatherRecyclerViewAdapter(WeatherRecyclerViewAdapter.CardViewClickListener cardViewClickListener, Context context, ArrayList<String> weatherCondition, ArrayList<String> date){
        this.cardViewClickListener = cardViewClickListener;
        this.weatherCondition = weatherCondition;
        this.date = date;
        this.context = context;
    }


    @NonNull
    @Override
    public WeatherRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.weather_recycler_view_row, parent, false);
        return new WeatherRecyclerViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(WeatherRecyclerViewHolder holder, int position) {

        if(date == null){
            Log.e("error", "date is null");
        }

        holder.dateTextView.setText(date.get(position));
        String strWeatherCondition = weatherCondition.get(position);
        holder.conditionsTextView.setText(weatherCondition.get(position));

        WeatherTypes weatherTypes = new WeatherTypes();

        if(weatherTypes.isItClearWeatherType(strWeatherCondition)){
            holder.imgWeather.setImageResource(R.drawable.clear);
        }
        else if(weatherTypes.isItCloudWeatherType(strWeatherCondition)){
            holder.imgWeather.setImageResource(R.drawable.cloud);
        }
        else{
            //There are 43 weather types, 35 of them being different rain types :p
            holder.imgWeather.setImageResource(R.drawable.rain);
        }

        holder.itemView.setOnClickListener(view -> {

            cardViewClickListener.onItemClick(date.get(position));

        });
    }

        @Override
    public int getItemCount() {
        return weatherCondition.size();
        }

    public interface CardViewClickListener{
        void onItemClick(String id);
    }

    public class WeatherRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView conditionsTextView;
        public ImageView imgWeather;

        public WeatherRecyclerViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.txtTime);
            conditionsTextView = (TextView) itemView.findViewById(R.id.txtWeather);
            imgWeather = itemView.findViewById(R.id.imgWeatherType);
        }
    }
}