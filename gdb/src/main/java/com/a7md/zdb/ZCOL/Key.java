package com.a7md.zdb.ZCOL;

public class Key<V> {

    public final String ColName;
    private final V Value;

    public Key(String Col, V Value) {
        this.ColName = Col;
        this.Value = Value;
    }

    public V getValue() {
        return Value;
    }
}
