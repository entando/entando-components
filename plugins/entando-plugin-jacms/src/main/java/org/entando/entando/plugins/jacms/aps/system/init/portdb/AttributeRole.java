package org.entando.entando.plugins.jacms.aps.system.init.portdb;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = AttributeRole.TABLE_NAME)
public class AttributeRole {
    public static final String TABLE_NAME = "attributeroles";

    @DatabaseField(columnName = "id",
            dataType = DataType.LONG_OBJ,
            canBeNull = false, id = true)
    private Long id;

    @DatabaseField(columnName = "name",
            dataType = DataType.STRING,
            width = 30,
            canBeNull = false)
    private String name;
}
