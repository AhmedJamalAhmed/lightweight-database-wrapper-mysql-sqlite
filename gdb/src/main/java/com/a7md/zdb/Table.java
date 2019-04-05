package com.a7md.zdb;

import com.a7md.zdb.Query.ZQ.Condition;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.COL;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.utility.ZSystemError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Table<Item extends ZSqlRow, Resultset,
        DB extends Link<Resultset>,
        IDTYPE extends COL<Resultset, Item, Integer>,
        ColsType extends COL<Resultset, Item, ?>> {

    private final IDTYPE ID;
    public DB db;
    public String TableName;
    private ColsType[] cols;

    public Table(DB link, String TName, IDTYPE ID) {
        this.db = link;
        this.TableName = TName;
        this.ID = ID;
        ID.setMtable(this);
    }

    public ColsType[] getCols() {
        return cols;
    }

    final protected void setCols(ColsType[] cols) {
        this.cols = cols;
    }

    public void onTableCreation() throws Exception {

    }

    public Item fromResultSet(Resultset res) throws Exception {
        Item newElement = createNewElement();
        COL[] cols = getCols();
        for (COL col : cols) {
            col.assign(newElement, res);
        }
        return newElement;
    }

    public abstract Item createNewElement();

    public List<Key> toRow(Item item, boolean withId) {
        ColsType[] cols = getCols();
        ArrayList<Key> list = new ArrayList<Key>();
        if (withId) {
            for (ColsType col : cols) {
                list.add(col.toDbKey(item));
            }
        } else {
            IDTYPE id = getID();
            for (ColsType col : cols) {
                if (col != id) list.add(col.toDbKey(item));
            }
        }
        return list;
    }

    /**
     * @param item
     * @return Item with new inserted id
     * @throws Exception
     */
    public Item insert(Item item) throws Exception {
        int id = item.getId();
        List<Key> keys;
        if (id >= 1) {
            if (ID.exist(id)) {
                throw new ZSystemError("this item is existed");
            }
            keys = toRow(item, true);
        } else {
            keys = toRow(item, false);
        }

        int i = db.AddRow(this, keys);
        item.setId(i);
        return item;
    }

    public boolean insertOrUpdate(Item item) throws Throwable {
        boolean selectexists = ID.exist(item.getId());
        if (selectexists) update(item);
        else insert(item);
        return !selectexists;
    }

    public long update(Item item) throws Exception {
        List<Key> keys = toRow(item, true);
        int id = item.getId();
        boolean selectexists = ID.exist(id);
        if (selectexists) {
            db.UpdateRow(ID.equal(id), keys);
            return id;
        } else {
            throw new ZSystemError("not exist");
        }
    }

    final public void delete(int id) throws Exception {
        validate_delete(id);
        db.DeleteRow(ID.equal(id));
    }

    final public void delete(Item item) throws Exception {
        delete(item.getId());
    }

    protected void validate_delete(int id) throws Exception {
    }

    public final IDTYPE getID() {
        return this.ID;
    }

    public Item getById(int id) throws Exception {
        return (Item) db.row(getID().equal(id));
    }

    public List<Item> list(Condition where) throws Exception {
        return db.list(this, new Selector(where));
    }

    public List<Item> list() throws Exception {
        return db.list(this);
    }

    public List<Item> list(Condition... conditions) throws Exception {
        return list(new Selector(true, conditions));
    }

    public List<Item> all() throws Exception {
        return db.list(this, null);
    }

    public List<Item> list(Selector where) throws Exception {
        return db.list(this, where);
    }

    public void clearTable() throws SQLException {
        this.db.clearTable(this);
    }

    public Item getItem(Selector installmentsRecorder) throws Exception {
        return (Item) db.row(this, installmentsRecorder);
    }


    public Item getItem(Condition condition) throws Exception {
        return getItem(new Selector(condition));
    }
}
