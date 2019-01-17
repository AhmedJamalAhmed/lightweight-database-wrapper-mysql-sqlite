package com.a7md.zdb;

import java.util.function.Function;

public abstract class ValidateFunction<T, R> implements Function<T, R> {
    final String msg;

    public ValidateFunction(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
