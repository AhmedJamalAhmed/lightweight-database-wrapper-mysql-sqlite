package com.a7md.zdb.properties;


public class Property<E, V> {

    private final String title;
    private final Func<E, V> reader;


    public Property(String title, Func<E, V> reader) {
        this.title = title;
        this.reader = reader;
    }

    public String getTitle() {
        return title;
    }

    public V getValue(E e) {
        return reader.apply(e);
    }

    public Func<E, V> getReader() {
        return reader;
    }
}
