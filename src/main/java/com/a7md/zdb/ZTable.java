package com.a7md.zdb;

import com.a7md.zdb.Query.Select;
import com.a7md.zdb.Query.ZQ.Condition;
import com.a7md.zdb.Query.ZQ.ZWhere;
import com.a7md.zdb.RowTypes.types.ZSqlRow;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._ID_AI;
import com.a7md.zdb.utility.ZSystemError;

import java.sql.ResultSet;
import java.util.ArrayList;

abstract public class ZTable<Item extends ZSqlRow> {

    private final _ID_AI<Item> ID;
    public Link db;
    public String TableName;
    public SqlCol<Item, ?>[] cols;

    public ZTable(Link link, String TName, _ID_AI<Item> ID) {
        this.db = link;
        this.TableName = TName;
        this.ID = ID;
        ID.setMtable(this);
    }

    public SqlCol<Item, ?>[] getCols() {
        return cols;
    }

    protected void register(SqlCol<Item, ?>... otherCols) {
        SqlCol<Item, ?>[] cols = new SqlCol[otherCols.length + 1];
        cols[0] = ID;
        int i = 1;
        for (SqlCol<Item, ?> col : otherCols) {
            cols[i] = col;
            cols[i].setMtable(this);
            i++;
        }
        this.cols = otherCols;
        db.registerTable(this, cols);
    }

    public void onTableCreation() throws Exception {
        System.out.println("-> creating table " + this.TableName + " ...");
    }

    public Item fromResultSet(ResultSet res) throws Exception {
        Item newElement = createNewElement();
        ID.assign(newElement, res); ///must assign the id
        SqlCol<Item, ?>[] cols = getCols();
        for (SqlCol<Item, ?> col : cols) {
            col.assign(newElement, res);
        }
        return newElement;
    }

    public abstract Item createNewElement();

    public Key[] toRow(Item item) {
        SqlCol<Item, ?>[] cols = getCols();
        Key[] keys = new Key[cols.length];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = cols[i].toDbKey(item);
        }
        return keys;
    }

    /**
     * @param item
     * @return Item with new inserted id
     * @throws Exception
     */
    public Item insert(Item item) throws Exception {
        Key[] keys = toRow(item);
        for (Key key : keys) {
            if (key.ColName.equals(getID().name)) {
                throw new ZSystemError("asdask aksjdklasjkldjaskldj to delete on release");
            }
        }
        int id = item.getId();
        boolean selectexists = ID.exist(id);
        if (selectexists) {
            throw new ZSystemError("موجود من قبل");
        } else {
            int i = db.AddRow(this, keys);
            item.setId(i);
            return item;
        }
    }

    public boolean addOrEdit(Item item) throws Throwable {
        boolean selectexists = ID.exist(item.getId());
        if (selectexists) update(item);
        else insert(item);
        return !selectexists;
    }

    final public int update(Item item) throws Exception {
        Key[] keys = toRow(item);
        int id = item.getId();
        boolean selectexists = ID.exist(id);
        if (selectexists) {
            db.UpdateRow(ID.equal(id), keys);
            return id;
        } else {
            throw new ZSystemError("غير موجود");
        }
    }

    final public void delete(int id) throws Exception {
        validate_delete(id);
        db.DeleteRow(ID.equal(id));
    }

    final public void delete(Item item) throws Exception {
        validate_delete(item.getId());
        db.DeleteRow(ID.equal(item.getId()));
    }

    protected void validate_delete(int id) throws Exception {

    }

    public final _ID_AI getID() {
        return this.ID;
    }

    public Item getById(int id) throws Exception {
        return Select.row(getID().equal(id));
    }

    public ArrayList<Item> list(Condition where) throws Exception {
        return Select.list(this, new ZWhere(where));
    }

    public ArrayList<Item> list() throws Exception {
        return Select.list(this);
    }

    public ArrayList<Item> list(Condition... conditions) throws Exception {
        return list(new ZWhere(conditions));
    }

    public ArrayList<Item> all() throws Exception {
        return Select.list(this, null);
    }

    public ArrayList<Item> list(ZWhere where) throws Exception {
        return Select.list(this, where);
    }

}
