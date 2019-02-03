package com.a7md.zdb.vars;

import java.math.BigDecimal;

public enum converter {
    text(new con<String>() {
        @Override
        public String getVal(String s) {
            return s;
        }

        @Override
        public String setVal(String o) {
            return o;
        }

        @Override
        public String setDefault() {
            return "";
        }
    }),
    decimal(new con<BigDecimal>() {
        @Override
        public BigDecimal getVal(String v) {
            return new BigDecimal(v);
        }

        @Override
        public String setVal(BigDecimal v) {
            return v.toPlainString();
        }

        @Override
        public String setDefault() {
            return "0";
        }
    }),
    bool(new con<Boolean>() {
        @Override
        public Boolean getVal(String v) {
            return Boolean.valueOf(v);
        }

        @Override
        public String setVal(Boolean v) {
            return v.toString();
        }

        @Override
        public String setDefault() {
            return Boolean.FALSE.toString();
        }
    });
    public final con factory;

    converter(con factory) {
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    public <x> String toStr(x val) {
        if (val == null) {
            return factory.setDefault();
        } else {
            return ((con<x>) factory).setVal(val);
        }
    }

    @SuppressWarnings("unchecked")
    public <x> x val_of(String val) {
        return ((con<x>) factory).getVal(val);
    }

    interface con<x> {
        x getVal(String v);

        String setVal(x v);

        String setDefault();
    }
}
