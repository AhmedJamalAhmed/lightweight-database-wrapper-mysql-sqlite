package com.a7md.zdb.helpers;

import com.a7md.zdb.Query.Join;
import com.a7md.zdb.Query.JoinHandler;
import com.a7md.zdb.Query.ZQ.Condition;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.Table;
import com.a7md.zdb.ZCOL.COL;
import com.a7md.zdb.ZCOL.CreateTable;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.ZCOL._Decimal;
import com.a7md.zdb.ZCOL._Number;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.utility.ZSystemError;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Link<ResultSet> {

    public void DeleteRow(Equal id) throws SQLException {
        String SQL = "DELETE FROM " + id.col.mtable.TableName + new Selector(id).get();
        update(SQL);
    }

    final public <E extends ZSqlRow> void registerTable(Table zTable, ArrayList<COL> sqlCols) throws Exception {
        if (!TableExist(zTable.TableName)) {
            new CreateTable(zTable, this, sqlCols);
            zTable.onTableCreation();
            System.out.println("Database - " + this.getDbName() + ". -> creating table " + zTable.TableName + " ...");
        }
    }

    final protected String build_join(Join join, String sql_cols) {
        COL c1 = join.first_col;
        COL c2 = join.Second_col;
        return "SELECT " + sql_cols + " FROM " +
                c1.mtable.TableName + " join " + c2.mtable.TableName + " on "
                + c1.mtable.TableName + "." + c1.name + "="
                + c2.mtable.TableName + "." + c2.name + " ";
    }

    public <U extends ZSqlRow> U row(Equal where) throws Exception {
        List<U> list = list(where.col.mtable, new Selector(where));
        if (list.size() != 1) {
            throw new ZSystemError("query of " + new Selector(where).get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public <U extends ZSqlRow> U row(Table table, Selector where) throws Exception {
        List<U> list = list(table, where);
        if (list.size() != 1) {
            throw new ZSystemError("query of " + where.get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public <U extends ZSqlRow> U lastRow(Table<U, ResultSet, ?, ?, ?> table, Selector where) throws Exception {
        where.setLimits(0, 1);
        where.orderDescBy(table.getID());
        List<U> list = list(table, where);
        if (list.size() != 1) {
            throw new ZSystemError("query of " + where.get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public <U extends ZSqlRow> List<U> list(Table<U, ResultSet, ?, ?, ?> table) throws Exception {
        return list(table, null);
    }

    public <T> T value(COL<ResultSet, ?, T> col, int id) throws Exception {
        return value(col, new Selector(col.mtable.getID().equal(id)));
    }

    public int count(Table table, Condition condition) throws Exception {
        return count(table, new Selector(condition));
    }

    abstract public void createTransaction(DBTransaction dbTransaction) throws Throwable;

    abstract public String getDbName();

    abstract public void DeleteDbIfExists() throws Exception;

    abstract public void update(String SQL) throws SQLException;

    abstract public int AddRow(Table table, List<Key> RowArray) throws SQLException;

    abstract public boolean UpdateRow(Equal id, List<Key> RowArray) throws SQLException;

    abstract public void clearTable(Table zTable) throws SQLException;

    abstract public void deleteTable(String tableName) throws SQLException;

    abstract public <U extends ZSqlRow> List<U> list(Table<U, ResultSet, ?, ?, ?> table, Selector where) throws Exception;

    abstract public void query(JoinHandler bind, Selector where) throws Exception;

    abstract public <T> T value(COL<ResultSet, ?, T> of_col, Selector where) throws Exception;

    abstract public int count(Table table, Selector Where) throws Exception;

    abstract public int count(Join join, Selector Where) throws Exception;

    abstract public boolean exist(Equal Ident) throws Exception;

    abstract public boolean exist(Table table, Selector selector) throws Exception;

    abstract public <V> ArrayList<V> distinctValues(COL<ResultSet, ?, V> col) throws Exception;

    abstract protected boolean TableExist(String tableName) throws SQLException;

    abstract public <Item extends ZSqlRow> Item fromResultSet(Table<Item, ResultSet, ?, ?, ?> table, ResultSet res) throws Exception;

    public interface DBTransaction {
        void run() throws Throwable;
    }
}
