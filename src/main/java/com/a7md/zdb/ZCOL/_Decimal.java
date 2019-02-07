package com.a7md.zdb.ZCOL;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.GreaterThan;
import com.a7md.zdb.Query.ZQ.LessThan;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.properties.WritableProperty;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class _Decimal<E extends ZSqlRow> extends SqlCol<E, BigDecimal> {

    public _Decimal(String name, WritableProperty<E, BigDecimal> property) {
        super(name, property);
    }

    @Override
    public Equal equal(BigDecimal val) {
        return new Equal(this, val);
    }

    public LessThan less_than(BigDecimal val) {
        return new LessThan(this, val);
    }

    public GreaterThan greater_than(BigDecimal val) {
        return new GreaterThan(this, val);
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` decimal(18,6) " + (not_null ? " NOT NULL" : ""));
    }

    @Override
    public BigDecimal get(ResultSet resultSet) throws SQLException {
        String string = resultSet.getString(name);
        if (string != null) {
            return new BigDecimal(string);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
