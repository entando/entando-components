
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

package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.entando.entando.web.common.json.JsonDateDeserializer;
import org.entando.entando.web.common.json.JsonDateSerializer;

public class DashboardBarChartDto {

	private int id;
	private String widgetId;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}


    public static String getEntityFieldName(String dtoFieldName) {
		return dtoFieldName;
    }
    
}
