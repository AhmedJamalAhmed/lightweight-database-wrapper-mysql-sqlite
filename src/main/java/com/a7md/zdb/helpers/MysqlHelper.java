package com.a7md.zdb.helpers;

import com.a7md.zdb.DBErrorHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlHelper extends Link {

    final private Connection connection;
    private String DBName;

    public MysqlHelper(String host, String dbname, String userName, String password, DBErrorHandler dbErrorHandler) {
        super(dbErrorHandler);
        Connection con;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection c = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/?autoReconnect=true&useSSL=false&useUnicode=yes&characterEncoding=UTF-8", userName, password);
                 Statement S = c.createStatement()) {
                S.executeUpdate("create database if not exists " + dbname + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            }
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + dbname + "?autoReconnect=true&useSSL=false&useUnicode=yes&characterEncoding=UTF-8", userName, password);
            this.DBName = dbname;
        } catch (ClassNotFoundException | SQLException e) {
            dbErrorHandler.handle_error(e);
            con = null;
        }
        this.connection = con;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void DeleteDbIfExists() throws SQLException {
        update("DROP DATABASE IF EXISTS " + DBName);
    }

    public void CopyTo(String FromDBName, String ToDBName, String TableName) throws SQLException {
        update("SET SQL_SAFE_UPDATES = 0;");
        update("INSERT INTO " + ToDBName + "." + TableName + " SELECT * FROM " + FromDBName + "." + TableName);
    }
}
