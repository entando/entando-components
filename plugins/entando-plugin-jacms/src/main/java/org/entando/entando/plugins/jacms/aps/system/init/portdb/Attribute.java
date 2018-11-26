package org.entando.entando.plugins.jacms.aps.system.init.portdb;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.AttributeType;

@DatabaseTable(tableName = Attribute.TABLE_NAME)
public class Attribute {
    public static final String TABLE_NAME = "attributes";

    @DatabaseField(columnName = "id",
            dataType = DataType.LONG_OBJ,
            canBeNull = false, id = true)
    private Long id;

    @DatabaseField(columnName = "type",
            dataType = DataType.ENUM_STRING,
            width = 30,
            canBeNull = false)
    private AttributeType type;

    @DatabaseField(columnName = "code",
            dataType = DataType.STRING,
            width = 3,
            canBeNull = false)
    private String code;

    @DatabaseField(columnName = "name",
            dataType = DataType.STRING,
            width = 30,
            canBeNull = false)
    private String name;

    @DatabaseField(columnName = "mandatory",
            dataType = DataType.BOOLEAN_OBJ,
            canBeNull = false)
    private Boolean mandatory;

    @DatabaseField(columnName = "searchable",
            dataType = DataType.BOOLEAN_OBJ,
            canBeNull = false)
    private Boolean searchable;

    @DatabaseField(columnName = "filterable",
            dataType = DataType.BOOLEAN_OBJ,
            canBeNull = false)
    private Boolean filterable;
}
