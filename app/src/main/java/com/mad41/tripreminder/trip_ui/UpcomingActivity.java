package com.mad41.tripreminder.trip_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mad41.tripreminder.R;

import java.util.ArrayList;

public class UpcomingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TripAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<TripModel> tripModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        tripModelArrayList = new ArrayList<>();
        tripModelArrayList.add(new TripModel("Line 1", "Line 2","Line","Line","Line",1, true, true));
        tripModelArrayList.add(new TripModel("Line 1", "Line 2","Line","Line","Line",1, true, true));
        tripModelArrayList.add(new TripModel("Line 1", "Line 2","Line","Line","Line",1, true, true));
        tripModelArrayList.add(new TripModel("Line 1", "Line 2","Line","Line","Line",1, true, true));


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        adapter = new TripAdapter(tripModelArrayList);
        layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}