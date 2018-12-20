package org.entando.entando.plugins.jacms.aps.system.init.portdb.enums;

import com.fasterxml.jackson.annotation.*;

public enum DefaultContentModel {
    FULL("Full"),
    LISTS("Lists");

    private String value;

    DefaultContentModel(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static DefaultContentModel fromValue(String text) {
        for (DefaultContentModel b : DefaultContentModel.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}


