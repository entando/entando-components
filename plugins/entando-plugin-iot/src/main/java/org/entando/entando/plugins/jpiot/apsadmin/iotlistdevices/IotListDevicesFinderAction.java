/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.apsadmin.iotlistdevices;

import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IotListDevices;
import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IIotListDevicesManager;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotListDevicesFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IotListDevicesFinderAction.class);

	public List<Integer> getIotListDevicessId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your IIotListDevicesManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getWidgetTitle())) {
				//TODO add a constant into your IIotListDevicesManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("widgetTitle"), this.getWidgetTitle(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getDatasource())) {
				//TODO add a constant into your IIotListDevicesManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("datasource"), this.getDatasource(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getContext())) {
				//TODO add a constant into your IIotListDevicesManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("context"), this.getContext(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getDownload()) {
				//TODO add a constant into your IIotListDevicesManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("download"), this.getDownload(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getFilter()) {
				//TODO add a constant into your IIotListDevicesManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("filter"), this.getFilter(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getAllColumns()) {
				//TODO add a constant into your IIotListDevicesManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("allColumns"), this.getAllColumns(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getColumns())) {
				//TODO add a constant into your IIotListDevicesManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("columns"), this.getColumns(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			List<Integer> iotListDevicess = this.getIotListDevicesManager().searchIotListDevicess(filters);
			return iotListDevicess;
		} catch (Throwable t) {
			_logger.error("Error getting iotListDevicess list", t);
			throw new RuntimeException("Error getting iotListDevicess list", t);
		}
	}

	protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	public IotListDevices getIotListDevices(int id) {
		IotListDevices iotListDevices = null;
		try {
			iotListDevices = this.getIotListDevicesManager().getIotListDevices(id);
		} catch (Throwable t) {
			_logger.error("Error getting iotListDevices with id {}", id, t);
			throw new RuntimeException("Error getting iotListDevices with id " + id, t);
		}
		return iotListDevices;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
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


	public Integer getDownload() {
		return _download;
	}
	public void setDownload(Integer download) {
		this._download = download;
	}


	public Integer getFilter() {
		return _filter;
	}
	public void setFilter(Integer filter) {
		this._filter = filter;
	}


	public Integer getAllColumns() {
		return _allColumns;
	}
	public void setAllColumns(Integer allColumns) {
		this._allColumns = allColumns;
	}


	public String getColumns() {
		return _columns;
	}
	public void setColumns(String columns) {
		this._columns = columns;
	}


	protected IIotListDevicesManager getIotListDevicesManager() {
		return _iotListDevicesManager;
	}
	public void setIotListDevicesManager(IIotListDevicesManager iotListDevicesManager) {
		this._iotListDevicesManager = iotListDevicesManager;
	}
	
	private Integer _id;
	private String _widgetTitle;
	private String _datasource;
	private String _context;
	private Integer _download;
	private Integer _filter;
	private Integer _allColumns;
	private String _columns;
	private IIotListDevicesManager _iotListDevicesManager;
}