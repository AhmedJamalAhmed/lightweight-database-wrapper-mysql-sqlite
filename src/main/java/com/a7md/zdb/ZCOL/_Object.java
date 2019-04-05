package com.a7md.zdb.ZCOL;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.properties.WritableProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class _Object<E extends ZSqlRow, type> extends SqlCol<E, type> {

    private final static Gson gsonBuilder = new GsonBuilder().create();
    private final int size;

    public _Object(String Name, int size, WritableProperty<E, type> property) {
        super(Name, property);
        this.size = size;
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` VARCHAR(" + size + ")" + (not_null ? " NOT NULL" : ""));
    }

    @Override
    public Equal equal(type val) {
        return new Equal(this, gsonBuilder.toJson(new AtomicReference<>(val), AtomicReference.class));
    }

    @Override
    final public Key toDbKey(E item) {
        type value = property.getValue(item);
        return new Key<>(name, gsonBuilder.toJson(new AtomicReference<>(value), AtomicReference.class));
    }

    public Key setData(AtomicReference<type> Value) {
        return new Key<>(name, gsonBuilder.toJson(Value));
    }

    @Override
    final public type get(ResultSet resultSet) throws SQLException {
        AtomicReference atomicReference = gsonBuilder.fromJson(resultSet.getString(this.name), AtomicReference.class);
        return (type) atomicReference.get();
    }
}
