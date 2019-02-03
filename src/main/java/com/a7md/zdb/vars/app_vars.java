package com.a7md.zdb.vars;

import com.a7md.zdb.Link;
import com.a7md.zdb.ZCOL._EnumVal;
import com.a7md.zdb.ZGSonTable;
import com.a7md.zdb.properties.WritableProperty;

public class app_vars extends ZGSonTable<VarData, Var> {

    final _EnumVal<Var, var_role> role = new _EnumVal<>("role", var_role.class,
            new WritableProperty<>("المدير", Var::getRole, Var::setRole));

    public app_vars(Link link) {
        super(link, "app_vars", VarData.class);
        register(role);
    }

    @Override
    public Var createNewElement() {
        return new Var();
    }
}
