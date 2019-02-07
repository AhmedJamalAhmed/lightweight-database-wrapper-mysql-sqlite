package com.a7md.zdb.ZCOL;

import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.properties.WritableProperty;

public abstract class _Custom<E extends ZSqlRow, X> extends SqlCol<E, X> {

    public final short Size;

    public _Custom(String name, int Size, WritableProperty<E, X> property) {
        super(name, property, false);
        this.Size = (short) Size;
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` VARCHAR(" + Size + ")" + (not_null ? " NOT NULL" : ""));
    }
}