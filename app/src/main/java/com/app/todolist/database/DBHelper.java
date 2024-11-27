package com.app.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.app.todolist.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String db_Name = "App Notes";
    private static final int db_Version = 1;

    private static final String TB_USER = "USER";
    private static final String TB_USER_ID = "Id";
    private static final String TB_USER_USERNAME = "Username";
    private static final String TB_USER_EMAIL = "Email";
    private static final String TB_USER_PASSWORD = "Password";

    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    public DBHelper( Context context) {
        super(context, db_Name, null, db_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tbUser = " CREATE TABLE " +TB_USER+ " ( " + TB_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_USER_USERNAME +" TEXT, " +TB_USER_EMAIL+ " TEXT UNIQUE, "+TB_USER_PASSWORD+" TEXT )";
        sqLiteDatabase.execSQL(tbUser);

        String createTable = "CREATE TABLE " + TABLE_NOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_CONTENT + " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int versionOld, int versionNew) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TB_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(sqLiteDatabase);
    }

    // Lấy tất cả ghi chú
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                );
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    // Thêm ghi chú
    public long insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());

        // Thêm ghi chú vào bảng
        return db.insert(TABLE_NOTES, null, values);
    }

    // Cập nhật ghi chú
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());

        // Cập nhật ghi chú theo ID
        return db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    // Xóa ghi chú
    public int deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Xóa ghi chú theo ID
        return db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(noteId)});
    }

    // Lấy ghi chú theo ID
    public Note getNoteById(int noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT},
                COLUMN_ID + " = ?", new String[]{String.valueOf(noteId)}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Note note = new Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                );
                cursor.close();
                return note;
            }
            cursor.close();
        }
        return null; // Trả về null nếu không tìm thấy
    }
}

