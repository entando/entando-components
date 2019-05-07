
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

package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

import com.agiletec.aps.system.common.FieldSearchFilter;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DatasourcesConfigRequest;

import java.util.List;

public interface IDashboardConfigDAO {

  int countDatasourceByName(String datasourceName);

  int countDatasourceByCode(String datasourceCode);

  List<Integer> searchDashboardConfigs(FieldSearchFilter[] filters);

  DashboardConfig loadDashboardConfig(int id);

  List<Integer> loadDashboardConfigs();

  void removeDashboardConfig(int id);

  void updateDashboardConfig(DashboardConfig dashboardConfig);

  void insertDashboardConfig(DashboardConfig dashboardConfig);

  int countDashboardConfigs(FieldSearchFilter[] filters);

  DatasourcesConfigDto loadDatasourceConfigByDatasourceCodeAndDashboardConfig(int dashboardId, String datasourceCode);
}