/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.api;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IotListDevices;

@XmlRootElement(name = "iotListDevices")
@XmlType(propOrder = {"id", "widgetTitle", "datasource", "context", "download", "filter", "allColumns", "columns"})
public class JAXBIotListDevices {

    public JAXBIotListDevices() {
        super();
    }

    public JAXBIotListDevices(IotListDevices iotListDevices) {
		this.setId(iotListDevices.getId());
		this.setWidgetTitle(iotListDevices.getWidgetTitle());
		this.setDatasource(iotListDevices.getDatasource());
		this.setContext(iotListDevices.getContext());
		this.setDownload(iotListDevices.getDownload());
		this.setFilter(iotListDevices.getFilter());
		this.setAllColumns(iotListDevices.getAllColumns());
		this.setColumns(iotListDevices.getColumns());
    }
    
    public IotListDevices getIotListDevices() {
    	IotListDevices iotListDevices = new IotListDevices();
		iotListDevices.setId(this.getId());
		iotListDevices.setWidgetTitle(this.getWidgetTitle());
		iotListDevices.setDatasource(this.getDatasource());
		iotListDevices.setContext(this.getContext());
		iotListDevices.setDownload(this.getDownload());
		iotListDevices.setFilter(this.getFilter());
		iotListDevices.setAllColumns(this.getAllColumns());
		iotListDevices.setColumns(this.getColumns());
    	return iotListDevices;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "widgetTitle", required = true)
	public String getWidgetTitle() {
		return _widgetTitle;
	}
	public void setWidgetTitle(String widgetTitle) {
		this._widgetTitle = widgetTitle;
	}

	@XmlElement(name = "datasource", required = true)
	public String getDatasource() {
		return _datasource;
	}
	public void setDatasource(String datasource) {
		this._datasource = datasource;
	}

	@XmlElement(name = "context", required = true)
	public String getContext() {
		return _context;
	}
	public void setContext(String context) {
		this._context = context;
	}

	@XmlElement(name = "download", required = true)
	public int getDownload() {
		return _download;
	}
	public void setDownload(int download) {
		this._download = download;
	}

	@XmlElement(name = "filter", required = true)
	public int getFilter() {
		return _filter;
	}
	public void setFilter(int filter) {
		this._filter = filter;
	}

	@XmlElement(name = "allColumns", required = true)
	public int getAllColumns() {
		return _allColumns;
	}
	public void setAllColumns(int allColumns) {
		this._allColumns = allColumns;
	}

	@XmlElement(name = "columns", required = true)
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
