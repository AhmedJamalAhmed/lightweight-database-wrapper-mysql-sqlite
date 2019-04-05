package com.a7md.zdb.ZCOL;

import com.a7md.zdb.Table;
import com.a7md.zdb.helpers.Link;

import java.sql.SQLException;
import java.util.ArrayList;

public class CreateTable {
    public final ArrayList<String> first;
    public final ArrayList<String> last;

    public CreateTable(Table zTable, Link link, ArrayList<COL> sqlCols) throws SQLException {
        this.first = new ArrayList<String>();
        this.last = new ArrayList<String>();

        for (COL sqlCol : sqlCols) {
            sqlCol.create(this, link);
        }
        first.addAll(last);
        String cls_data = "";
        int size = first.size();

        for (int i = 0; i < size; i++) {
            cls_data += first.get(i);
            if (i < size - 1) {
                cls_data += " , ";
            }
        }
        String sql = "create table " + zTable.TableName + "(" +
                cls_data + ")";
        if (link.getClass().getSimpleName().equals("MysqlHelper")) {
            sql += "ENGINE = InnoDB CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci;";
        } else {
            sql += ";";
        }
        link.update(sql);
    }

}
