package com.a7md.zdb.Query.ZQ;

import com.a7md.zdb.ZCOL.SqlCol;

public class Equal implements Condition {
    public final SqlCol col;
    private final Object value;

    public Equal(SqlCol col, Object value) {
        this.col = col;
        this.value = value;
    }

    @Override
    public String getWherePiece() {
        return "`" + col.mtable.TableName + "`.`" + col.name + "`='" + value.toString() + "'";
    }
}
