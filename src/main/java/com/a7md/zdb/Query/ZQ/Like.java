package com.a7md.zdb.Query.ZQ;

import com.a7md.zdb.ZCOL.COL;

public class Like implements Condition {
    private final COL col;
    private final Object value;

    public Like(COL col, Object value) {
        this.col = col;
        this.value = value;
    }

    @Override
    public String getWherePiece() {
        if (value == null) {
            return "`" + col.mtable.TableName + "`.`" + col.name + "` Like '%" + "" + "%'";
        }
        return "`" + col.mtable.TableName + "`.`" + col.name + "` Like '%" + value.toString() + "%'";
    }
}
