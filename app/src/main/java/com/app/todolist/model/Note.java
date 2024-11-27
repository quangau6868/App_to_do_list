package com.app.todolist.model;

public class Note {
    private int id;         // ID của ghi chú
    private String title;   // Tiêu đề ghi chú
    private String content; // Nội dung ghi chú

    // Constructor không tham số
    public Note() {}

    // Constructor đầy đủ
    public Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getter và Setter cho các trường
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Phương thức toString (tuỳ chọn, dùng để debug)
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
