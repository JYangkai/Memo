package com.yk.memo.data.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.memo.R;
import com.yk.memo.data.bean.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {
    private Context context;

    private final List<Note> noteList;
    private final List<Note> filterList = new ArrayList<>();

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
        filterList.clear();
        filterList.addAll(noteList);
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
                if (onItemListener != null) {
                    onItemListener.onItemClick(v, filterList.get(holder.getAdapterPosition()));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemListener != null) {
                    onItemListener.onItemLongClick(v, filterList.get(holder.getAdapterPosition()));
                }
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = filterList.get(position);
        holder.tvNoteContent.setText(note.getSpanStrBuilder());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Note> curList = new ArrayList<>();
                if (TextUtils.isEmpty(constraint)) {
                    curList = noteList;
                } else {
                    for (Note note : noteList) {
                        if (!note.getSrc().contains(constraint)) {
                            continue;
                        }
                        curList.add(note);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = curList;
                results.count = curList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Note> curList = (List<Note>) results.values;
                filterList.clear();
                filterList.addAll(curList);
                notifyDataSetChanged();
            }
        };
    }

    public void topNote(Note note) {
        if (noteList.contains(note)) {
            Collections.swap(noteList, 0, noteList.indexOf(note));
        }

        if (filterList.contains(note)) {
            Collections.swap(filterList, 0, filterList.indexOf(note));
        }

        notifyDataSetChanged();
    }

    public Note findNoteForId(long id) {
        Note findNote = null;
        for (Note note : noteList) {
            if (note.getId() != id) {
                continue;
            }
            findNote = note;
            break;
        }
        return findNote;
    }

    public void refreshDataRemove(Note note) {
        if (note == null) {
            return;
        }

        noteList.remove(note);
        filterList.remove(note);

        notifyDataSetChanged();
    }

    public void refreshDataRemove(List<Note> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return;
        }

        for (Note note : noteList) {
            refreshDataRemove(note);
        }
    }

    public void refreshDataAdd(Note note, boolean needTop) {
        if (note == null) {
            return;
        }

        if (!noteList.contains(note)) {
            if (needTop) {
                noteList.add(0, note);
            } else {
                noteList.add(note);
            }
        }

        if (!filterList.contains(note)) {
            if (needTop) {
                filterList.add(0, note);
            } else {
                filterList.add(note);
            }
        }

        notifyDataSetChanged();
    }

    public void refreshDataAdd(List<Note> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return;
        }

        for (Note note : noteList) {
            refreshDataAdd(note, false);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvNoteContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteContent = itemView.findViewById(R.id.tvNoteContent);
        }
    }

    private OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        void onItemClick(View view, Note note);

        void onItemLongClick(View view, Note note);
    }
}
