package com.a7md.zdb.RowTypes;

import com.a7md.zdb.RowTypes.types.ZNamedRow;

import java.time.LocalDateTime;

public class ZItem implements ZNamedRow, LastModified {
    private int id;
    private String name;
    private LocalDateTime lastModified;

    @Override
    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "(" + getId() + ") " + getName();
    }
}
