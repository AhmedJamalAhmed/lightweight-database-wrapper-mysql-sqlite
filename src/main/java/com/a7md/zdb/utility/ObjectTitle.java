package com.a7md.zdb.utility;

import com.a7md.zdb.utility.functions.Value;

public class ObjectTitle<Obj> implements Value<Obj> {
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
        return " -> " + this.title;
    }
}
