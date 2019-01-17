package com.a7md.zdb;

import com.a7md.zdb.utility.ZSystemError;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlHelper extends Link {

    final private Connection connection;
    public String DBName = "";
    String UserName;
    private String Password;

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
            this.UserName = userName;
            this.Password = password;
        } catch (ClassNotFoundException | SQLException e) {
            dbErrorHandler.handle_error(new ZSystemError("خطأ بالاتصال بقاعدة البيانات",
                    "حدث خطأ أثناء الاتصال بقاعدة البيانات يرجى التأكد من ان قاعدة البيانات موجودة على جهاز الكمبيوتر" +
                            "\n او الاتصال بالدعم الفني في حالة عدم القدرة على إصلاح المشكلة "));
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

    //<editor-fold defaultstate="collapsed" desc="backup">
    public void Backupdbtosqlfile(String savePath) throws IOException, InterruptedException, ZSystemError, SQLException {
        savePath = "\"" + savePath + "\"";
        String executeCmd = GetmysqlBinfile("mysqldump") + " -u" + UserName + " -p" + Password + " --databases " + DBName + " -r " + savePath;
        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        int processComplete = runtimeProcess.waitFor();

        if (processComplete != 0) {
            throw new ZSystemError("نسخ البيانات", "فشلت العملية");
        }
    }

    public void Restoredbfromsql(String Path) throws IOException, InterruptedException, ZSystemError, SQLException {
        Path = "\"" + Path + "\"";
        String[] executeCmd = new String[]{GetmysqlBinfile("mysql"), DBName, "-u" + UserName, "-p" + Password, "-e", " source " + Path};
        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        int processComplete = runtimeProcess.waitFor();
        if (processComplete != 0) {
            throw new ZSystemError("استيراد البيانات", "فشلت العملية");
        }
    }

    public String GetmysqlBinfile(String FileName) throws SQLException {
        String val;
        try (Statement statement = getConnection().createStatement()) {
            try (ResultSet r = statement.executeQuery("SELECT @@basedir;")) {
                r.next();
                val = "\"" + r.getString(1) + "\\bin\\" + FileName + "\"";
            }
        }
        return val;
    }
    //</editor-fold>
}
