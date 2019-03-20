
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

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface IDashboardConfigService {

    String BEAN_NAME = "dashboardDashboardConfigService";

    PagedMetadata<DashboardConfigDto> getDashboardConfigs(RestListRequest requestList);

    DashboardConfigDto updateDashboardConfig(DashboardConfigRequest dashboardConfigRequest);

    DashboardConfigDto addDashboardConfig(DashboardConfigRequest dashboardConfigRequest);

    void removeDashboardConfig(int id);

    DashboardConfigDto getDashboardConfig(int id);

    DatasourcesConfigDto getDashboardDataSourceDtobyIdAndCode(int dashboardId, String datasourceCode);
}

