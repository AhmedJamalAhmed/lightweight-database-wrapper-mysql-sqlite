package com.a7md.zdb.ZCOL;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.RowTypes.types.ZSqlRow;
import com.a7md.zdb.ZCOL._Custom;
import com.a7md.zdb.properties.WritableProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class _Array<E extends ZSqlRow, ArrayElement> extends _Custom<E, ArrayList<ArrayElement>> {


    public _Array(String name, int Size, WritableProperty<E, ArrayList<ArrayElement>> property) {
        super(name, Size, property);
    }

    @Override
    public Equal equal(ArrayList<ArrayElement> val) {
        return new Equal(this, val);
    }

    @Override
    public ArrayList<ArrayElement> get(ResultSet resultSet) throws SQLException {
        ArrayElement[] array = (ArrayElement[]) resultSet.getArray(name).getArray();
        return new ArrayList<>(Arrays.asList(array));
    }
}
