package com.a7md.zdb.helpers;

import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.COL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlHelper extends JavaSeLink {

    final private Connection connection;
    private String DBName;

    public MysqlHelper(String host, String dbname, String userName, String password) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection c = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/?autoReconnect=true&useSSL=false&useUnicode=yes&characterEncoding=UTF-8", userName, password);
             Statement S = c.createStatement()) {
            S.executeUpdate("create database if not exists " + dbname + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        }
        this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + dbname + "?autoReconnect=true&useSSL=false&useUnicode=yes&characterEncoding=UTF-8", userName, password);
        this.DBName = dbname;
    }

    @Override
    public String getDbName() {
        return this.DBName;
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
