package com.a7md.zdb.utility;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class IO {

    static public String ReadFileFromRecourse(String FileName, Object FileClass) {
        String Content = "";
        try {
            Content = ReadFileFromRecourse(new InputStreamReader(FileClass.getClass().getResourceAsStream(FileName), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            if (Content.equals("")) {
                Content = ReadFileFromRecourse(new InputStreamReader(FileClass.getClass().getResourceAsStream(FileName)));
            }
        }
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
}
