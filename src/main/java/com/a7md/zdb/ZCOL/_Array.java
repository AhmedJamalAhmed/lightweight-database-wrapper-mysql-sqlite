package com.a7md.zdb.ZCOL;

import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.properties.WritableProperty;

public class _Array<E extends ZSqlRow> extends _Object<E, String[]> {

    public _Array(String Name, int size, WritableProperty<E, String[]> property) {
        super(Name, size, String[].class, property);
    }
}
