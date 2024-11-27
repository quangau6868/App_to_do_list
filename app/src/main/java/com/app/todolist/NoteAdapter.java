package com.app.todolist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.todolist.activity.NoteDetailActivity;
import com.app.todolist.model.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes; // Danh sách ghi chú
    private Context context; // Ngữ cảnh của ứng dụng

    // Constructor
    public NoteAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    // Tạo ViewHolder cho mỗi item
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    // Liên kết dữ liệu với ViewHolder
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position); // Lấy ghi chú tại vị trí hiện tại
        holder.titleTextView.setText(note.getTitle()); // Gán tiêu đề
        holder.contentTextView.setText(note.getContent()); // Gán nội dung

        // Xử lý sự kiện click vào một item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteDetailActivity.class); // Chuyển đến màn hình chi tiết ghi chú
            intent.putExtra("noteId", note.getId()); // Truyền ID của ghi chú qua Intent
            context.startActivity(intent);
        });
    }

    // Trả về số lượng ghi chú
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // Cập nhật danh sách ghi chú và làm mới giao diện
    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes; // Gán danh sách mới
        notifyDataSetChanged(); // Làm mới RecyclerView
    }

    // Lớp ViewHolder: Gán các thành phần của item
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView; // TextView để hiển thị tiêu đề và nội dung

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteTitleTextView); // Liên kết với TextView tiêu đề
            contentTextView = itemView.findViewById(R.id.noteContentTextView); // Liên kết với TextView nội dung
        }
    }
}
