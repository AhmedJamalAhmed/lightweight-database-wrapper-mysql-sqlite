package com.a7md.zdb.properties;

import java.util.function.Function;

public class Property<E, V> {

    private final String title;
    private final Function<E, V> reader;


    public Property(String title, Function<E, V> reader) {
        this.title = title;
        this.reader = reader;
    }

    public String getTitle() {
        return title;
    }

    public V getValue(E e) {
        return reader.apply(e);
    }

    public Function<E, V> getReader() {
        return reader;
    }
}
