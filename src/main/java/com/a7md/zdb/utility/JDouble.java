package com.a7md.zdb.utility;

import com.a7md.zdb.RowTypes.features.Valuable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class JDouble {

    /**
     * this is the default Scale in the bareza System to be the maximum
     * of transactions amounts
     */
    public final static int AppMaxScale = 4;
    /**
     * this is the default Scale in the bareza System to be the maximum user can
     * perform
     */
    public final static int UserMaxScale = AppMaxScale / 2;
    /**
     * this is the default Scale in the bareza System to be the maximum user can
     * perform
     */
    public final static RoundingMode RoundingType = RoundingMode.HALF_UP;
    final private static int UserShowScale = 1;
    /**
     * system currency
     */
    private static final String currency_factory = "V جنيه";
    private final static String currency_factory_replacement = "V";


    public static BigDecimal sum(List<? extends Valuable> list) throws ZSystemError {
        BigDecimal sum = BigDecimal.ZERO;
        String msg_title = "حساب قيمة المدخلات";
        for (Valuable item : list) {
            if (item == null) {
                throw new ZSystemError(msg_title, "مدخل فارغ");
            }
            BigDecimal value = item.getValue();
            if (value.scale() > UserMaxScale) {
                throw new ZSystemError(msg_title, "ِقيمة عشرية أكبر من المتاح");
            } else if (value.doubleValue() == 0) {
                throw new ZSystemError(msg_title, "هناك عناصر قيمتها صفر");
            }
            sum = sum.add(value);
        }

        if (sum.scale() > JDouble.AppMaxScale) {
            throw new ZSystemError(msg_title, "ِقيمة عشرية أكبر من المتاح");
        } else if (sum.doubleValue() == 0) {
            throw new ZSystemError(msg_title, "هناك عناصر قيمتها صفر");
        }
        return sum;
    }


    public static <E> BigDecimal sum(List<E> list, short max_scale, boolean checkZero, Function<E, BigDecimal> getter) throws ZSystemError {
        BigDecimal sum = BigDecimal.ZERO;
        String msg_title = "حساب قيمة المدخلات";

        for (E item : list) {
            if (item == null) {
                throw new ZSystemError(msg_title, "مدخل فارغ");
            }
            BigDecimal value = getter.apply(item);
            if (value.scale() > max_scale) {
                throw new ZSystemError(msg_title, "قيمة عشرية أكبر من المتاح");
            } else if (checkZero && value.doubleValue() == 0) {
                throw new ZSystemError(msg_title, "هناك عناصر قيمتها صفر");
            }
            sum = sum.add(value);
        }

        if (sum.scale() > JDouble.AppMaxScale) {
            throw new ZSystemError(msg_title, "قيمة عشرية أكبر من المتاح");
        } else if (checkZero && sum.doubleValue() == 0) {
            throw new ZSystemError(msg_title, "هناك عناصر قيمتها صفر");
        }
        return sum;
    }

    public static <E> BigDecimal sum(List<E> list, Function<E, BigDecimal> getter) throws ZSystemError {
        BigDecimal sum = BigDecimal.ZERO;
        String msg_title = "حساب قيمة المدخلات";
        for (E item : list) {
            if (item == null) {
                throw new ZSystemError(msg_title, "مدخل فارغ");
            }
            BigDecimal value = getter.apply(item);
            if (value.scale() > UserMaxScale) {
                throw new ZSystemError(msg_title, "ِقيمة عشرية أكبر من المتاح");
            } else if (value.doubleValue() == 0) {
                throw new ZSystemError(msg_title, "هناك عناصر قيمتها صفر");
            }
            sum = sum.add(value);
        }

        if (sum.scale() > JDouble.AppMaxScale) {
            throw new ZSystemError(msg_title, "ِقيمة عشرية أكبر من المتاح");
        } else if (sum.doubleValue() == 0) {
            throw new ZSystemError(msg_title, "هناك عناصر قيمتها صفر");
        }
        return sum;
    }

    public static <E> BigDecimal sum_list(List<E> list, Function<E, BigDecimal> getter) {
        BigDecimal sum = BigDecimal.ZERO;
        for (E item : list) {
            BigDecimal value = getter.apply(item);
            sum = sum.add(value);
        }
        return sum;
    }

    public static String ShowValue(BigDecimal value) {
        value = value.setScale(UserShowScale, RoundingType);
        int signum = value.signum();
        if (signum < 0) {
            return currency_factory.replaceFirst(currency_factory_replacement, "(" + value.abs().toPlainString() + ")");
        } else {
            return currency_factory.replaceFirst(currency_factory_replacement, value.toPlainString());
        }
    }


    public static String showJustDebit(BigDecimal value) {
        value = value.setScale(UserShowScale, RoundingType);
        int signum = value.signum();
        if (signum < 0) {
            return currency_factory.replaceFirst(currency_factory_replacement, "(" + value.abs().toPlainString() + ")");
        } else {
            return "";
        }
    }

    public static String showCreditValue(BigDecimal value) {
        value = value.setScale(UserShowScale, RoundingType);
        int signum = value.signum();
        if (signum > 0) {
            return currency_factory.replaceFirst(currency_factory_replacement, value.toPlainString());
        } else {
            return "";
        }
    }

    public static boolean is_debit(BigDecimal value) {
        return value.signum() < 0;
    }

    public static BigDecimal formatForUser(BigDecimal val) {
        return val.setScale(UserMaxScale, JDouble.RoundingType);
    }
}
