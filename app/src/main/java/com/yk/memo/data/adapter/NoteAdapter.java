package com.yk.memo.data.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.markdown.Markdown;
import com.yk.memo.R;
import com.yk.memo.data.bean.Note;
import com.yk.memo.ui.view.NoteCardView;
import com.yk.memo.utils.FileUtils;
import com.yk.memo.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {
    private Context context;

    private final List<Note> noteList;
    private final List<Note> filterList = new ArrayList<>();

    private boolean isMoreSelectMode = false;

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
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener != null) {
                    onItemListener.onItemMoreClick(v, filterList.get(holder.getAdapterPosition()));
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = filterList.get(position);
        Markdown.with(context).load(note.getSrc()).into(holder.tvNoteContent);
        holder.tvTime.setText(TimeUtils.getSmartTime(note.getUpdateTime()));
        holder.noteCardView.select(note.isSelect());
        holder.noteCardView.outputMarkdown(FileUtils.isOutputMarkdown(context, note));
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

    /**
     * 置顶note
     *
     * @param note note
     */
    public void topNote(Note note) {
        if (noteList.contains(note)) {
            Collections.swap(noteList, 0, noteList.indexOf(note));
        }

        if (filterList.contains(note)) {
            Collections.swap(filterList, 0, filterList.indexOf(note));
        }

        notifyDataSetChanged();
    }

    /**
     * 查找note（通过id）
     *
     * @param id id
     * @return note
     */
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

    /**
     * 移除note并刷新
     *
     * @param note note
     */
    public void refreshDataRemove(Note note) {
        if (note == null) {
            return;
        }

        noteList.remove(note);
        filterList.remove(note);

        notifyDataSetChanged();
    }

    /**
     * 移除note list并刷新
     *
     * @param noteList note list
     */
    public void refreshDataRemove(List<Note> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return;
        }

        for (Note note : noteList) {
            refreshDataRemove(note);
        }
    }

    /**
     * 添加note并刷新
     *
     * @param note    note
     * @param needTop 需要置顶
     */
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
                notifyItemInserted(0);
            } else {
                filterList.add(note);
                notifyItemInserted(filterList.size() - 1);
            }
        }
    }

    /**
     * 刷新note list并刷新
     *
     * @param noteList note list
     */
    public void refreshDataAdd(List<Note> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return;
        }

        for (Note note : noteList) {
            refreshDataAdd(note, false);
        }
    }

    /**
     * 刷新note
     *
     * @param note note
     */
    public void refreshDataChange(Note note) {
        notifyItemChanged(filterList.indexOf(note));
    }

    /**
     * 选择/取消note
     *
     * @param note note
     */
    public void selectNote(Note note) {
        note.setSelect(!note.isSelect());
        refreshDataChange(note);
    }

    /**
     * 判断多选模式
     *
     * @return 是否多选模式
     */
    public boolean isMoreSelectMode() {
        return isMoreSelectMode;
    }

    /**
     * 设置多选模式
     *
     * @param moreSelectMode 是否多选模式
     */
    public void setMoreSelectMode(boolean moreSelectMode) {
        isMoreSelectMode = moreSelectMode;

        if (!isMoreSelectMode) {
            for (Note note : noteList) {
                if (!note.isSelect()) {
                    continue;
                }
                note.setSelect(false);
                if (filterList.contains(note)) {
                    refreshDataChange(note);
                }
            }
        }
    }

    /**
     * 获取选中的note list
     *
     * @return note list
     */
    public List<Note> getSelectNoteList() {
        List<Note> selectNoteList = new ArrayList<>();
        for (Note note : filterList) {
            if (!note.isSelect()) {
                continue;
            }
            selectNoteList.add(note);
        }
        return selectNoteList;
    }

    /**
     * view holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 自定义CardView（带选择标识）
         */
        NoteCardView noteCardView;

        /**
         * 文本显示
         */
        AppCompatTextView tvNoteContent;

        /**
         * 时间
         */
        AppCompatTextView tvTime;

        /**
         * 更多按钮
         */
        AppCompatImageView ivMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteCardView = itemView.findViewById(R.id.noteCardView);
            tvNoteContent = itemView.findViewById(R.id.tvNoteContent);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivMore = itemView.findViewById(R.id.ivMore);
        }
    }

    /**
     * item事件 监听
     */
    private OnItemListener onItemListener;

    /**
     * 设置item事件监听
     *
     * @param onItemListener onItemListener
     */
    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        /**
         * 单击
         *
         * @param view view
         * @param note note
         */
        void onItemClick(View view, Note note);

        /**
         * 长按
         *
         * @param view view
         * @param note note
         */
        void onItemLongClick(View view, Note note);

        /**
         * 更多
         *
         * @param view view
         * @param note note
         */
        void onItemMoreClick(View view, Note note);
    }
}
