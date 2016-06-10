package com.example.soroushmehraein.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditTodo;
    Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        todo = (Todo) getIntent().getSerializableExtra("Todo");
        etEditTodo = (EditText) findViewById(R.id.etEditTodo);
        etEditTodo.setText(todo.title);
    }
}
