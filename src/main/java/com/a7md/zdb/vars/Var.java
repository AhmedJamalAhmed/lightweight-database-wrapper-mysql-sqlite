package com.a7md.zdb.vars;

import com.a7md.zdb.RowTypes.GSonRow;

public class Var<V> extends GSonRow<VarData> {
    var_role role;

    public Var() {
        super(new VarData());
    }

    public var_role getRole() {
        return role;
    }

    public void setRole(var_role role) {
        this.role = role;
    }

    public V getValue() {
        return getData().getValue();
    }

    public void setValue(V value) throws Exception {
        getData().setValue(value);
        Vars.app_vars.update(this);
    }

}
