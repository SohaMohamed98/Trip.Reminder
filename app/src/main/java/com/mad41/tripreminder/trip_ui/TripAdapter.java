package com.mad41.tripreminder.trip_ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mad41.tripreminder.R;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<com.mad41.tripreminder.trip_ui.TripAdapter.ExampleViewHolder> {
    private ArrayList<TripModel> tripModels;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_date;
        public TextView txt_time;
        public TextView txt_place;
        public TextView txt_state;
        public TextView txt_start;
        public TextView txt_end;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date2);
            txt_time = itemView.findViewById(R.id.txt_time2);
            txt_place = itemView.findViewById(R.id.txt_tripName2);
            txt_start = itemView.findViewById(R.id.txt_start2);
            txt_state = itemView.findViewById(R.id.txt_state);
            txt_end = itemView.findViewById(R.id.txt_end2);
        }
    }

    public TripAdapter(ArrayList<TripModel> exampleList) {
        tripModels = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }


    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        TripModel currentItem = tripModels.get(position);
        holder.txt_date.setText(currentItem.getDate());
        holder.txt_time.setText(currentItem.getTime());
        holder.txt_start.setText(currentItem.getStartLoacation());
        holder.txt_end.setText(currentItem.getEndLoacation());
        holder.txt_state.setText(String.valueOf(currentItem.getStatus()));
        holder.txt_place.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return tripModels.size();
    }
}

