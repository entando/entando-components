package org.entando.entando.plugins.jacms.aps.system.init.portdb;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.DefaultContentModel;

@DatabaseTable(tableName = ContentType.TABLE_NAME)
public class ContentType {
    public static final String TABLE_NAME = "contenttypes";

    @DatabaseField(columnName = "id",
            dataType = DataType.LONG_OBJ,
            canBeNull = false, id = true)
    private Long id;

    @DatabaseField(columnName = "name",
            dataType = DataType.STRING,
            width = 30,
            canBeNull = false)
    private String name;

    @DatabaseField(columnName = "code",
            dataType = DataType.STRING,
            width = 3,
            canBeNull = false)
    private String code;

    @DatabaseField(columnName = "default_content_model",
            dataType = DataType.ENUM_STRING,
            width = 30)
    private DefaultContentModel defaultContentModel;

    @DatabaseField(columnName = "default_content_model_list",
            dataType = DataType.ENUM_STRING,
            width = 30)
    private DefaultContentModel defaultContentModelList;
}
