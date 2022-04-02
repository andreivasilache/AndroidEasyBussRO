package com.example.androideasybussro.state;

import java.util.HashMap;

public class Globe {
    private static Globe globe;
    private HashMap<ContextEnum, Context> contextCollection;

    private Globe() {
        contextCollection = new HashMap<>();
    }

    public static Globe getGlobe() {
        if (globe == null)
            globe = new Globe();

        return globe;
    }

    public Context getContext(ContextEnum key) {
        Context context = contextCollection.get(key);
        return context;
    }

    public void putContext(ContextEnum key, Context context) {
        contextCollection.put(key, context);
    }

    public void removeContext(ContextEnum key) {
        contextCollection.remove(key);
    }

    public void emptyGlobe() {
        contextCollection.clear();
    }
}