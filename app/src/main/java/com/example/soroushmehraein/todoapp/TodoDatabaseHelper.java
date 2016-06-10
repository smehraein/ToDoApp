package com.example.soroushmehraein.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soroushmehraein on 6/9/16.
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "TodoDatabaseHelper";
    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_TODOS = "todos";

    // Todo Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_TITLE = "title";

    // Singleton Instance
    private static TodoDatabaseHelper ourInstance;

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODOS +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TODO_TITLE + " TEXT" +
                ")";

        db.execSQL(CREATE_TODOS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(db);
        }
    }

    public static synchronized TodoDatabaseHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new TodoDatabaseHelper(context.getApplicationContext());
        }
        return ourInstance;
    }

    private TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addTodo(Todo todo) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_TITLE, todo.title);
            db.insertOrThrow(TABLE_TODOS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add todo to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();

        String TODOS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_TODOS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_TODO_ID));
                    String title = cursor.getString(cursor.getColumnIndex(KEY_TODO_TITLE));
                    Todo newTodo = new Todo(id, title);
                    todos.add(newTodo);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get todos from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todos;
    }

    public int updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_TITLE, todo.title);

        return db.update(TABLE_TODOS, values, KEY_TODO_ID + " = ?",
                new String[] { String.valueOf(todo.id) });
    }

    public int deleteTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_TODOS, KEY_TODO_ID + " = ?",
                new String[] { String.valueOf(todo.id) });
    }
}
