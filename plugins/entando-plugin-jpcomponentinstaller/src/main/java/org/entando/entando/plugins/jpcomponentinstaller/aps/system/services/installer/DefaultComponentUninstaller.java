/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.model.IPostProcess;
import org.entando.entando.aps.system.init.model.InvalidPostProcessResultException;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.init.AbstractInitializerManager;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.IPostProcessor;
import org.entando.entando.aps.system.init.InstallationReportDAO;
import org.entando.entando.aps.system.init.model.Component;

/**
 * @author E.Santoboni
 */
public class DefaultComponentUninstaller extends AbstractInitializerManager implements IComponentUninstaller {
	
	private static final Logger _logger = LoggerFactory.getLogger(DefaultComponentUninstaller.class);
	
	public void init() throws Exception {
		//nothing to do
	}
	
	@Override
	public boolean uninstallComponent(Component component) throws ApsSystemException {
		try {
			if (null == component || null == component.getUninstallerInfo()) {
				return false;
			}
			//backup database
			//move resources (jar, files and folders) on temp folder
			//remove records from db
			//drop tables
			//upgrade report
		} catch (Exception e) {
			//restore files on temp folder
			return false;
		} finally {
			//clean temp folder
		}
		return true;
	}
	
	/*
	public void executePostInitProcesses() throws BeansException {
		SystemInstallationReport report = null;
		try {
			report = this.extractReport();
			List<Component> components = this.getComponentManager().getCurrentComponents();
			for (int i = 0; i < components.size(); i++) {
				Component component = components.get(i);
				ComponentInstallationReport componentReport = report.getComponentReport(component.getCode(), false);
				SystemInstallationReport.Status postProcessStatus = componentReport.getPostProcessStatus();
				if (!postProcessStatus.equals(SystemInstallationReport.Status.INIT)) {
					continue;
				}
				String compEnvKey = (AbstractInitializerManager.Environment.test.equals(this.getEnvironment())) 
						? AbstractInitializerManager.Environment.test.toString() : AbstractInitializerManager.Environment.production.toString();
				ComponentEnvironment componentEnvironment = (null != component.getEnvironments()) ? 
						component.getEnvironments().get(compEnvKey) :
						null;
				List<IPostProcess> postProcesses = (null != componentEnvironment) ? componentEnvironment.getPostProcesses() : null;
				if (null == postProcesses || postProcesses.isEmpty()) {
					postProcessStatus = SystemInstallationReport.Status.NOT_AVAILABLE;
				} else if (!this.isCheckOnStartup()) {
					postProcessStatus = SystemInstallationReport.Status.SKIPPED;
				} else if (!componentReport.isPostProcessExecutionRequired()) {
					//Porting or restore
					postProcessStatus = SystemInstallationReport.Status.NOT_AVAILABLE;
				} else {
					postProcessStatus = this.executePostProcesses(postProcesses);
				}
				componentReport.setPostProcessStatus(postProcessStatus);
				report.setUpdated();
			}
		} catch (Throwable t) {
			_logger.error("Error while executing post processes", t);
			throw new FatalBeanException("Error while executing post processes", t);
		} finally {
			if (null != report && report.isUpdated()) {
				this.saveReport(report);
			}
		}
	}
	*/
	protected SystemInstallationReport.Status executePostProcesses(List<IPostProcess> postProcesses) throws ApsSystemException {
		if (null == postProcesses || postProcesses.isEmpty()) {
			return SystemInstallationReport.Status.NOT_AVAILABLE;
		}
		for (int i = 0; i < postProcesses.size(); i++) {
			IPostProcess postProcess = postProcesses.get(i);
			try {
				IPostProcessor postProcessor = this.getPostProcessors().get(postProcess.getCode());
				if (null != postProcessor) {
					postProcessor.executePostProcess(postProcess);
				} else {
					_logger.error("Missing Post Processor for process '{}'", postProcess.getCode());
				}
			} catch (InvalidPostProcessResultException t) {
				_logger.error("Error while executing post process of index {}",i, t);
				return SystemInstallationReport.Status.INCOMPLETE;
			} catch (Throwable t) {
				_logger.error("Error while executing post process - index {}", i, t);
				return SystemInstallationReport.Status.INCOMPLETE;
			}
		}
		return SystemInstallationReport.Status.OK;
	}
	
	private void saveReport(SystemInstallationReport report) throws BeansException {
		if (null == report || report.getReports().isEmpty()) {
			return;
		}
		try {
			InstallationReportDAO dao = new InstallationReportDAO();
			DataSource dataSource = (DataSource) this.getBeanFactory().getBean("portDataSource");
			dao.setDataSource(dataSource);
			dao.saveConfigItem(report.toXml(), this.getConfigVersion());
		} catch (Throwable t) {
			_logger.error("Error saving report", t);
			throw new FatalBeanException("Error saving report", t);
		}
	}
	
	public Map<String, IPostProcessor> getPostProcessors() {
		return _postProcessors;
	}
	public void setPostProcessors(Map<String, IPostProcessor> postProcessors) {
		this._postProcessors = postProcessors;
	}
	
	protected IDatabaseManager getDatabaseManager() {
		return _databaseManager;
	}
	public void setDatabaseManager(IDatabaseManager databaseManager) {
		this._databaseManager = databaseManager;
	}
	
	private Map<String, IPostProcessor> _postProcessors;
	
	private IDatabaseManager _databaseManager;
	
	//public static final String REPORT_CONFIG_ITEM = "entandoComponentsReport";
	
}
