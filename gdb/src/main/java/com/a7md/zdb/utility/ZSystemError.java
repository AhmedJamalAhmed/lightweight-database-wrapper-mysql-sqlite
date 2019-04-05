package com.a7md.zdb.utility;

public class ZSystemError extends Exception {

    String Title;

    public ZSystemError(String Title, String msg) {
        super(msg);
        this.Title = Title;
    }

    public ZSystemError(String msg) {
        super(msg);
    }

    public String getTitle() {
        if (Title == null) {
            return "error";
        }
        return Title;
    }
}
