package com.example.soroushmehraein.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
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
            writeItems();
            etNewItem.setText("");
        }
    }

    private void writeItems() {
    }
}
