package com.a7md.zdb.Query.ZQ;

import com.a7md.zdb.ZCOL.COL;

public class LessThan implements Condition {
    private final COL col;
    private final Object value;

    public LessThan(COL col, Object value) {
        this.col = col;
        this.value = value;
    }

    @Override
    public String getWherePiece() {
        return "`" + col.mtable.TableName + "`.`" + col.name + "`<'" + value.toString() + "'";
    }
}
