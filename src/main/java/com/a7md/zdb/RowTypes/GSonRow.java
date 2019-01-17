package com.a7md.zdb.RowTypes;

import com.a7md.zdb.GSonObject;
import com.a7md.zdb.RowTypes.types.ZNamedRow;

import java.time.LocalDateTime;

abstract public class GSonRow<t extends GSonObject> implements ZNamedRow, last_updated_time {

    private int id;
    private String name;
    private t data;
    private LocalDateTime lastUpdated;

    public GSonRow(t data) {
        this.data = data;
    }

    public t getData() {
        return data;
    }

    public void setData(t data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
