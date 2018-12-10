/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.apsadmin.iotlistdevices;

import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IotListDevices;
import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IIotListDevicesManager;



import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotListDevicesAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IotListDevicesAction.class);

	public String newIotListDevices() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newIotListDevices", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String edit() {
		try {
			IotListDevices iotListDevices = this.getIotListDevicesManager().getIotListDevices(this.getId());
			if (null == iotListDevices) {
				this.addActionError(this.getText("error.iotListDevices.null"));
				return INPUT;
			}
			this.populateForm(iotListDevices);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			IotListDevices iotListDevices = this.createIotListDevices();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getIotListDevicesManager().addIotListDevices(iotListDevices);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getIotListDevicesManager().updateIotListDevices(iotListDevices);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			IotListDevices iotListDevices = this.getIotListDevicesManager().getIotListDevices(this.getId());
			if (null == iotListDevices) {
				this.addActionError(this.getText("error.iotListDevices.null"));
				return INPUT;
			}
			this.populateForm(iotListDevices);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getIotListDevicesManager().deleteIotListDevices(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String view() {
		try {
			IotListDevices iotListDevices = this.getIotListDevicesManager().getIotListDevices(this.getId());
			if (null == iotListDevices) {
				this.addActionError(this.getText("error.iotListDevices.null"));
				return INPUT;
			}
			this.populateForm(iotListDevices);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(IotListDevices iotListDevices) throws Throwable {
		this.setId(iotListDevices.getId());
		this.setWidgetTitle(iotListDevices.getWidgetTitle());
		this.setDatasource(iotListDevices.getDatasource());
		this.setContext(iotListDevices.getContext());
		this.setDownload(iotListDevices.getDownload());
		this.setFilter(iotListDevices.getFilter());
		this.setAllColumns(iotListDevices.getAllColumns());
		this.setColumns(iotListDevices.getColumns());
	}
	
	private IotListDevices createIotListDevices() {
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
	

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
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

	
	protected IIotListDevicesManager getIotListDevicesManager() {
		return _iotListDevicesManager;
	}
	public void setIotListDevicesManager(IIotListDevicesManager iotListDevicesManager) {
		this._iotListDevicesManager = iotListDevicesManager;
	}
	
	private int _strutsAction;
	private int _id;
	private String _widgetTitle;
	private String _datasource;
	private String _context;
	private int _download;
	private int _filter;
	private int _allColumns;
	private String _columns;
	
	private IIotListDevicesManager _iotListDevicesManager;
	
}