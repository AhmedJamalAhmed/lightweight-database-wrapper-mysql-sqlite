package com.a7md.zdb.Query;

import com.a7md.zdb.ZCOL.COL;

import java.sql.ResultSet;

public abstract class JoinHandler extends Join {

    protected JoinHandler(COL first_col, COL second_col) {
        super(first_col, second_col);
    }

    public abstract void handleRow(ResultSet set) throws Exception;

}
