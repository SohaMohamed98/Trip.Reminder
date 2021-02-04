package com.mad41.tripreminder;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.trip_ui.TripAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    TripAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    String name, start, end, time, date;
    Boolean status, round;

    OnGoingFrag.onGoingCommunicator onGoingCommunicator1;
    List<Trip> tripModelArrayList;

    int count = 0;

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
        MyHandler mh = new MyHandler();
        tripModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) fragment.findViewById(R.id.recyclerView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                tripModelArrayList = MyRoomDataBase.getUserDataBaseInstance(getContext().getApplicationContext()).tripDao().getHistoryTrips();
                mh.sendEmptyMessage(0);
            }
        }).start();


        return fragment;
    }
    void setRecyclerView(){

        recyclerView.setHasFixedSize(true);
        adapter = new TripAdapter((Context) onGoingCommunicator1,tripModelArrayList);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            setRecyclerView();
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onGoingCommunicator1= (OnGoingFrag.onGoingCommunicator) getActivity();
    }
}