package com.a7md.zdb.RowTypes.features;

import com.a7md.zdb.utility.JDateTime;

import java.time.LocalDateTime;

public interface Timed {

    default String getDate() {
        return JDateTime.str_date(getDateTime());
    }

    default String getTime() {
        return JDateTime.str_time(getDateTime());
    }

    LocalDateTime getDateTime();
}
