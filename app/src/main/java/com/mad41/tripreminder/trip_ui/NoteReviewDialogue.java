package com.mad41.tripreminder.trip_ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad41.tripreminder.AddTripFragments;
import com.mad41.tripreminder.R;
import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.view_model.TripViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteReviewDialogue extends DialogFragment {

    Context context;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> myNote;
    AddTripFragments addTripFragments;
    List<Trip> tripModelArrayList;
    private MyRoomDataBase dataBaseInstance;
    private TripViewModel tripViewModel;

    public NoteReviewDialogue() {
        // Required empty public constructor
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_note_review_dialogue, null);
       addTripFragments= new AddTripFragments();
        myNote= new ArrayList<String>();
       /* myNote.add("Soha");
        myNote.add("Marwa");
        myNote.add("Moataz");
        myNote.add("Mahmoud");
        myNote.add("Soha");
        myNote.add("Marwa");
        myNote.add("Moataz");
        myNote.add("Mahmoud");*/


        dataBaseInstance = MyRoomDataBase.getUserDataBaseInstance(getContext().getApplicationContext());

        if(getArguments()!=null){
          myNote = getArguments().getStringArrayList("note");
        }

        recyclerView = view.findViewById(R.id.recyclerView_note_review);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        noteAdapter = new NoteAdapter(getContext(), myNote);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);
        tripViewModel = ViewModelProviders.of(requireActivity()).get(TripViewModel.class);
        tripViewModel.getAllNotes().observe(requireActivity(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {

                tripModelArrayList = trips;
                noteAdapter.setList(trips);
            }
        });


        builder.setView(view)
                .setTitle("My Notes")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }



}