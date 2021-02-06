package com.mad41.tripreminder.trip_ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad41.tripreminder.AddTripFragments;
import com.mad41.tripreminder.R;
import com.mad41.tripreminder.room_database.trip.Trip;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements AddTripFragments.Communicator {

    Context context;
    ArrayList<String> myNotes;
    RecyclerView.ViewHolder vh;
    private List<Trip> tripModels=new ArrayList<>();


    public NoteAdapter(Context context, ArrayList<String> myNotes) {

        this.context = context;
        this.myNotes = myNotes;
    }
    public void setList(List<Trip> trips){
        this.tripModels=trips;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_note, parent, false);
        vh = new ViewHolder(v);

        return (ViewHolder) vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_note.setText(myNotes.get(position));
    }


    @Override
    public int getItemCount() {
        return myNotes.size();
    }

    @Override
    public void respon(long alarmTime, int id, String start, String end, int tripBack, int repeatInterval) {

    }

    @Override
    public void passingNotes(ArrayList<String> myNotes) {

       this.myNotes=myNotes;
        Bundle bundle=new Bundle();
        bundle.putStringArrayList("note",myNotes);
        NoteReviewDialogue noteReviewDialogue=new NoteReviewDialogue();
        noteReviewDialogue.setArguments(bundle);
    }

    @Override
    public void sendArrayListToRecycleView(ArrayList<Trip> arrayList2) {

    }

    @Override
    public void returnToOnGoingActivity() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_note = (TextView) itemView.findViewById(R.id.txt_note);

        }
    }
}
