package com.a7md.zdb.ZCOL.viewProperties;

import com.a7md.zdb.Additive;
import com.a7md.zdb.ZCOL._Object;
import com.a7md.zdb.properties.WritableProperty;

public class _ViewProperties<E extends Additive> extends _Object<E, ViewProperties> {

    public _ViewProperties(String Name, String title) {
        super(Name, 8000, ViewProperties.class, new WritableProperty<>(title, E::getViewProperties, E::setViewProperties));
    }
}
