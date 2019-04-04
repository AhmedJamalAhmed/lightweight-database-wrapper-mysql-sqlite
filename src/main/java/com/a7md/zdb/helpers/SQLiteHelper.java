package com.a7md.zdb.helpers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteHelper extends JavaSeLink {

    final private Connection connection;
    private String FilePath;

    public SQLiteHelper(String FilePath) throws Exception {
        this.FilePath = FilePath;
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.FilePath);
    }

    @Override
    public String getDbName() {
        return this.FilePath;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void DeleteDbIfExists() throws Exception {
        Files.deleteIfExists(Paths.get(FilePath));
    }
}
