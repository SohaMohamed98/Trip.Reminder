package com.mad41.tripreminder.trip_ui;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;
import com.mad41.tripreminder.historyListner;
import com.mad41.tripreminder.R;
import com.mad41.tripreminder.room_database.trip.Trip;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<com.mad41.tripreminder.trip_ui.HistoryAdapter.ExampleViewHolder> {
    private List<Trip> tripModels;
    boolean flag = true;
    Animation slideDown , slideUp;
    private Context context;
    private static View view;
    public final historyListner lis;
    private FragmentContainerView contenr;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_date;
        public TextView txt_time;
        public TextView txt_place;
        public TextView txt_state;
        public TextView txt_start;
        public TextView txt_end , to;
        public Button Details , delete;
        public ImageButton Notes;
        public FragmentContainerView contenr;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.date);
            txt_time = itemView.findViewById(R.id.Time);
            txt_place = itemView.findViewById(R.id.tripName);
            txt_start = itemView.findViewById(R.id.start);
            txt_state = itemView.findViewById(R.id.Status);
            txt_end = itemView.findViewById(R.id.End);
            Details = itemView.findViewById(R.id.details);
            delete = itemView.findViewById(R.id.delete);
            to = itemView.findViewById(R.id.to);
            Notes = itemView.findViewById(R.id.DNotes);
            contenr = itemView.findViewById(R.id.dynamicFrag);

            view = itemView;
        }
    }

    public HistoryAdapter(List<Trip> exampleList , historyListner listen  ) {
        tripModels = exampleList;
        lis = listen;

    }

    @Override
    public HistoryAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        HistoryAdapter.ExampleViewHolder evh = new HistoryAdapter.ExampleViewHolder(v);
        slideUp = AnimationUtils.loadAnimation(parent.getContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(parent.getContext(), R.anim.slide_down);
        context = parent.getContext();

        return evh;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ExampleViewHolder holder, int position) {
        Trip currentItem = tripModels.get(position);
        holder.txt_date.setText(currentItem.getDate());
        holder.txt_time.setText(currentItem.getTime());
        holder.txt_start.setText(currentItem.getStartLoacation());
        holder.txt_end.setText(currentItem.getEndLoacation());
        holder.txt_state.setText(String.valueOf(currentItem.getStatus()));
        holder.txt_place.setText(currentItem.getName());


        holder.Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag) {
                    holder.txt_date.setVisibility(View.VISIBLE);
                    holder.txt_date.startAnimation(slideDown);
                    holder.txt_time.setVisibility(View.VISIBLE);
                    holder.txt_time.startAnimation(slideDown);
                    holder.txt_start.setVisibility(View.VISIBLE);
                    holder.txt_start.startAnimation(slideDown);
                    holder.txt_end.setVisibility(View.VISIBLE);
                    holder.txt_end.startAnimation(slideDown);
                    holder.to.setVisibility(View.VISIBLE);
                    holder.Details.setText("Hide Details");
                    holder.Details.startAnimation(slideDown);
                    holder.to.startAnimation(slideDown);

                    flag = false;
                }
                else{
                    holder.txt_date.setVisibility(View.GONE);
                    holder.txt_time.setVisibility(View.GONE);
                    holder.txt_start.setVisibility(View.GONE);
                    holder.txt_end.setVisibility(View.GONE);
                    holder.to.setVisibility(View.GONE);
                    holder.Details.setText("Show Details");
                    flag = true;
                }

            }
        });
        tripModels.add(1,new Trip("marwa",
                "yuy","yruyuy","yhfhk","yihfh",1,true,true));
        holder.Notes.setOnClickListener(v -> lis.showNotes(tripModels));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                tripModels.remove(tripModels.get(position));
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripModels.size();
    }

}
