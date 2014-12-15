/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.AbstractInitializerManager;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.IPostProcessor;
import org.entando.entando.aps.system.init.InstallationReportDAO;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.ComponentUninstallerInfo;
import org.entando.entando.aps.system.init.model.IPostProcess;
import org.entando.entando.aps.system.init.model.InvalidPostProcessResultException;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.init.util.QueryExtractor;
import org.entando.entando.aps.system.init.util.TableDataUtils;
import org.entando.entando.aps.system.init.util.TableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.io.Resource;

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
			this.getDatabaseManager().createBackup();//backup database
			
			SystemInstallationReport report = super.extractReport();
			
			//move resources (jar, files and folders) on temp folder
			//TODO
			
			ComponentUninstallerInfo ui = component.getUninstallerInfo();
			
			//remove records from db
			String[] dataSourceNames = this.extractBeanNames(DataSource.class);
			for (int j = 0; j < dataSourceNames.length; j++) {
				String dataSourceName = dataSourceNames[j];
				Resource resource = (null != ui) ? ui.getSqlResources(dataSourceName) : null;
				String script = (null != resource) ? this.readFile(resource) : null;
				if (null != script && script.trim().length() > 0) {
					DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
					String[] queries = QueryExtractor.extractDeleteQueries(script);
					TableDataUtils.executeQueries(dataSource, queries, true);
				}
			}
			this.executePostProcesses(ui.getPostProcesses());
			
			//drop tables
			Map<String, List<String>> tableMapping = component.getTableMapping();
			for (int j = 0; j < dataSourceNames.length; j++) {
				String dataSourceName = dataSourceNames[j];
				List<String> classes = tableMapping.get(dataSourceName);
				if (null != classes && classes.size() > 0) {
					DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
					IDatabaseManager.DatabaseType type = this.getDatabaseManager().getDatabaseType(dataSource);
					TableFactory tableFactory = new TableFactory(dataSourceName, dataSource, type);
					tableFactory.dropTables(classes);
				}
			}
			
			//upgrade report
			report.removeComponentReport(component.getCode());
			this.saveReport(report);
		} catch (Throwable t) {
			//restore files on temp folder
			return false;
		} finally {
			//clean temp folder
		}
		return true;
	}
	
	protected void executePostProcesses(List<IPostProcess> postProcesses) throws ApsSystemException {
		if (null == postProcesses || postProcesses.isEmpty()) {
			return;
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
			} catch (Throwable t) {
				_logger.error("Error while executing post process - index {}", i, t);
			}
		}
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
	
	private String[] extractBeanNames(Class beanClass) {
		ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
		return factory.getBeanNamesForType(beanClass);
	}
	
	private String readFile(Resource resource) throws Throwable {
		if (resource == null) {
			return null;
		}
		InputStream is = null;
		String text = null;
		try {
			is = resource.getInputStream();
			if (null == is) {
				return null;
			}
			text = FileTextReader.getText(is);
		} catch (Throwable t) {
			_logger.error("Error reading resource", t);
			throw new ApsSystemException("Error reading resource", t);
		} finally {
			if (null != is) {
				is.close();
			}
		}
		return text;
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
