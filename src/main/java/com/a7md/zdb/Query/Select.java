package com.a7md.zdb.Query;

import com.a7md.zdb.Query.ZQ.Condition;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.ZWhere;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._Decimal;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.ZTable;
import com.a7md.zdb.utility.ZSystemError;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Select {

    private static String build_join(Join join, String sql_cols) {
        SqlCol c1 = join.first_col;
        SqlCol c2 = join.Second_col;
        return "SELECT " + sql_cols + " FROM " +
                c1.mtable.TableName + " join " + c2.mtable.TableName + " on "
                + c1.mtable.TableName + "." + c1.name + "="
                + c2.mtable.TableName + "." + c2.name + " ";
    }


    public static <U extends ZSqlRow> ArrayList<U> list(ZTable<U> table, ZWhere where) throws Exception {
        String sql = "SELECT * FROM " + table.TableName;
        if (where != null) {
            sql += where.get();
        }
        return table.db.getResult(sql, r -> {
            ArrayList<U> list = new ArrayList<>();
            while (r.next()) {
                list.add(table.fromResultSet(r));
            }
            return list;
        });
    }

    @SuppressWarnings("unchecked")
    public static <U extends ZSqlRow> U row(Equal where) throws Exception {
        ArrayList<U> list = list(where.col.mtable, new ZWhere(where));
        if (list.size() != 1) {
            throw new ZSystemError("query of " + new ZWhere(where).get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public static void query(JoinHandler bind, ZWhere where) throws Exception {
        String sql = build_join(bind, "*") + (where != null ? where.get() : "");
        bind.first_col.mtable.db.getResult(sql, bind);
    }

    public static <U extends ZSqlRow> ArrayList<U> list(ZTable<U> table) throws Exception {
        return list(table, null);
    }

    static public <T> T value(SqlCol<?, T> of_col, ZWhere where) throws Exception {
        String SQl = "SELECT " + of_col.name + " From " + of_col.mtable.TableName + where.get();
        return of_col.mtable.db.getResult(SQl,
                r -> {
                    if (r.next()) {
                        return of_col.get(r);
                    } else {
                        return null;
                    }
                });
    }

    static public <T> T value(SqlCol<?, T> col, int id) throws Exception {
        return value(col, new ZWhere(col.mtable.getID().equal(id)));
    }

    static public BigDecimal sum(_Decimal col, Equal where) throws Exception {
        return sum(col, new ZWhere(where));
    }

    static public BigDecimal sum(_Decimal col, ZWhere where) throws Exception {
        _Decimal decimal = new _Decimal<>("sum(" + col.name + ")", null);
        decimal.setMtable(col.mtable);
        String sql = "select sum(" + col.name + ") from " + col.mtable.TableName
                + (where == null ? "" : where.get());
        return col.mtable.db.getResult(sql, r -> {
            if (r.next()) {
                return decimal.get(r);
            } else {
                return BigDecimal.ZERO;
            }
        });
    }

    static public BigDecimal sum(_Decimal col, Join join, ZWhere where) throws Exception {
        _Decimal decimal = new _Decimal<>("sum(" + col.name + ")", null);
        decimal.setMtable(col.mtable);
        String sql = build_join(join, decimal.name) + where.get();
        return col.mtable.db.getResult(sql, r -> {
            if (r.next()) {
                return decimal.get(r);
            } else {
                return BigDecimal.ZERO;
            }
        });
    }

    static public BigDecimal sum(_Decimal col) throws Exception {
        return sum(col, (ZWhere) null);
    }

    static public int count(ZTable table, ZWhere Where) throws Exception {
        String SQL = "select count(0) from " + table.TableName + (Where == null ? "" : Where.get());
        return table.db.getResult(SQL, r -> {
            if (r.next()) {
                return r.getInt(1);
            } else {
                return -1;
            }
        });
    }

    static public int count(ZTable table, Condition condition) throws Exception {
        return count(table, new ZWhere(condition));
    }


    static public int count(Join join, ZWhere Where) throws Exception {
        String SQL = build_join(join, "count(0)") + (Where == null ? "" : Where.get());
        return join.first_col.mtable.db.getResult(SQL, r -> {
            if (r.next()) {
                return r.getInt(1);
            } else {
                return -1;
            }
        });
    }


    static public boolean exist(Equal Ident) throws Exception {
        return Ident.col.mtable.db.getResult("SELECT EXISTS(SELECT 1 FROM " + Ident.col.mtable.TableName + new ZWhere(Ident).get() + ")",
                r -> {
                    if (r.next()) {
                        return r.getInt(1) == 1;
                    } else {
                        return false;
                    }
                }
        );
    }


    static public <V> ArrayList<V> distinctValues(SqlCol<?, V> col) throws Exception {
        return col.mtable.db.getResult("SELECT DISTINCT " + col.name + " from " + col.mtable.TableName,
                r -> {
                    ArrayList<V> list = new ArrayList<>();
                    while (r.next()) {
                        list.add(col.get(r));
                    }
                    return list;
                }
        );
    }
}
