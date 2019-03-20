
/*
 *
 *  * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *  *
 *  * This library is free software; you can redistribute it and/or modify it under
 *  * the terms of the GNU Lesser General Public License as published by the Free
 *  * Software Foundation; either version 2.1 of the License, or (at your option)
 *  * any later version.
 *  *
 *  * This library is distributed in the hope that it will be useful, but WITHOUT
 *  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  * details.
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

  @DatabaseField(columnName = "datasourcecode",
      dataType = DataType.STRING,
      width = 255, canBeNull = false, id=true)
  private String datasourceCode;

  @DatabaseField(columnName = "datasourceuri",
      dataType = DataType.STRING,
      width = 255, canBeNull = true)
  private String datasourceURI;

  @DatabaseField(columnName = "status",
      dataType = DataType.STRING,
      width = 50, canBeNull = true)
  private String status;


  public static final String TABLE_NAME = "dashboard_config_datasource";
}
