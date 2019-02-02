package com.a7md.zdb;

import com.a7md.zdb.RowTypes.GSonRow;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._ID_AI;
import com.a7md.zdb.ZCOL._Object;
import com.a7md.zdb.ZCOL._String;
import com.a7md.zdb.ZCOL._TimeStamp;
import com.a7md.zdb.properties.WritableProperty;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ZGSonTable<sex extends GSonObject, Row extends GSonRow> extends ZTable<Row> {

    public final _Object<Row, sex> data;
    public final _String<Row> name = new _String<>("name", 100, new WritableProperty<>("الاسم", Row::getName, Row::setName));
    public final _TimeStamp<Row> last_updated = new _TimeStamp<>("last_updated", new WritableProperty<>("تاريخ اخر تعديل", Row::getLastModified, Row::setLastUpdated));

    public ZGSonTable(Link link, String TName, Class<sex> aClass) {
        super(link, TName, new _ID_AI<>("id"));
        data = new _Object<>("value", 100, aClass,
                new WritableProperty<>("", e -> (sex) e.getData(), Row::setData));
    }

    @Override
    final protected void register(SqlCol... othercols) {
        ArrayList<SqlCol> sqlCols = new ArrayList<>(Arrays.asList(othercols));
        sqlCols.add(0, name);
        sqlCols.add(1, data);
        sqlCols.add(2, last_updated);
        super.register(sqlCols.toArray(new SqlCol[0]));
    }
}
