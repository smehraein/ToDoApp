package com.example.soroushmehraein.todoapp;

import java.io.Serializable;

import static java.util.UUID.randomUUID;

/**
 * Created by soroushmehraein on 6/9/16.
 */
public class Todo implements Serializable {
    public String title;
    public int position;

    private String id;

    public Todo(String todoTitle, int todoPosition) {
        title = todoTitle;
        position = todoPosition;
        id = randomUUID().toString();
    }

    public Todo(String todoTitle, int todoPosition, String todoId) {
        title = todoTitle;
        position = todoPosition;
        id = todoId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString(){
        return title;
    }
}
