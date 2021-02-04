package com.mad41.tripreminder.trip_ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad41.tripreminder.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context context;
    ArrayList<String> myNotes;
    RecyclerView.ViewHolder vh;


    public NoteAdapter(Context context, ArrayList<String> myNotes){

        this.context=context;
        this.myNotes=myNotes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_note, parent, false);
        vh =new ViewHolder(v);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_note= (TextView)itemView.findViewById(R.id.txt_note);

        }
    }
}
