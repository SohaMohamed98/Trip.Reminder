package com.mad41.tripreminder;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.view_model.TripViewModel;
import com.mad41.tripreminder.trip_ui.NoteReviewDialogue;
import com.mad41.tripreminder.trip_ui.TripAdapter;

import java.util.List;

public class OnGoingFrag extends Fragment {
    private static List<Trip> tripModelArrayList;
    Context context;
    RecyclerView recyclerView;
    TripAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    onGoingCommunicator onGoingCommunicator1;
    private FloatingActionButton btn_add;
    private TripViewModel tripViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onGoingCommunicator1 = (onGoingCommunicator) getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_on_going2, container, false);
        btn_add = fragment.findViewById(R.id.btnf_add);
        recyclerView = (RecyclerView) fragment.findViewById(R.id.HistoryRecyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new TripAdapter();
        layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        tripViewModel = ViewModelProviders.of(requireActivity()).get(TripViewModel.class);
        tripViewModel.getUpcomingNotes().observe(requireActivity(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                tripModelArrayList = trips;
                adapter.setList(trips);
            }
        });

        adapter.setOnNoteClickListener(new TripAdapter.NoteReview() {
            @Override
            public void onNoteClick(View view, int id) {
                Trip trip = null;
                for (int i = 0; i < tripModelArrayList.size(); i++) {
                    if (tripModelArrayList.get(i).getId() == id) {
                        trip = tripModelArrayList.get(i);
                    }
                }
                NoteReviewDialogue noteReviewDialogue = new NoteReviewDialogue(trip);
                noteReviewDialogue.show(getActivity().getSupportFragmentManager(), "frag");

            }
        });

        adapter.setOnItemClickListener(new TripAdapter.OnMenuClickListener() {
            @Override

            public void onItemClick(View view, int id) {

                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.btnEditTrip:
                                editTrip(id);
                                Toast.makeText(view.getContext(), "item: " + id + " trip ", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.btnEditNotes:
                                Toast.makeText(view.getContext(), "item: " + item + " trip ", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.btnCancel:
                                tripViewModel.updateStatus(id, Constants.TRIP_CANCELED);
                                Toast.makeText(view.getContext(), "item: " + item + " trip ", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.btnDelete:
                                tripViewModel.deleteTripById(id);
                                Toast.makeText(view.getContext(), "item: " + item + " trip ", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }

        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoingCommunicator1.startAddTripFragment(null);

            }
        });


        return fragment;
    }

    private void editTrip(int id) {
        Trip trip = null;
        for (int i = 0; i < tripModelArrayList.size(); i++) {
            if (tripModelArrayList.get(i).getId() == id) {
                trip = tripModelArrayList.get(i);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("trip", trip);
        onGoingCommunicator1.startAddTripFragment(bundle);

    }


    public interface onGoingCommunicator {
        void startAddTripFragment(Bundle bundle);
    }
}