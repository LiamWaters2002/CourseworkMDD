package com.example.coursework;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.CustomViewHolder> {

    Context context;
    ArrayList<Integer> locationId, priority;
    ArrayList<String>  locationName, weatherPreference, placeType, dateList;
    ArrayList<Double> latitude, longitude;
    CardViewClickListener cardViewClickListener;

    public ScheduleRecyclerViewAdapter(CardViewClickListener cardViewClickListener, ArrayList<Integer> locationId, ArrayList<String> locationName,
                                       ArrayList<Double> latitude, ArrayList<Double> longitude, ArrayList<Integer> priority, ArrayList<String> weatherPreference, ArrayList<String> placeType, ArrayList<String> dateList){
        this.context = context;
        this.locationId = locationId;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.priority = priority;
        this.weatherPreference = weatherPreference;
        this.dateList = dateList;
        this.placeType = placeType;
        this.cardViewClickListener = cardViewClickListener;
    }

    public void clearAll(){
        locationId.clear();
        locationName.clear();
        latitude.clear();
        longitude.clear();
        priority.clear();
        weatherPreference.clear();
        placeType.clear();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view_row, parent, false);
        return new CustomViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//        holder.txtLocationId.setText(String.valueOf(locationId.get(position)));
        holder.txtLocationName.setText(String.valueOf(locationName.get(position)));
        holder.txtWeatherPreference.setText(Html.fromHtml("<b>Weather conditions: </b> " +  weatherPreference.get(position)));
        holder.txtPriority.setText(Html.fromHtml("<b>Time:</b> " + dateList.get(position)));

        holder.itemView.setOnClickListener(view -> {

            cardViewClickListener.onItemClick(locationId.get(position), position);

                });
    }

    @Override
    public int getItemCount() {
        return locationId.size();
    }

    public interface CardViewClickListener{

        void onItemClick(int id, int position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txtLocationId;
        TextView txtLocationName;
        TextView txtWeatherPreference;
        TextView txtPriority;

        public CustomViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            txtLocationId = itemView.findViewById(R.id.txtLocationId);
            txtLocationName = itemView.findViewById(R.id.txtLocation);
            txtWeatherPreference = itemView.findViewById(R.id.txtWeatherPreference);
            txtPriority = itemView.findViewById(R.id.txtPriority);
        }
    }



}
