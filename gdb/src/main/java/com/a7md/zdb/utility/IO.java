package com.a7md.zdb.utility;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class IO {

    public static void writeFile(File file, String text) throws IOException {
        FileWriter myWriter = new FileWriter(file);
        myWriter.write(text);
        myWriter.close();
    }

    static public String ReadFileFromRecourse(String FileName, Object FileClass) {
        String Content = "";
        Content = ReadFileFromRecourse(new InputStreamReader(FileClass.getClass().getResourceAsStream(FileName), StandardCharsets.UTF_8));
        return Content;
    }

    static public String ReadFileFromRecourse(Reader reader) {
        String content = "";
        StringBuffer temp = new StringBuffer(1024);
        char[] buffer = new char[1024];
        int read;
        try {
            while ((read = reader.read(buffer, 0, buffer.length)) != -1) {
                temp.append(buffer, 0, read);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        content = temp.toString();
        return content;
    }

    public static void print(String page) throws IOException {
        File tempDir = new File("PrintingTemp/");
        tempDir.mkdir();
        File file = new File(tempDir.getAbsolutePath() + "/temp.html");
        IO.writeFile(file, page);
        Desktop.getDesktop().print(file);
        Desktop.getDesktop().open(tempDir);
    }
}
