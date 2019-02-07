package com.a7md.zdb.ZCOL;

import com.a7md.zdb.GSonObject;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.properties.WritableProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class _Object<E extends ZSqlRow, Value> extends SqlCol<E, Value> {
    private final static Gson gsonBuilder = new GsonBuilder().create();

    private final int size;
    private final Class<Value> aClass;

    public _Object(String Name, int size, Class<Value> aClass, WritableProperty<E, Value> property) {
        super(Name, property);
        this.size = size;
        this.aClass = aClass;
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` VARCHAR(" + 1024 + ")" + (not_null ? " NOT NULL" : ""));
    }

    @Override
    public Equal equal(Value val) {
        return new Equal(this, gsonBuilder.toJson(val));
    }

    @Override
    final public Key toDbKey(E item) {
        Value value = property.getValue(item);
        return new Key<>(name, gsonBuilder.toJson(value));
    }

    public <c extends GSonObject> Key setData(c Value) {
        return new Key<>(name, gsonBuilder.toJson(Value));
    }

    @Override
    final public Value get(ResultSet resultSet) throws SQLException {
        return gsonBuilder.fromJson(resultSet.getString(this.name), aClass);
    }
}
