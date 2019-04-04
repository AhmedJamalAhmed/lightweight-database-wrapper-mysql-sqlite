package com.a7md.zdb.helpers;

import com.a7md.zdb.Query.Join;
import com.a7md.zdb.Query.JoinHandler;
import com.a7md.zdb.Query.ZQ.Condition;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._Decimal;
import com.a7md.zdb.ZCOL._Number;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.ZTable;
import com.a7md.zdb.utility.ZSystemError;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Link {

    public void DeleteRow(Equal id) throws SQLException {
        String SQL = "DELETE FROM " + id.col.mtable.TableName + new Selector(id).get();
        update(SQL);
    }

    final protected String build_join(Join join, String sql_cols) {
        SqlCol c1 = join.first_col;
        SqlCol c2 = join.Second_col;
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

    public <U extends ZSqlRow> U row(ZTable<U> table, Selector where) throws Exception {
        List<U> list = list(table, where);
        if (list.size() != 1) {
            throw new ZSystemError("query of " + where.get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public <U extends ZSqlRow> U lastRow(ZTable<U> table, Selector where) throws Exception {
        where.setLimits(0, 1);
        where.orderDescBy(table.getID());
        List<U> list = list(table, where);
        if (list.size() != 1) {
            throw new ZSystemError("query of " + where.get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public <U extends ZSqlRow> List<U> list(ZTable<U> table) throws Exception {
        return list(table, null);
    }

    public <T> T value(SqlCol<?, T> col, int id) throws Exception {
        return value(col, new Selector(col.mtable.getID().equal(id)));
    }

    public BigDecimal sum(_Decimal col, Equal where) throws Exception {
        return sum(col, new Selector(where));
    }

    public long sum(_Number col, Equal where) throws Exception {
        return sum(col, new Selector(where));
    }

    public BigDecimal sum(_Decimal col) throws Exception {
        return sum(col, (Selector) null);
    }

    public int count(ZTable table, Condition condition) throws Exception {
        return count(table, new Selector(condition));
    }

    abstract public void createTransaction(DBTransaction dbTransaction) throws Throwable;

    abstract public String getDbName();

    abstract public <E extends ZSqlRow> void registerTable(ZTable<E> table, SqlCol<E, ?>[] cols) throws Exception;

    abstract public void DeleteDbIfExists() throws Exception;

    abstract public int update(String SQL) throws SQLException;

    abstract public int AddRow(ZTable table, List<Key> RowArray) throws SQLException;

    abstract public boolean UpdateRow(Equal id, List<Key> RowArray) throws SQLException;

    abstract public void clearTable(ZTable zTable) throws SQLException;

    abstract public void deleteTable(String tableName) throws SQLException;

    abstract public <U extends ZSqlRow> List<U> list(ZTable<U> table, Selector where) throws Exception;

    abstract public void query(JoinHandler bind, Selector where) throws Exception;

    abstract public <T> T value(SqlCol<?, T> of_col, Selector where) throws Exception;

    abstract public BigDecimal sum(_Decimal col, Selector where) throws Exception;

    abstract public BigDecimal sum(_Decimal col, Join join, Selector where) throws Exception;

    abstract public long sum(_Number col, Selector where) throws Exception;

    abstract public int count(ZTable table, Selector Where) throws Exception;

    abstract public int count(Join join, Selector Where) throws Exception;

    abstract public boolean exist(Equal Ident) throws Exception;

    abstract public boolean exist(ZTable table, Selector selector) throws Exception;

    abstract public <V> ArrayList<V> distinctValues(SqlCol<?, V> col) throws Exception;

    public interface DBTransaction {
        void run() throws Throwable;
    }

}
