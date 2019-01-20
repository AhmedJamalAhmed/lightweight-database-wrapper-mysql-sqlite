package com.a7md.zdb;

import com.a7md.zdb.RowTypes.ZItem;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._ID_AI;
import com.a7md.zdb.ZCOL._String;
import com.a7md.zdb.ZCOL._TimeStamp;
import com.a7md.zdb.properties.WritableProperty;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ZNamedTable<E extends ZItem> extends ZTable<E> {

    final _TimeStamp<E> last_updated = new _TimeStamp<>("last_updated", new WritableProperty<>("تاريخ اخر تعديل", E::getLastModified, E::setLastModified));
    final _String<E> name = new _String<>("name", 100, new WritableProperty<>("الاسم", E::getName, E::setName));

    public ZNamedTable(Link link, String TName, _ID_AI<E> ID) {
        super(link, TName, ID);
    }

    @Override
    protected void register(SqlCol... othercols) {
        ArrayList<SqlCol> sqlCols = new ArrayList<>(Arrays.asList(othercols));
        sqlCols.add(0, name);
        sqlCols.add(2, last_updated);
        super.register(sqlCols.toArray(new SqlCol[0]));
    }
}
