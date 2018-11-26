package org.entando.entando.plugins.jacms.aps.system.init.portdb.enums;

import com.fasterxml.jackson.annotation.*;

public enum AttributeType {
    ATTACH("Attach"),

    AUTHOR("Author"),

    BOOLEAN("Boolean"),

    CHECKBOX("CheckBox"),

    COMPOSITE("Composite"),

    COORDS("Coords"),

    DATE("Date"),

    ENUMERATOR("Enumerator"),

    ENUMERATORMAP("EnumeratorMap"),

    HYPERTEXT("Hypertext"),

    IMAGE("Image"),

    LINK("Link"),

    LIST("List"),

    LONGTEXT("Longtext"),

    MONOLIST("Monolist"),

    MONOTEXT("Monotext"),

    NUMBER("Number"),

    TEXT("Text"),

    THREESTATE("ThreeState"),

    TIME("Time"),

    TIMESTAMP("Timestamp");

    private String value;

    AttributeType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static AttributeType fromValue(String text) {
        for (AttributeType b : AttributeType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
