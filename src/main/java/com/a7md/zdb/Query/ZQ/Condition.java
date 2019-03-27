package com.a7md.zdb.Query.ZQ;

public interface Condition {

    String getWherePiece();

    default Selector and(Condition condition) {
        return toWhere().and(condition);
    }

    default Selector or(Condition condition) {
        return toWhere().or(condition);
    }

    default Selector toWhere() {
        return new Selector(this);
    }
}
