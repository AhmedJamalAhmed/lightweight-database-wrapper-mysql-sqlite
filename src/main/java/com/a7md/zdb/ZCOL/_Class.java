package com.a7md.zdb.ZCOL;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.RowTypes.types.ZSqlRow;
import com.a7md.zdb.ZCOL._Custom;
import com.a7md.zdb.properties.WritableProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class _Class<E extends ZSqlRow, C> extends _Custom<E, Class<? extends C>> {

    public _Class(String name, int Size, WritableProperty<E, Class<? extends C>> property) {
        super(name, Size, property);
    }

    @Override
    public Equal equal(Class<? extends C> val) {
        return new Equal(this, val);
    }

    @Override
    public Class<? extends C> get(ResultSet resultSet) throws SQLException {
        try {
            return (Class<? extends C>) Class.forName(resultSet.getString(name));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
