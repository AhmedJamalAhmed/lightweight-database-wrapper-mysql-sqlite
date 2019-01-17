package com.a7md.zdb.Query.ZQ;

import com.a7md.zdb.ZCOL._String;

public class Like implements Condition {
    private final _String col;
    private final Object value;

    public Like(_String col, Object value) {
        this.col = col;
        this.value = value;
    }

    @Override
    public String getWherePiece() {
        return "`" + col.mtable.TableName + "`.`" + col.name + "` Like '%" + value.toString() + "%'";
    }
}
