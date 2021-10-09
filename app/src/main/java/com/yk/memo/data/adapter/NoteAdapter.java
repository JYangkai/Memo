package com.yk.memo.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.memo.R;
import com.yk.memo.data.bean.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;

    private final List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvNoteContent.setText(note.getSpanStrBuilder());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvNoteContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteContent = itemView.findViewById(R.id.tvNoteContent);
        }
    }
}
