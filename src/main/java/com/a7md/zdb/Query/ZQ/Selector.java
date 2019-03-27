package com.a7md.zdb.Query.ZQ;

import com.a7md.zdb.ZCOL.SqlCol;

import java.util.ArrayList;

public class Selector {
    private final ArrayList<Condition> conditions;
    private final ArrayList<String> combine_Operators = new ArrayList<>();
    private String Limits = "";
    private String OrderStatement = "";

    public Selector(Condition cond) {
        this.conditions = new ArrayList<>();
        conditions.add(cond);
    }

    public Selector(boolean combined, Condition... conditions) {
        this(conditions[0]);
        if (combined) {
            for (int i = 1; i < conditions.length; i++) {
                and(conditions[i]);
            }
        } else {
            for (int i = 1; i < conditions.length; i++) {
                or(conditions[i]);
            }
        }
    }

    public boolean hasValues() {
        return !conditions.isEmpty();
    }

    public String get() {
        String pieces = "";
        if (hasValues()) {
            int size = conditions.size();
            for (int i = 0; i < size; i++) {
                String wherePiece = conditions.get(i).getWherePiece();
                if (wherePiece != null) {
                    pieces += wherePiece;
                    if (i < size - 1) {
                        pieces += combine_Operators.get(i);
                    }
                }
            }
        }
        pieces = (pieces.isEmpty()) ? "" : " where " + pieces;
        pieces += " " + OrderStatement + " " + Limits;
        return pieces;
    }

    public Selector and(Condition condition) {
        conditions.add(condition);
        combine_Operators.add(" and ");
        return this;
    }

    public Selector or(Condition condition) {
        conditions.add(condition);
        combine_Operators.add(" or ");
        return this;
    }

    public Selector setLimits(int offset, int limit) {
        this.Limits = "Limit " + offset + "," + limit;
        return this;
    }

    public void orderBy(SqlCol Col) {
        this.OrderStatement = "order by " + Col.name;
    }

    public void orderDescBy(SqlCol Col) {
        this.OrderStatement = "order by " + Col.name + " desc";
    }
}
