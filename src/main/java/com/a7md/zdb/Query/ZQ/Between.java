package com.a7md.zdb.Query.ZQ;

import com.a7md.zdb.ZCOL._TimeStamp;

public class Between implements Condition {
    final _TimeStamp mCol;
    private String from;
    private String to;

    public Between(_TimeStamp mCol, String from, String to) {
        this.mCol = mCol;
        this.from = from;
        this.to = to;
    }

    @Override
    public String getWherePiece() {
        if (from != null && to != null)
            return "timestamp(`" + mCol.mtable.TableName + "`.`" + mCol.name + "`) Between '" + from + "' and '" + to + "'";
        else {
            return null;
        }
    }

}
