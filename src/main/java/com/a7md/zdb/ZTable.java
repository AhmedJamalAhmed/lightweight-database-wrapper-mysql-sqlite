package com.a7md.zdb;

import com.a7md.zdb.Query.Select;
import com.a7md.zdb.Query.ZQ.Condition;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._ID_AI;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.utility.ZSystemError;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        this.cols = cols;
        db.registerTable(this, cols);
    }

    public void onTableCreation() throws Exception {
        System.out.println("-> creating table " + this.TableName + " ...");
    }

    public Item fromResultSet(ResultSet res) throws Exception {
        Item newElement = createNewElement();
        SqlCol<Item, ?>[] cols = getCols();
        for (SqlCol<Item, ?> col : cols) {
            col.assign(newElement, res);
        }
        return newElement;
    }

    public abstract Item createNewElement();

    public List<Key> toRow(Item item, boolean withId) {
        return Arrays.stream(getCols()).
                filter(f -> withId || f != ID)
                .map(s -> s.toDbKey(item)).collect(Collectors.toList());
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

    public int update(Item item) throws Exception {
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

    public final _ID_AI getID() {
        return this.ID;
    }

    public Item getById(int id) throws Exception {
        return Select.row(getID().equal(id));
    }

    public List<Item> list(Condition where) throws Exception {
        return Select.list(this, new Selector(where));
    }

    public List<Item> list() throws Exception {
        return Select.list(this);
    }

    public List<Item> list(Condition... conditions) throws Exception {
        return list(new Selector(true, conditions));
    }

    public List<Item> all() throws Exception {
        return Select.list(this, null);
    }

    public List<Item> list(Selector where) throws Exception {
        return Select.list(this, where);
    }

    public void clearTable() throws SQLException {
        this.db.clearTable(this);
    }

    public Item getItem(Selector installmentsRecorder) throws Exception {
        return Select.row(this, installmentsRecorder);
    }

    public SqlCol<Item, ?>[] getFilterCols() {
        return new SqlCol[]{
                this.getID()
        };
    }

    public Item getItem(Condition condition) throws Exception {
        return getItem(new Selector(condition));
    }
}
