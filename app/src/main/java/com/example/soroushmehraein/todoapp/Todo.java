package com.example.soroushmehraein.todoapp;

/**
 * Created by soroushmehraein on 6/9/16.
 */
public class Todo {
    public String title;
    public int position;

    public Todo(String todoTitle, int todoPosition) {
        title = todoTitle;
        position = todoPosition;
    }

    @Override
    public String toString(){
        return title;
    }
}
