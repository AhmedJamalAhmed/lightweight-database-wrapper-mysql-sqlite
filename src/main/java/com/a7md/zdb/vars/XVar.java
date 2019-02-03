package com.a7md.zdb.vars;

import com.a7md.zdb.Query.Select;

public class XVar<S> {
    private final int id;

    XVar(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public S getVal() {
        VarData value = null;
        try {
            value = Select.value(Vars.app_vars.data, getId());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value.getValue();
    }

    public void setVal(S val) throws Exception {
        Var<S> var = Vars.getVar(getId());
        var.setValue(val);
    }
}
