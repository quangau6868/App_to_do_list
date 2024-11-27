package com.app.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.todolist.R;
import com.app.todolist.database.DBHelper;
import com.app.todolist.model.Note;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private Button saveButton, deleteButton, shareButton, boldButton, italicButton, underlineButton;
    private DBHelper dbHelper;
    private int noteId = -1; // ID của ghi chú (mặc định là -1 nếu tạo mới)
    private String currentFormat = ""; // Biến để lưu trạng thái định dạng hiện tại

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // Liên kết các thành phần giao diện
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        shareButton = findViewById(R.id.shareButton);
        boldButton = findViewById(R.id.boldButton);
        italicButton = findViewById(R.id.italicButton);
        underlineButton = findViewById(R.id.underlineButton);

        dbHelper = new DBHelper(this);

        // Lấy noteId từ Intent nếu có
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("noteId")) {
            noteId = intent.getIntExtra("noteId", -1); // Lấy ID của ghi chú
        }

        // Nếu có noteId, lấy thông tin ghi chú từ database
        if (noteId != -1) {
            Note note = dbHelper.getNoteById(noteId);
            if (note != null) {
                titleEditText.setText(note.getTitle());
                contentEditText.setText(note.getContent());
            }
        } else {
            // Nếu không có ID, ẩn nút "Delete" (ghi chú mới)
            deleteButton.setVisibility(View.GONE);
        }

        // Xử lý sự kiện khi nhấn nút "Save"
        saveButton.setOnClickListener(v -> saveNote());

        // Xử lý sự kiện khi nhấn nút "Delete"
        deleteButton.setOnClickListener(v -> deleteNote());

        // Xử lý sự kiện khi nhấn nút "Share"
        shareButton.setOnClickListener(v -> shareNote());

        // Xử lý sự kiện khi nhấn nút "Bold"
        boldButton.setOnClickListener(v -> {
            setCurrentFormat("bold");
            formatTextOnTyping();  // Gọi trực tiếp hàm format
        });

        // Xử lý sự kiện khi nhấn nút "Italic"
        italicButton.setOnClickListener(v -> {
            setCurrentFormat("italic");
            formatTextOnTyping();  // Gọi trực tiếp hàm format
        });

        // Xử lý sự kiện khi nhấn nút "Underline"
        underlineButton.setOnClickListener(v -> {
            setCurrentFormat("underline");
            formatTextOnTyping();  // Gọi trực tiếp hàm format
        });

    }

    // Lưu ghi chú vào database
    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ tiêu đề và nội dung!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteId == -1) {
            // Thêm ghi chú mới
            Note newNote = new Note();
            newNote.setTitle(title);
            newNote.setContent(content);

            long result = dbHelper.insertNote(newNote);
            if (result > 0) {
                Toast.makeText(this, "Ghi chú đã được thêm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không thể thêm ghi chú!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Cập nhật ghi chú hiện tại
            Note updatedNote = new Note(noteId, title, content);
            int result = dbHelper.updateNote(updatedNote);
            if (result > 0) {
                Toast.makeText(this, "Ghi chú đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không thể cập nhật ghi chú!", Toast.LENGTH_SHORT).show();
            }
        }

        finish(); // Quay lại màn hình trước
    }

    // Xóa ghi chú khỏi database
    private void deleteNote() {
        if (noteId != -1) {
            int result = dbHelper.deleteNote(noteId);
            if (result > 0) {
                Toast.makeText(this, "Ghi chú đã được xóa thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không thể xóa ghi chú!", Toast.LENGTH_SHORT).show();
            }
        }
        finish(); // Quay lại màn hình trước
    }

    // Chia sẻ ghi chú
    private void shareNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Ghi chú trống, không thể chia sẻ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Intent chia sẻ
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title); // Chia sẻ tiêu đề
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);  // Chia sẻ nội dung

        // Mở hộp thoại chia sẻ
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
    }

    // Cập nhật định dạng hiện tại khi người dùng nhấn vào một nút định dạng
    private void setCurrentFormat(String formatType) {
        currentFormat = formatType;
        Log.d("Format", "Current Format: " + currentFormat);  // Kiểm tra định dạng hiện tại
    }

    // Hàm xử lý định dạng văn bản khi người dùng gõ
    private void formatTextOnTyping() {
        Editable editableText = contentEditText.getText();
        int start = contentEditText.getSelectionStart();
        int end = contentEditText.getSelectionEnd();
        Log.d("TextSelection", "Start: " + start + ", End: " + end); // Kiểm tra vị trí của selection

        // Kiểm tra xem start và end có hợp lệ không
        if (start == end) {
            // Nếu không có văn bản nào được chọn, áp dụng định dạng cho ký tự kế tiếp
            end = start + 1;
        }

        // Đảm bảo rằng các chỉ số start và end không vượt quá độ dài của văn bản
        if (start >= 0 && end <= editableText.length()) {
            // Áp dụng định dạng đã chọn nếu phạm vi hợp lệ
            if (currentFormat.equals("bold")) {
                editableText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (currentFormat.equals("italic")) {
                editableText.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (currentFormat.equals("underline")) {
                editableText.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
