package com.a7md.zdb.ZCOL;

import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.properties.WritableProperty;

import java.sql.ResultSet;

abstract public class SqlCol<E extends ZSqlRow, V> extends COL<ResultSet, E, V> {

    protected SqlCol(String Name, WritableProperty<E, V> property, boolean not_null) {
        super(Name, property, not_null);
    }

    protected SqlCol(String Name, WritableProperty<E, V> property) {
        super(Name, property);
    }

    @Override
    public void assign(E e, ResultSet resultSet) throws Exception {
        V v = get(resultSet);
        property.setValue(e, v);
    }
}
