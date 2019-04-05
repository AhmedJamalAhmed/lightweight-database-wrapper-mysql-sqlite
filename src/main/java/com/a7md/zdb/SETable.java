package com.a7md.zdb;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.COL;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._Decimal;
import com.a7md.zdb.ZCOL._ID_AI;
import com.a7md.zdb.ZCOL._Number;
import com.a7md.zdb.helpers.JavaSeLink;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

abstract public class SETable<Item extends ZSqlRow> extends Table<Item, ResultSet, JavaSeLink,
        _ID_AI<Item>, SqlCol<Item, ?>> {

    public SETable(JavaSeLink link, String TName, _ID_AI<Item> ID) {
        super(link, TName, ID);
    }

    public SqlCol[] getFilterCols() {
        return new SqlCol[]{
                this.getID()
        };
    }

    protected void register(SqlCol<Item, ?>... otherCols) throws Exception {
        ArrayList<COL> cols = new ArrayList<>();
        cols.add(getID());
        for (SqlCol col : otherCols) {
            cols.add(col);
            col.setMtable(this);
        }
        setCols(cols.toArray(new SqlCol[0]));
        db.registerTable(this, cols);
    }


}
