package com.a7md.zdb.ZCOL;

import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.helpers.MysqlHelper;
import com.a7md.zdb.ZTable;

import java.sql.SQLException;
import java.util.ArrayList;

public class CreateTable {
    public final ArrayList<String> first;
    public final ArrayList<String> last;

    public CreateTable(ZTable zTable, Link link, SqlCol... sqlCols) throws SQLException {
        this.first = new ArrayList<>();
        this.last = new ArrayList<>();

        for (SqlCol sqlCol : sqlCols) {
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
        if (link instanceof MysqlHelper) {
            sql += "ENGINE = InnoDB CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci;";
        } else {
            sql += ";";
        }
        link.update(sql);
    }

}
