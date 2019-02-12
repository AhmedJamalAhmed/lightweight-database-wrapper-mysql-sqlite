package com.a7md.zdb.ZCOL;

import com.a7md.zdb.RowTypes.LastModified;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.properties.WritableProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class _LastModified<E extends LastModified & ZSqlRow> extends _TimeStamp<E> {

    public _LastModified(String title) {
        super("last_modified", new WritableProperty<>(title, E::getLastModified, E::setLastModified));
    }

    @Override
    public Key toDbKey(E i) {
        return new Key<>(name, Timestamp.valueOf(LocalDateTime.now()));
    }
}
