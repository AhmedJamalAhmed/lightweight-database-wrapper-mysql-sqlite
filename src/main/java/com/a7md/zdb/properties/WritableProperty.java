package com.a7md.zdb.properties;

import com.a7md.zdb.Writer;

import java.util.function.Function;

public class WritableProperty<E, V> extends Property<E, V> {

    private final Writer<E, V> writer;

    public WritableProperty(String title, Function<E, V> reader,
                            Writer<E, V> writer) {
        super(title, reader);
        this.writer = writer;
    }

    public void setValue(E e, V v) {
        writer.set(e, v);
    }

    public void validate(V v) throws Exception {

    }
}
