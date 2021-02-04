package com.mad41.tripreminder.trip_ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad41.tripreminder.R;

import java.text.BreakIterator;
import java.util.ArrayList;

public class AddNoteAdapter extends RecyclerView.Adapter<AddNoteAdapter.ViewHolder> {

    Context context;
    ArrayList<String> noteList;


    public AddNoteAdapter(Context context, ArrayList<String> noteList){
        this.context=context;
        this.noteList=noteList;
    }

    @NonNull
    @Override
    public AddNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_add_note, parent, false);
        EditText editText =  view.findViewById(R.id.et_note_name);
        editText.setText("");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int i= holder.getLayoutPosition();
        holder.et_note_name.setText(noteList.get(i).toString());

    }

    public ArrayList<String> getNoteList(){
        return  noteList;
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton btn_note_delete;
        public EditText et_note_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_note_delete = itemView.findViewById(R.id.btn_note_delete);
            et_note_name = itemView.findViewById(R.id.et_note_name);

            btn_note_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        noteList.remove(position);
                        notifyItemRemoved(position);
                        et_note_name.setText("");
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });

            et_note_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    noteList.set(getAdapterPosition(), new String(s.toString()));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }


}
