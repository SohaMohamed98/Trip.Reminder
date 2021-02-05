package com.mad41.tripreminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.trip_ui.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<Trip> tripModelArrayList;
    Handler handler;
    FragmentManager mgr = getFragmentManager();

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
        tripModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) fragment.findViewById(R.id.HistoryRecycleView);

        recyclerView.setHasFixedSize(true);
        tripModelArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                tripModelArrayList = (List<Trip>) msg.obj;
                adapter = new HistoryAdapter(tripModelArrayList , mgr);
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
                    }
                };
                new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);


            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                tripModelArrayList = MyRoomDataBase.getUserDataBaseInstance(getContext().getApplicationContext()).tripDao().getUpcomingTrips();
                Message msg =new Message();
                msg.obj = tripModelArrayList;
                handler.sendMessage(msg);
            }
        }).start();


        return fragment;
    }
    public  void changeFragment()
    {
        HistoryNotes HN = new HistoryNotes();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.HNotes, HN); 

        fragmentTransaction.commit();

    }


}