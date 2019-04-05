package com.a7md.zdb.Query.ZQ;

public abstract class Condition {

    public abstract String getWherePiece();

    public Selector and(Condition condition) {
        return toWhere().and(condition);
    }

    public Selector or(Condition condition) {
        return toWhere().or(condition);
    }

    public Selector toWhere() {
        return new Selector(this);
    }
}
