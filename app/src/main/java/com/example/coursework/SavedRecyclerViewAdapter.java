package com.example.coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.CustomViewHolder> {

    Context context;
    ArrayList locationId, locationName, latitude, longitude, timePreference, weatherPreference;

    public SavedRecyclerViewAdapter(Context context, ArrayList locationId, ArrayList locationName, ArrayList latitude,
                                    ArrayList longitude, ArrayList timePreference, ArrayList weatherPreference){
        this.context = context;
        this.locationId = locationId;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timePreference = timePreference;
        this.weatherPreference = weatherPreference;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.txtLocationId.setText(String.valueOf(locationId.get(position)));
        holder.txtLocationName.setText(String.valueOf(locationName.get(position)));
        holder.txtLocationAddress.setText("Test");
    }

    @Override
    public int getItemCount() {
        return locationId.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txtLocationId;
        TextView txtLocationName;
        TextView txtLocationAddress;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLocationId = itemView.findViewById(R.id.txtLocationId);
            txtLocationName = itemView.findViewById(R.id.txtLocationName);
            txtLocationAddress = itemView.findViewById(R.id.txtAddress);
        }
    }

}
