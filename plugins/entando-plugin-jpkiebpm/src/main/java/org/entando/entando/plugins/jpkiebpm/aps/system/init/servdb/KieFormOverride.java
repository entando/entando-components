package org.entando.entando.plugins.jpkiebpm.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable(tableName = KieFormOverride.TABLE_NAME)
public class KieFormOverride {

    public static final String TABLE_NAME = "jpkiebpm_kieformoverride";

    public KieFormOverride() {
    }

    @DatabaseField(columnName = "id",
            dataType = DataType.INTEGER,
            canBeNull = false, id = true)
    private int id;

    @DatabaseField(columnName = "widgetinfoid",
            dataType = DataType.INTEGER,
            canBeNull = false)
    private int widgetInfoId;

    @DatabaseField(columnName = "date",
            dataType = DataType.DATE,
            canBeNull = false)
    private Date date;

    @DatabaseField(columnName = "field",
            dataType = DataType.STRING,
            width = 255, canBeNull = false)
    private String field;

    @DatabaseField(columnName = "containerid",
            dataType = DataType.LONG_STRING,
            canBeNull = false)
    private String containerId;

    @DatabaseField(columnName = "processid",
            dataType = DataType.LONG_STRING,
            canBeNull = false)
    private String processId;

    @DatabaseField(columnName = "sourceid",
            dataType = DataType.LONG_STRING,
            canBeNull = false)
    private String sourceid;

    @DatabaseField(columnName = "override",
            dataType = DataType.LONG_STRING,
            canBeNull = false)
    private String override;

    @DatabaseField(columnName = "active",
            dataType = DataType.INTEGER,
            canBeNull = false)
    private int active;

    @DatabaseField(columnName = "online",
            dataType = DataType.INTEGER,
            canBeNull = false)
    private int online;
}
