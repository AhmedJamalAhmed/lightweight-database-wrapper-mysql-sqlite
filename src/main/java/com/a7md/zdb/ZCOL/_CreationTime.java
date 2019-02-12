package com.a7md.zdb.ZCOL;

import com.a7md.zdb.RowTypes.CreationTime;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.properties.WritableProperty;

public class _CreationTime<E extends CreationTime & ZSqlRow> extends _TimeStamp<E> {
    public _CreationTime(String title) {
        super("created", new WritableProperty<>(title,
                E::getCreationDateTime,
                E::setCreationDateTime));
    }
}
