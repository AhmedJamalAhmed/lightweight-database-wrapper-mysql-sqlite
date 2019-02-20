package com.a7md.zdb.ZCOL;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.properties.WritableProperty;
import com.a7md.zdb.utility.JDateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class _Date<E extends ZSqlRow> extends SqlCol<E, LocalDate> {

    public _Date(String Name, WritableProperty<E, LocalDate> property) {
        super(Name, property);
    }

    @Override
    public Equal equal(LocalDate val) {
        return new Equal(this, JDateTime.DB_TIMESTAMP(val.atStartOfDay()));
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` DATETIME " + (not_null ? " NOT NULL" : ""));
    }

    @Override
    public LocalDate get(ResultSet resultSet) throws SQLException {
        return resultSet.getTimestamp(name).toLocalDateTime().toLocalDate();
    }

    @Override
    public Key toDbKey(E i) {
        LocalDate ite = property.getValue(i);
        if (ite == null) ite = LocalDate.now(); /// todo
        return new Key<>(name, Timestamp.valueOf(ite.atStartOfDay()));
    }
}
