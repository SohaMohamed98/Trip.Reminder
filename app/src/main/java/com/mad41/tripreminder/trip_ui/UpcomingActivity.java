package com.mad41.tripreminder.trip_ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mad41.tripreminder.R;

import java.util.ArrayList;

public class UpcomingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TripAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton btn_add;

    String name, date, time, start, end;
    Boolean status, round;

    ArrayList<TripModel> tripModelArrayList;

    int count=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        tripModelArrayList = new ArrayList<>();


        btn_add = (FloatingActionButton) findViewById(R.id.btnf_add);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btn_add_trip();
        setRecyclerView();

    }

    void btn_add_trip() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAddTrip = new Intent(UpcomingActivity.this, AddTrip.class);
                startActivityForResult(intentToAddTrip,100);
                //count++;

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {
            if(resultCode==RESULT_OK)
            {
                name = data.getStringExtra("name");
                start = data.getStringExtra("start");
                end = data.getStringExtra("end");
                time = data.getStringExtra("time");
                date = data.getStringExtra("date");
                tripModelArrayList.add(new TripModel(name, start, end, time, date,1, true, true));
                adapter.notifyItemInserted(count++);

            }

        }
    }

    void setRecyclerView(){

        recyclerView.setHasFixedSize(true);
        adapter = new TripAdapter(tripModelArrayList);
        layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}