package com.example.coursework.unused;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.R;

import java.util.ArrayList;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.FilterViewHolder> {

    Context context;
    ArrayList placeType;
    private FilterRecyclerViewAdapter.FilterViewClickListener filterViewClickListener;


    public FilterRecyclerViewAdapter(FilterRecyclerViewAdapter.FilterViewClickListener filterViewClickListener, Context context, ArrayList placeType){
        this.filterViewClickListener = filterViewClickListener;
        this.context = context;
        this.placeType = placeType;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_row, parent, false);
        return new FilterViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {

        holder.txtLocationType.setText(String.valueOf(placeType.get(position)));

        holder.itemView.setOnClickListener(view -> {
            filterViewClickListener.onItemClick(placeType.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return placeType.size();
    }

    public interface FilterViewClickListener{
        void onItemClick(Object id);

    }



    public class FilterViewHolder extends RecyclerView.ViewHolder {

        TextView txtLocationId;
        TextView txtLocationType;
        TextView txtLocationAddress;

        public FilterViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            txtLocationId = itemView.findViewById(R.id.txtLocationId);
            txtLocationType = itemView.findViewById(R.id.txtLocation);
            txtLocationAddress = itemView.findViewById(R.id.txtWeatherPreference);
        }
    }



}