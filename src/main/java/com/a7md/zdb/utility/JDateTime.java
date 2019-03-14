package com.a7md.zdb.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface JDateTime {

    String db_time_stamp_format = "yyyy-MM-dd HH:mm:ss.S";
    DateTimeFormatter db_time_stamp_formatter = DateTimeFormatter.ofPattern(db_time_stamp_format);
    String time_format = "hh:mm a";
    DateTimeFormatter time_formatter = DateTimeFormatter.ofPattern(time_format);
    String date_format = "YYYY/MM/dd"; // yyyy/MM/dd
    DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern(date_format);
    String date_time_format = time_format + " | " + date_format;
    DateTimeFormatter date_time_formatter = DateTimeFormatter.ofPattern(date_time_format);

    static String str_time(LocalDateTime value) {
        return value.toLocalTime().format(time_formatter);
    }

    static String str_date_time(LocalDateTime value) {
        if (value == null) return "غير محدد";
        return value.format(date_time_formatter);
    }

    static String str_date(LocalDate value) {
        return value.format(date_formatter);
    }

    static String str_date(LocalDateTime value) {
        return value.toLocalDate().format(date_formatter);
    }

    static String FormattedNow() {
        return str_date_time(LocalDateTime.now());
    }

    static String DB_TIMESTAMP(LocalDateTime value) {
        return value.format(db_time_stamp_formatter);
    }

}
