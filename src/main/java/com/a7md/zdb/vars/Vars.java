package com.a7md.zdb.vars;

import com.a7md.zdb.Link;
import com.a7md.zdb.Query.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Vars {

    public static app_vars app_vars;

    public static void init(Link link) {
        app_vars = new app_vars(link);
    }

    @SuppressWarnings("unchecked")
    public static <t> Var<t> getVar(int id) throws Exception {
        return app_vars.getById(id);
    }

    private static <s> XVar<s> _new(String name, converter converter, s val, String notes, var_role role) throws Exception {
        int id;
        try {
            Var row = Select.row(app_vars.name.equal(name));
            id = row.getId();
        } catch (Throwable e) {
            /// no handling for error here because it's fine if the variable does'nt exist;
            id = -1;
        }

        if (id == -1) {
            VarData data = new VarData();
            data.setVal_type_name(converter);
            data.setVal(converter.toStr(val));
            data.setNotes(notes);

            Var<s> objectvar = new Var<>();

            objectvar.setId(-1);
            objectvar.setName(name);
            objectvar.setData(data);
            objectvar.setLastUpdated(LocalDateTime.now());
            objectvar.setRole(role);

            Var add = app_vars.insert(objectvar);
            id = add.getId();
        }
        if (id == -1) return null;
        return new XVar<>(id);
    }

    /**
     * {@link #_app(String name, converter converter, s val)}
     * for app vars
     */
    private static <s> XVar<s> _app(String name, converter converter, s val) throws Exception {
        return _new(name, converter, val, "", var_role.app);
    }

    public static XVar<String> _app(String name, String val) throws Exception {
        return _app(name, converter.text, val);
    }

    public static XVar<BigDecimal> _app(String name, BigDecimal val) throws Exception {
        return _app(name, converter.decimal, val);
    }

    /**
     * {@link #_user(String name, converter converter, s val, String notes)}
     * for user vars
     */
    private static <s> XVar<s> _user(String name, converter converter, s val, String notes) throws Exception {
        return _new(name, converter, val, notes, var_role.user);
    }

    public static XVar<String> _user(String name, String val, String notes) throws Exception {
        return _user(name, converter.text, val, notes);
    }

    public static XVar<Boolean> _user(String name, Boolean val, String notes) throws Exception {
        return _user(name, converter.bool, val, notes);
    }
}
