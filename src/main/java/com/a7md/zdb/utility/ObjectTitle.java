package com.a7md.zdb.utility;

import java.util.function.Supplier;

public class ObjectTitle<Obj> implements Supplier<Obj> {
    final String title;
    final Obj obj;

    public ObjectTitle(String title, Obj obj) {
        this.title = title;
        this.obj = obj;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Obj get() {
        return this.obj;
    }

    @Override
    public String toString() {
        return title;
    }
}
