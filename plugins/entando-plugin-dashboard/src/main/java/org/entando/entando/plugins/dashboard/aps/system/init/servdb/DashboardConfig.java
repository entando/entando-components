/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = DashboardConfig.TABLE_NAME)
public class DashboardConfig {

    public DashboardConfig() {
    }

    @DatabaseField(columnName = "id",
        dataType = DataType.INTEGER,
        canBeNull = false, id = true)
    private int _id;

    @DatabaseField(columnName = "serverdescription",
        dataType = DataType.STRING,
        width = 30, canBeNull = false)
    private String _serverDescription;

    @DatabaseField(columnName = "serveruri",
        dataType = DataType.STRING,
        width = 255, canBeNull = false)
    private String _serverURI;

    @DatabaseField(columnName = "username",
        dataType = DataType.STRING,
        width = 50, canBeNull = true)
    private String _username;

    @DatabaseField(columnName = "password",
        dataType = DataType.STRING,
        width = 50, canBeNull = true)
    private String _password;

    @DatabaseField(columnName = "token",
        dataType = DataType.STRING,
        width = 255, canBeNull = true)
    private String _token;

    @DatabaseField(columnName = "timeconnection",
        dataType = DataType.INTEGER,
        canBeNull = true)
    private int _timeConnection;

    @DatabaseField(columnName = "active",
        dataType = DataType.INTEGER,
        canBeNull = true)
    private int _active;

    @DatabaseField(columnName = "debug",
        dataType = DataType.INTEGER,
        canBeNull = true)
    private int _debug;


    public static final String TABLE_NAME = "dashboard_config";
}
