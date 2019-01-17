package com.a7md.zdb.RowTypes.types;

import com.a7md.zdb.utility.JDouble;

import java.math.BigDecimal;

public interface ZForEntryRow extends ZNamedRow {
    BigDecimal getBalance();

    default String getViewBalance() {
        return JDouble.ShowValue(getBalance());
    }
}
