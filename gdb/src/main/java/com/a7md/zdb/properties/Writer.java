package com.a7md.zdb.properties;

public interface Writer<C, V> {
    void set(C c, V v);
}
