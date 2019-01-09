/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardtable.model;


import javax.validation.constraints.*;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.entando.entando.web.common.json.JsonDateDeserializer;
import org.entando.entando.web.common.json.JsonDateSerializer;

public class DashboardTableRequest {

    @NotNull(message = "dashboardTable.id.notBlank")	
	private int id;

	@Size(max = 50, message = "string.size.invalid")
	@NotBlank(message = "dashboardTable.widgetTitle.notBlank")
	private String widgetTitle;

	@Size(max = 50, message = "string.size.invalid")
	@NotBlank(message = "dashboardTable.datasource.notBlank")
	private String datasource;

	@Size(max = 50, message = "string.size.invalid")
	@NotBlank(message = "dashboardTable.context.notBlank")
	private String context;

    @NotNull(message = "dashboardTable.download.notBlank")	
	private int download;

    @NotNull(message = "dashboardTable.filter.notBlank")	
	private int filter;

    @NotNull(message = "dashboardTable.allColumns.notBlank")	
	private int allColumns;

	@Size(max = 255, message = "string.size.invalid")
	@NotBlank(message = "dashboardTable.columns.notBlank")
	private String columns;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getWidgetTitle() {
		return widgetTitle;
	}
	public void setWidgetTitle(String widgetTitle) {
		this.widgetTitle = widgetTitle;
	}

	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}

	public int getDownload() {
		return download;
	}
	public void setDownload(int download) {
		this.download = download;
	}

	public int getFilter() {
		return filter;
	}
	public void setFilter(int filter) {
		this.filter = filter;
	}

	public int getAllColumns() {
		return allColumns;
	}
	public void setAllColumns(int allColumns) {
		this.allColumns = allColumns;
	}

	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}


}
