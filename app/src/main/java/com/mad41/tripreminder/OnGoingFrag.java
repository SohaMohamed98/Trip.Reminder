package com.mad41.tripreminder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.trip_ui.TripAdapter;
import com.mad41.tripreminder.trip_ui.TripModel;

import java.util.ArrayList;
import java.util.List;

public class OnGoingFrag extends Fragment {
    private FloatingActionButton btn_add;

    RecyclerView recyclerView;
    TripAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    String name, start, end, time, date;
    Boolean status, round;

    onGoingCommunicator onGoingCommunicator1;
    List<Trip> tripModelArrayList;

    int count = 0;

    public OnGoingFrag() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onGoingCommunicator1 = (onGoingCommunicator) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            setRecyclerView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MyHandler h = new MyHandler();
//        tripModelArrayList = new ArrayList<>();
        View fragment = inflater.inflate(R.layout.fragment_on_going2, container, false);
        btn_add = fragment.findViewById(R.id.btnf_add);
        tripModelArrayList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                tripModelArrayList = MyRoomDataBase.getUserDataBaseInstance(getContext().getApplicationContext()).tripDao().getUpcomingTrips();
                h.sendEmptyMessage(0);
            }
        }).start();

        recyclerView = (RecyclerView) fragment.findViewById(R.id.recyclerView);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                onGoingCommunicator1.startAddTripFragment();
//               onGoingCommunicator1.saveArrayList(tripModelArrayList);
            }
        });


        if (getArguments() != null) {
            name = getArguments().getString("name");
            start = getArguments().getString("start");
            end = getArguments().getString("end");
            date = getArguments().getString("date");
            time = getArguments().getString("time");
//            tripModelArrayList.add(new TripModel(name, start, end, time, date, 1, true, true));
            //  adapter.notifyItemInserted(count);


            setRecyclerView();

        }


        return fragment;
    }

    void setRecyclerView() {

        recyclerView.setHasFixedSize(true);
        adapter = new TripAdapter(tripModelArrayList);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    public void getArrayList(ArrayList<Trip> arrayList2) {
//        this.tripModelArrayList=arrayList2;
//        setRecyclerView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //  outState.putStringArrayList("list", (ArrayList<String>) tripModelArrayList);
    }

    public interface onGoingCommunicator {
        void saveArrayList(ArrayList<Trip> arr);

        void startAddTripFragment();
    }
}