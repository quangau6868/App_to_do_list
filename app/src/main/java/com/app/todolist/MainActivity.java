package com.app.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.todolist.activity.NoteDetailActivity;
import com.app.todolist.database.DBHelper;
import com.app.todolist.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView notesRecyclerView;
    private EditText searchEditText;
    private FloatingActionButton fab;
    private NoteAdapter noteAdapter; // Adapter cho RecyclerView
    private List<Note> noteList; // Danh sách ghi chú
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Tùy chọn làm giao diện tràn toàn màn hình
        setContentView(R.layout.activity_main);

        // Khởi tạo các thành phần
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        fab = findViewById(R.id.fab);

        dbHelper = new DBHelper(this);

        // Lấy danh sách ghi chú từ cơ sở dữ liệu
        noteList = dbHelper.getAllNotes();

        // Thiết lập RecyclerView
        noteAdapter = new NoteAdapter(noteList, this);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(noteAdapter);

        // Xử lý sự kiện nút thêm ghi chú
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
            startActivity(intent); // Chuyển sang màn hình thêm ghi chú mới
        });

        // Xử lý tìm kiếm ghi chú
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString()); // Lọc danh sách ghi chú theo từ khóa
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Làm mới danh sách ghi chú khi quay lại MainActivity
        noteList = dbHelper.getAllNotes();
        noteAdapter.updateNotes(noteList);
    }

    // Phương thức lọc danh sách ghi chú theo từ khóa
    private void filterNotes(String keyword) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : noteList) {
            if (note.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    note.getContent().toLowerCase().contains(keyword.toLowerCase())) {
                filteredNotes.add(note);
            }
        }
        noteAdapter.updateNotes(filteredNotes);
    }
}
