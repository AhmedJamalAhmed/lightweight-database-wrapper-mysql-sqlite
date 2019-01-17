package com.a7md.zdb.RowTypes;

import com.a7md.zdb.utility.JDateTime;

import java.time.LocalDateTime;

public interface last_updated_time {

    default String getLastUpdatedDate() {
        return JDateTime.str_date(getLastUpdated());
    }

    default String getLastUpdatedTime() {
        return JDateTime.str_time(getLastUpdated());
    }

    default String getLastUpdatedDateTime() {
        return JDateTime.str_date_time(getLastUpdated());
    }

    LocalDateTime getLastUpdated();
}
