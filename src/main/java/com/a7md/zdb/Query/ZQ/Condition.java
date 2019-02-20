package com.a7md.zdb.Query.ZQ;

public interface Condition {

    String getWherePiece();

    default ZWhere and(Condition condition) {
        return new ZWhere(this).and(condition);
    }
}
