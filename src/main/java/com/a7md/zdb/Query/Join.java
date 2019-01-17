package com.a7md.zdb.Query;

import com.a7md.zdb.ZCOL.SqlCol;

public class Join {
    public final SqlCol first_col;
    public final SqlCol Second_col;

    public Join(SqlCol first_col, SqlCol second_col) {
        this.first_col = first_col;
        Second_col = second_col;
    }
}
