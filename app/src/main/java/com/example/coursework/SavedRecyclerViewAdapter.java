package com.example.coursework;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.CustomViewHolder> {

    Context context;
    ArrayList<Integer> locationId, priority;
    ArrayList<String>  locationName, weatherPreference, placeType;
    ArrayList<Double> latitude, longitude;
    private CardViewClickListener cardViewClickListener;

    public SavedRecyclerViewAdapter(CardViewClickListener cardViewClickListener, Context context, ArrayList<Integer> locationId, ArrayList<String> locationName,
                                    ArrayList<Double> latitude, ArrayList<Double> longitude, ArrayList<Integer> priority, ArrayList<String> weatherPreference, ArrayList<String> placeType){
        this.cardViewClickListener = cardViewClickListener;
        this.context = context;
        this.locationId = locationId;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.priority = priority;
        this.weatherPreference = weatherPreference;
        this.placeType = placeType;
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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_row, parent, false);
        return new CustomViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//        holder.txtLocationId.setText(String.valueOf(locationId.get(position)));
        holder.txtLocationName.setText(String.valueOf(locationName.get(position)));
        holder.txtWeatherPreference.setText(Html.fromHtml("<b>Weather preference:</b> " + String.valueOf(weatherPreference.get(position))));

        if(priority.get(position) == 1){
            holder.txtPriority.setText(Html.fromHtml("<b>Priority:</b> low"));
        }
        else if(priority.get(position) == 2 ){
            holder.txtPriority.setText(Html.fromHtml("<b>Priority:</b> medium"));
        }
        else if(priority.get(position) == 3){
            holder.txtPriority.setText(Html.fromHtml("<b>Priority:</b> high"));
        }



        holder.itemView.setOnClickListener(view -> {

            cardViewClickListener.onItemClick(locationId.get(position));

                });
    }

    @Override
    public int getItemCount() {
        return locationId.size();
    }

    public interface CardViewClickListener{

        void onItemClick(int id);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

//        TextView txtLocationId;
        TextView txtLocationName;
        TextView txtWeatherPreference;
        TextView txtPriority;

        public CustomViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
//            txtLocationId = itemView.findViewById(R.id.txtLocationId);
            txtLocationName = itemView.findViewById(R.id.txtLocation);
            txtWeatherPreference = itemView.findViewById(R.id.txtWeatherPreference);
            txtPriority = itemView.findViewById(R.id.txtPriority);
        }
    }



}
