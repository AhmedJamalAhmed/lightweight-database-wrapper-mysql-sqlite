package com.a7md.zdb.vars;

import com.a7md.zdb.GSonObject;

public class VarData implements GSonObject {

    private converter val_type_name;
    private String val;
    private String notes;

    public VarData() {
    }

    public converter getVal_type_name() {
        return val_type_name;
    }

    public void setVal_type_name(converter val_type_name) {
        this.val_type_name = val_type_name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public <t> t getValue() {
        return val_type_name.val_of(val);
    }

    public <t> void setValue(t val) {
        this.val = val_type_name.toStr(val);
    }
}
