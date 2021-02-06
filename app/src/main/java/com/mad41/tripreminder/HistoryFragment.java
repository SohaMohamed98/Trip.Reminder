package com.mad41.tripreminder;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.view_model.TripViewModel;
import com.mad41.tripreminder.trip_ui.HistoryAdapter;


import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    private TripViewModel tripViewModel;
    RecyclerView.LayoutManager layoutManager;
    List<Trip> tripModelArrayList;
    historyListner listner;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment =  inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = (RecyclerView) fragment.findViewById(R.id.HistoryRecyclerView);
        tripModelArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        tripViewModel = ViewModelProviders.of(requireActivity()).get(TripViewModel.class);
        tripViewModel.getHistoryNotes().observe(requireActivity(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                tripModelArrayList=trips;
                listner = new historyListner() {
                    @Override
                    public void showNotes(List<Trip> notes) {

                    }
                };
                adapter = new HistoryAdapter(tripModelArrayList , listner);
                recyclerView.setAdapter(adapter);
                ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        tripModelArrayList.remove(viewHolder.getAdapterPosition());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext().getApplicationContext(), "Trip is Deleted From Hisrory", Toast.LENGTH_LONG).show();
                    }
                };
                new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

            }
        });

        return fragment;
    }
}