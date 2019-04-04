package com.a7md.zdb.helpers;

import com.a7md.zdb.Query.Join;
import com.a7md.zdb.Query.JoinHandler;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.CreateTable;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._Decimal;
import com.a7md.zdb.ZCOL._Number;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.ZTable;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class JavaSeLink extends Link {

    public void createTransaction(Link.DBTransaction dbTransaction) throws Throwable {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        try {
            dbTransaction.run();
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    final public <E extends ZSqlRow> void registerTable(ZTable<E> table, SqlCol<E, ?>[] cols) throws Exception {
        boolean tableExist = false;
        ResultSet rs = getConnection().getMetaData().getTables(null, null, "%", null);
        while (rs.next()) {
            if (rs.getString("TABLE_NAME").equalsIgnoreCase(table.TableName)) {
                tableExist = true;
                break;
            }
        }
        rs.close();
        if (!tableExist) {
            new CreateTable(table, this, cols);
            table.onTableCreation();
            System.out.println("Database - " + getDbName() + ". -> creating table " + table.TableName + " ...");
        }
    }

    public abstract Connection getConnection();

    public int update(String SQL) throws SQLException {
        Statement statement = getConnection().createStatement();
        int i = statement.executeUpdate(SQL);
        statement.close();
        return i;
    }


    public int AddRow(ZTable table, List<Key> RowArray) throws SQLException {
        StringBuilder SQl = new StringBuilder("INSERT INTO " + table.TableName + "(");
        StringBuilder Values = new StringBuilder(" VALUES ( ");

        for (int i = 0; i < RowArray.size(); i++) {
            Key get = RowArray.get(i);
            SQl.append(get.ColName);
            Values.append("?");
            if (i + 1 != RowArray.size()) {
                SQl.append(",");
                Values.append(",");
            }
        }
        SQl.append(")").append(Values).append(")");
        try (PreparedStatement PS = getConnection().prepareStatement(SQl.toString(), Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < RowArray.size(); i++) {
                Key key = RowArray.get(i);
                PS.setObject(i + 1, key.getValue());
            }
            int RowsAffected = PS.executeUpdate();
            if (RowsAffected != 0) {
                ResultSet rs = PS.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        // if faild
        return -1;
    }

    public boolean UpdateRow(Equal id, List<Key> RowArray) throws SQLException {
        StringBuilder SQl = new StringBuilder("UPDATE " + id.col.mtable.TableName + " SET ");
        for (int i = 0; i < RowArray.size(); i++) {
            SQl.append(RowArray.get(i).ColName);
            SQl.append("=");
            SQl.append("?");
            if (i + 1 != RowArray.size()) {
                SQl.append(",");
            }
        }
        SQl.append(new Selector(id).get());
        int RowsAffected;
        try (PreparedStatement PS = getConnection().prepareStatement(SQl.toString())) {
            for (int i = 0; i < RowArray.size(); i++) {
                Key key = RowArray.get(i);
                PS.setObject(i + 1, key.getValue());
            }
            RowsAffected = PS.executeUpdate();
        }
        return RowsAffected != 0;
    }

    private <E> E getResult(String sql, JavaSeLink.ResultHandler<E> handler) throws Exception {
        try (Statement e = getConnection().createStatement()) {
            try (ResultSet res = e.executeQuery(sql)) {
                return handler.handle(res);
            }
        }
    }

    private void getResult(String sql, JoinHandler handler) throws Exception {
        try (Statement e = getConnection().createStatement()) {
            try (ResultSet res = e.executeQuery(sql)) {
                while (res.next()) {
                    handler.handleRow(res);
                }
            }
        }
    }

    public void clearTable(ZTable zTable) throws SQLException {
        String SQL = "DELETE FROM " + zTable.TableName + ";";
        update(SQL);
    }

    public void deleteTable(String tableName) throws SQLException {
        update("DROP TABLE " + tableName + ";");
    }

    public <U extends ZSqlRow> List<U> list(ZTable<U> table, Selector where) throws Exception {
        String sql = "SELECT * FROM " + table.TableName;
        if (where != null) {
            sql += where.get();
        }
        return getResult(sql, r -> {
            LinkedList<U> list = new LinkedList<>();
            while (r.next()) {
                list.add(table.fromResultSet(r));
            }
            return list;
        });
    }

    public void query(JoinHandler bind, Selector where) throws Exception {
        String sql = build_join(bind, "*") + (where != null ? where.get() : "");
        getResult(sql, bind);
    }

    public <T> T value(SqlCol<?, T> of_col, Selector where) throws Exception {
        String SQl = "SELECT " + of_col.name + " From " + of_col.mtable.TableName + where.get();
        return getResult(SQl,
                r -> {
                    if (r.next()) {
                        return of_col.get(r);
                    } else {
                        return null;
                    }
                });
    }

    public BigDecimal sum(_Decimal col, Selector where) throws Exception {
        _Decimal decimal = new _Decimal<>("sum(" + col.name + ")", null);
        decimal.setMtable(col.mtable);
        String sql = "select sum(" + col.name + ") from " + col.mtable.TableName
                + (where == null ? "" : where.get());
        return getResult(sql, r -> {
            if (r.next()) {
                BigDecimal decimal1 = decimal.get(r);
                return decimal1 == null ? BigDecimal.ZERO : decimal1;
            } else {
                return BigDecimal.ZERO;
            }
        });
    }

    public BigDecimal sum(_Decimal col, Join join, Selector where) throws Exception {
        _Decimal decimal = new _Decimal<>("sum(" + col.name + ")", null);
        decimal.setMtable(col.mtable);
        String sql = build_join(join, decimal.name) + where.get();
        return getResult(sql, r -> {
            if (r.next()) {
                return decimal.get(r);
            } else {
                return BigDecimal.ZERO;
            }
        });
    }

    public long sum(_Number col, Selector where) throws Exception {
        _Number decimal = new _Number<>("sum(" + col.name + ")", null);
        decimal.setMtable(col.mtable);
        String sql = "select sum(" + col.name + ") from " + col.mtable.TableName
                + (where == null ? "" : where.get());
        return getResult(sql, r -> {
            if (r.next()) {
                return decimal.get(r).longValue();
            } else {
                return 0L;
            }
        });
    }

    public int count(ZTable table, Selector Where) throws Exception {
        String SQL = "select count(0) from " + table.TableName + (Where == null ? "" : Where.get());
        return getResult(SQL, r -> {
            if (r.next()) {
                return r.getInt(1);
            } else {
                return -1;
            }
        });
    }

    public int count(Join join, Selector Where) throws Exception {
        String SQL = build_join(join, "count(0)") + (Where == null ? "" : Where.get());
        return getResult(SQL, r -> {
            if (r.next()) {
                return r.getInt(1);
            } else {
                return -1;
            }
        });
    }

    public boolean exist(Equal Ident) throws Exception {
        return getResult("SELECT EXISTS(SELECT 1 FROM " + Ident.col.mtable.TableName + new Selector(Ident).get() + ")",
                r -> {
                    if (r.next()) {
                        return r.getInt(1) == 1;
                    } else {
                        return false;
                    }
                }
        );
    }

    public boolean exist(ZTable table, Selector selector) throws Exception {
        return getResult("SELECT EXISTS(SELECT 1 FROM " + table.TableName +
                        selector.get() + ")",
                r -> {
                    if (r.next()) {
                        return r.getInt(1) == 1;
                    } else {
                        return false;
                    }
                }
        );
    }

    public <V> ArrayList<V> distinctValues(SqlCol<?, V> col) throws Exception {
        return getResult("SELECT DISTINCT " + col.name + " from " + col.mtable.TableName,
                r -> {
                    ArrayList<V> list = new ArrayList<>();
                    while (r.next()) {
                        list.add(col.get(r));
                    }
                    return list;
                }
        );
    }

    public interface ResultHandler<Return> {
        Return handle(ResultSet r) throws Exception;
    }
}
