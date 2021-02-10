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

import android.util.Log;
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

    Trip trip;

    public NoteReviewDialogue(Trip trip) {

        this.trip = trip;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_note_review_dialogue, null);
        addTripFragments = new AddTripFragments();

        recyclerView = view.findViewById(R.id.recyclerView_note_review);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        noteAdapter = new NoteAdapter(context, trip.getNotes());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);

        builder.setView(view)
                .setTitle(R.string.myNotes)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }


}