package com.a7md.zdb.Query.ZQ;

import com.a7md.zdb.ZCOL._TimeStamp;
import com.a7md.zdb.helpers.MysqlHelper;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Between extends Condition {
    private final _TimeStamp mCol;

    final private LocalDateTime from;
    final private LocalDateTime to;

    public Between(_TimeStamp mCol, LocalDateTime from, LocalDateTime to) {
        this.mCol = mCol;
        this.from = from;
        this.to = to;
    }

    @Override
    public String getWherePiece() {
        if (from == null || to == null)
            return null;
        String val;
        if (mCol.mtable.db instanceof MysqlHelper) {
            val = "UNIX_TIMESTAMP(`" + mCol.mtable.TableName + "`.`" + mCol.name + "`) Between '" + from + "' and '" + to + "'";
        } else {
            Timestamp fromF = Timestamp.valueOf(from);
            Timestamp toF = Timestamp.valueOf(to);
            val = "`" + mCol.mtable.TableName + "`.`" + mCol.name + "` Between '" + toF.getTime() + "' and '" + fromF.getTime() + "'";
        }
        System.out.println("between : " + val);
        return val;
    }

}
