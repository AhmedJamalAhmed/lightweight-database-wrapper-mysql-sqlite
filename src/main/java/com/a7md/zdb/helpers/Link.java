package com.a7md.zdb.helpers;

import com.a7md.zdb.DBErrorHandler;
import com.a7md.zdb.Query.JoinHandler;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.CreateTable;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.ZTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class Link {

    private DBErrorHandler errorHandler;

    public Link(DBErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void createTransaction(DBTransaction dbTransaction) throws Throwable {
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

    final public <E extends ZSqlRow> void registerTable(ZTable<E> table, SqlCol<E, ?>[] cols) {
        try {
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
            }
        } catch (Throwable e) {
            this.errorHandler.handle_error(e);
        }
    }

    public abstract Connection getConnection();

    abstract public void DeleteDbIfExists() throws Exception;

    public int update(String SQL) throws SQLException {
        Statement statement = getConnection().createStatement();
        int i = statement.executeUpdate(SQL);
        statement.close();
        return i;
    }

    public void DeleteRow(Equal id) throws SQLException {
        String SQL = "DELETE FROM " + id.col.mtable.TableName + new Selector(id).get();
        update(SQL);
    }

    public int AddRow(ZTable table, List<Key> RowArray) throws SQLException {
        String SQl = "INSERT INTO " + table.TableName + "(";
        String Values = " VALUES ( ";

        for (int i = 0; i < RowArray.size(); i++) {
            Key get = RowArray.get(i);
            SQl += get.ColName;
            Values += "?";
            if (i + 1 != RowArray.size()) {
                SQl += ",";
                Values += ",";
            }
        }
        SQl += ")" + Values + ")";
        try (PreparedStatement PS = getConnection().prepareStatement(SQl, Statement.RETURN_GENERATED_KEYS)) {
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
        String SQl = "UPDATE " + id.col.mtable.TableName + " SET ";
        for (int i = 0; i < RowArray.size(); i++) {
            SQl += RowArray.get(i).ColName;
            SQl += "=";
            SQl += "?";
            if (i + 1 != RowArray.size()) {
                SQl += ",";
            }
        }
        SQl += new Selector(id).get();
        int RowsAffected;
        try (PreparedStatement PS = getConnection().prepareStatement(SQl)) {
            for (int i = 0; i < RowArray.size(); i++) {
                Key key = RowArray.get(i);
                PS.setObject(i + 1, key.getValue());
            }
            RowsAffected = PS.executeUpdate();
        }
        return RowsAffected != 0;
    }

    public <E> E getResult(String sql, ResultHandler<E> handler) throws Exception {
        try (Statement e = getConnection().createStatement()) {
            try (ResultSet res = e.executeQuery(sql)) {
                return handler.handle(res);
            }
        }
    }

    public void getResult(String sql, JoinHandler handler) throws Exception {
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

    public interface ResultHandler<Return> {
        Return handle(ResultSet r) throws Exception;
    }

    public interface DBTransaction {
        void run() throws Throwable;
    }
}
