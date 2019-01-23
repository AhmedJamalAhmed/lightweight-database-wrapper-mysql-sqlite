package com.a7md.zdb.ZCOL;

import com.a7md.zdb.Link;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.RowTypes.types.ZSqlRow;
import com.a7md.zdb.properties.WritableProperty;
import com.a7md.zdb.utility.JDateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class _TimeStamp<E extends ZSqlRow> extends SqlCol<E, LocalDateTime> {

    public _TimeStamp(String Name, WritableProperty<E, LocalDateTime> property) {
        super(Name, property);
    }

    @Override
    public Equal equal(LocalDateTime val) {
        return new Equal(this, JDateTime.DB_TIMESTAMP(val));
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` DATETIME " + (not_null ? " NOT NULL" : ""));
    }

    @Override
    public LocalDateTime get(ResultSet resultSet) throws SQLException {
        return resultSet.getTimestamp(name).toLocalDateTime();
    }

    @Override
    public Key toDbKey(E i) {
        LocalDateTime ite = property.getValue(i);
        if (ite == null) ite = LocalDateTime.now(); /// todo
        return new Key<>(name, Timestamp.valueOf(ite));
    }
}
