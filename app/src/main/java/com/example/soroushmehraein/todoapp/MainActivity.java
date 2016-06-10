package com.example.soroushmehraein.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Todo> items;
    ArrayAdapter<Todo> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupViewListener();
    }

    private void readItems() {
        TodoDatabaseHelper db = TodoDatabaseHelper.getInstance(this);
        items = (ArrayList<Todo>) db.getAllTodos();
    }

    private void setupViewListener() {
        final Context savedContext = this;
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Todo removedTodo = items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        TodoDatabaseHelper db = TodoDatabaseHelper.getInstance(savedContext);
                        db.deleteTodo(removedTodo);
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Todo updatingTodo = items.get(position);
                        launchEditView(updatingTodo);
                    }
                }
        );
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        assert etNewItem != null;
        String itemText = etNewItem.getText().toString();
        if (!itemText.isEmpty()) {
            int itemPosition = items.size();
            Todo newTodo = new Todo(itemText, itemPosition);
            itemsAdapter.add(newTodo);
            TodoDatabaseHelper db = TodoDatabaseHelper.getInstance(this);
            db.addTodo(newTodo);
            etNewItem.setText("");
        }
    }

    public void launchEditView(Todo todo) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra("Todo", todo);
        startActivity(i); // brings up the second activity
    }
}
