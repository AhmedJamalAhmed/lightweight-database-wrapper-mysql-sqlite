package com.a7md.zdb.ZCOL;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.properties.WritableProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class _EnumVal<E extends ZSqlRow, enm extends Enum<enm>> extends SqlCol<E, enm> {

    private final Class<enm> mclass;

    public _EnumVal(String Name, Class<enm> mclass, WritableProperty<E, enm> mProperty) {
        super(Name, mProperty);
        this.mclass = mclass;
    }


    @Override
    public Equal equal(enm val) {
        return new Equal(this, val.name());
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` VARCHAR(" + 150 + ")" + (not_null ? " NOT NULL" : ""));
    }

    @Override
    public enm get(ResultSet resultSet) throws SQLException {
        return enm.valueOf(mclass, resultSet.getString(name));
    }

    @Override
    public Key toDbKey(E e) {
        enm value = property.getValue(e);
        return new Key<>(name, value.name());
    }
}
