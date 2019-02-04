package com.a7md.zdb;

import com.a7md.zdb.RowTypes.types.ZSqlRow;
import com.a7md.zdb.ZCOL.viewProperties.ViewProperties;

public interface Additive extends ZSqlRow {
    ViewProperties getViewProperties();

    void setViewProperties(ViewProperties viewProperties);
}
