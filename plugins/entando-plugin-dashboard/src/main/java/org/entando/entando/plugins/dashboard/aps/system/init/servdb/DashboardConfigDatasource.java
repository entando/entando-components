/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = DashboardConfigDatasource.TABLE_NAME)
public class DashboardConfigDatasource {

  public DashboardConfigDatasource() {
  }

  @DatabaseField(columnName = "fk_dashboard_config",
      dataType = DataType.INTEGER,
      canBeNull = false)
  private int fkDashboardConfig;

  @DatabaseField(columnName = "datasource",
      dataType = DataType.STRING,
      width = 30, canBeNull = false, id=true)
  private String datasource;

  @DatabaseField(columnName = "datasourceuri",
      dataType = DataType.STRING,
      width = 255, canBeNull = false)
  private String datasourceURI;

  @DatabaseField(columnName = "status",
      dataType = DataType.STRING,
      width = 50, canBeNull = true)
  private String status;


  public static final String TABLE_NAME = "dashboard_config_datasource";
}
