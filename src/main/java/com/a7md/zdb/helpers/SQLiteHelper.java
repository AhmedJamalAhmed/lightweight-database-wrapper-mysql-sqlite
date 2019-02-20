package com.a7md.zdb.helpers;

import com.a7md.zdb.DBErrorHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteHelper extends Link {

    final private Connection connection;
    private String FilePath;

    public SQLiteHelper(String FilePath, DBErrorHandler dbErrorHandler) {
        super(dbErrorHandler);
        Connection ctin = null;
        try {
            this.FilePath = FilePath;
            Class.forName("org.sqlite.JDBC");
            ctin = DriverManager.getConnection("jdbc:sqlite:" + this.FilePath);
        } catch (Exception e) {
            dbErrorHandler.handle_error(e);
        }
        this.connection = ctin;
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
