/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = DashboardTable.TABLE_NAME)
public class DashboardTable {

    public DashboardTable() {
    }

    @DatabaseField(columnName = "id",
        dataType = DataType.INTEGER,
        canBeNull = false, id = true)
    private int _id;

    @DatabaseField(columnName = "widgettitle",
        dataType = DataType.STRING,
        width = 50, canBeNull = false)
    private String _widgetTitle;

    @DatabaseField(columnName = "datasource",
        dataType = DataType.STRING,
        width = 50, canBeNull = false)
    private String _datasource;

    @DatabaseField(columnName = "context",
        dataType = DataType.STRING,
        width = 50, canBeNull = false)
    private String _context;

    @DatabaseField(columnName = "download",
        dataType = DataType.INTEGER,
        width = 1, canBeNull = false)
    private int _download;

    @DatabaseField(columnName = "filter",
        dataType = DataType.INTEGER,
        width = 1, canBeNull = false)
    private int _filter;

    @DatabaseField(columnName = "allcolumns",
        dataType = DataType.INTEGER,
        width = 1, canBeNull = false)
    private int _allColumns;

    @DatabaseField(columnName = "columns",
        dataType = DataType.STRING,
        width = 255, canBeNull = false)
    private String _columns;


    public static final String TABLE_NAME = "dashboard_table";
}
