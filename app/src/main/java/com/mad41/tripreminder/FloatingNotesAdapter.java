package com.mad41.tripreminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FloatingNotesAdapter extends RecyclerView.Adapter<FloatingNotesAdapter.ExampleViewHolder> {

    ArrayList<String> notes;
    public FloatingNotesAdapter(ArrayList<String> notes) {
        this.notes=notes;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        String note=notes.get(position);
        holder.txt_note.setText(note);
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView txt_note;



        public ExampleViewHolder(View itemView) {
            super(itemView);
          txt_note=itemView.findViewById(R.id.tvContent);
          checkBox=itemView.findViewById(R.id.cbSelect);
        }
    }

}
