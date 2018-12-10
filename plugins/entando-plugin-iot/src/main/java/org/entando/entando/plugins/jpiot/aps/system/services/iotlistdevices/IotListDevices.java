/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices;



public class IotListDevices {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getWidgetTitle() {
		return _widgetTitle;
	}
	public void setWidgetTitle(String widgetTitle) {
		this._widgetTitle = widgetTitle;
	}

	public String getDatasource() {
		return _datasource;
	}
	public void setDatasource(String datasource) {
		this._datasource = datasource;
	}

	public String getContext() {
		return _context;
	}
	public void setContext(String context) {
		this._context = context;
	}

	public int getDownload() {
		return _download;
	}
	public void setDownload(int download) {
		this._download = download;
	}

	public int getFilter() {
		return _filter;
	}
	public void setFilter(int filter) {
		this._filter = filter;
	}

	public int getAllColumns() {
		return _allColumns;
	}
	public void setAllColumns(int allColumns) {
		this._allColumns = allColumns;
	}

	public String getColumns() {
		return _columns;
	}
	public void setColumns(String columns) {
		this._columns = columns;
	}

	
	private int _id;
	private String _widgetTitle;
	private String _datasource;
	private String _context;
	private int _download;
	private int _filter;
	private int _allColumns;
	private String _columns;

}
