package com.a7md.zdb;

import com.a7md.zdb.utility.ZSystemError;

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
            dbErrorHandler.handle_error(new ZSystemError("خطأ بالاتصال بقاعدة البيانات",
                    "حدث خطأ أثناء الاتصال بقاعدة البيانات يرجى التأكد من ان قاعدة البيانات موجودة على جهاز الكمبيوتر" +
                            "\n او الاتصال بالدعم الفني في حالة عدم القدرة على إصلاح المشكلة "));
        }
        this.connection = ctin;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void DeleteDbIfExists() throws Exception {
        Files.deleteIfExists(Paths.get(FilePath));
    }
}
