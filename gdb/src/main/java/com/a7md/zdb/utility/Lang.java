package com.a7md.zdb.utility;

import java.util.ArrayList;

public class Lang {

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static public boolean IsNumber(Object Item) {
        try {
            Double.valueOf(Item.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static <X> X random_item(X[] xes) {
        return xes[(int) (Math.random() * (xes.length - 1))];
    }

    public static <X> X random_item(ArrayList<X> xes) {
        return xes.get((int) (Math.random() * (xes.size() - 1)));
    }
}
