/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.model;


public class DashboardTableDto {

	private int id;
	private String widgetTitle;
	private String datasource;
	private String context;
	private int download;
	private int filter;
	private int allColumns;
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


    public static String getEntityFieldName(String dtoFieldName) {
		return dtoFieldName;
    }
    
}
