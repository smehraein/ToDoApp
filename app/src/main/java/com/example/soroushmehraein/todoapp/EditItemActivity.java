package com.example.soroushmehraein.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditTodo;
    Todo todo;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditTodo = (EditText) findViewById(R.id.etEditTodo);
        todo = (Todo) getIntent().getSerializableExtra("Todo");
        position = getIntent().getIntExtra("Position", 0);
        etEditTodo.setText(todo.title);
        etEditTodo.setSelection(todo.title.length());
    }

    public void onUpdateTodo(View view) {
        todo.title = etEditTodo.getText().toString();
        Intent data = new Intent();
        data.putExtra("Todo", todo);
        data.putExtra("Position", position);
        setResult(MainActivity.RESULT_OK, data);
        finish();
    }
}
