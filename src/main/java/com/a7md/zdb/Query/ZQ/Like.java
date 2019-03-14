package com.a7md.zdb.Query.ZQ;

import com.a7md.zdb.ZCOL.SqlCol;

public class Like implements Condition {
    private final SqlCol col;
    private final Object value;

    public Like(SqlCol col, Object value) {
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
