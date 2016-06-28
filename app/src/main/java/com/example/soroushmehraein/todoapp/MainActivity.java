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
    public static final int RESULT_OK = 200;
    public static final int REQUEST_CODE = 200;

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
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra(Todo.serializedName, updatingTodo);
                        // I really don't like just passing this between the activities.
                        // There has to be a way to pass this straight into onActivityResult
                        i.putExtra("Position", position);
                        startActivityForResult(i, REQUEST_CODE); // brings up the second activity
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Todo todo = (Todo) data.getSerializableExtra(Todo.serializedName);
            int position = data.getIntExtra("Position", 0);
            TodoDatabaseHelper db = TodoDatabaseHelper.getInstance(this);
            db.updateTodo(todo);
            items.set(position, todo);
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
