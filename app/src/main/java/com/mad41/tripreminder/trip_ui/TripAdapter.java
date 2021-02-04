package com.mad41.tripreminder.trip_ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.mad41.tripreminder.MainScreen;
import com.mad41.tripreminder.R;
import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<com.mad41.tripreminder.trip_ui.TripAdapter.ExampleViewHolder> {
    private List<Trip> tripModels;
    private final Context context;

    public TripAdapter(Context context,List<Trip> exampleList) {
        this.context = context;
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
        Trip currentItem = tripModels.get(position);
        holder.txt_date.setText(currentItem.getDate());
        holder.txt_time.setText(currentItem.getTime());
        holder.txt_start.setText(currentItem.getStartLoacation());
        holder.txt_end.setText(currentItem.getEndLoacation());
        holder.txt_state.setText(String.valueOf(currentItem.getStatus()));
        holder.txt_place.setText(currentItem.getName());
        //get the id of the trip and add it to the card (viewHolder)
        holder.id = currentItem.getId();
    }

    @Override
    public int getItemCount() {
        return tripModels.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_date;
        public TextView txt_time;
        public TextView txt_place;
        public TextView txt_state;
        public TextView txt_start;
        public TextView txt_end;
        public int id;
        ImageView btn_menu_card;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date2);
            txt_time = itemView.findViewById(R.id.txt_time2);
            txt_place = itemView.findViewById(R.id.txt_tripName2);
            txt_start = itemView.findViewById(R.id.txt_start2);
            txt_state = itemView.findViewById(R.id.txt_state);
            txt_end = itemView.findViewById(R.id.txt_end2);

            btn_menu_card = itemView.findViewById(R.id.btn_menu_card);
            btn_menu_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view, id);
                }
            });
        }

        public void showPopupMenu(View view,int id){
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.btnEditTrip:
                            Toast.makeText(view.getContext(), "item: "+id+" trip "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.btnEditNotes:
                            Toast.makeText(view.getContext(), "item: "+item+" trip "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.btnCancel:
                            updateDatabase(0,id);
                            Toast.makeText(view.getContext(), "item: "+item+" trip "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.btnDelete:
                            updateDatabase(1,id);
                            Toast.makeText(view.getContext(), "item: "+item+" trip "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }

        private void updateDatabase(int i,int id) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyRoomDataBase myRoomDataBase = MyRoomDataBase.getUserDataBaseInstance(itemView.getContext());
                    if(i==0){myRoomDataBase.tripDao().updateStatus(id, Constants.TRIP_CANCELED);}else{myRoomDataBase.tripDao().deletTripById(id);}
                }
            }).start();
            //cancel alarm
        }
    }
}

