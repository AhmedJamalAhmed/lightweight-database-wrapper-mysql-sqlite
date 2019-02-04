package com.a7md.zdb.ZCOL.viewProperties;

public class ViewProperties {
    public ViewProperty[] properties = new ViewProperty[0];

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ViewProperty property : properties) {
            builder.append(property.title).append("-").append(property.value).append(System.lineSeparator());
        }
        return builder.toString();
    }
}
