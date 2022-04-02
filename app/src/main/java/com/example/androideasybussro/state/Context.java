package com.example.androideasybussro.state;

import java.util.HashMap;

public class Context<T> {
    private HashMap<String, T> states;

    public Context() {
        states = new HashMap<>();
    }

    public void putItem(String key, T item) {
        states.put(key, item);
    }
    public T getState(String stateName) {
        T item = states.get(stateName);
        return item;
    }

    public void removeState(String stateName) {
        states.remove(stateName);
    }

    public void emptyState() {
        states.clear();
    }
}