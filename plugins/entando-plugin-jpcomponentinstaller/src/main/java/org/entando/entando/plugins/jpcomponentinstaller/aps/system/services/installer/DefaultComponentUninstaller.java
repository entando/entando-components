/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.aps.util.FileTextReader;

import java.io.InputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;

import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import org.entando.entando.aps.system.init.AbstractInitializerManager;
import org.entando.entando.aps.system.init.ComponentManager;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.IPostProcessor;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.aps.system.init.InstallationReportDAO;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.ComponentInstallationReport;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * @author E.Santoboni
 */
public class DefaultComponentUninstaller extends AbstractInitializerManager implements IComponentUninstaller, ApplicationContextAware {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultComponentUninstaller.class);

    public void init() throws Exception {
        //nothing to do
    }

    @Override
    public boolean uninstallComponent(Component component) throws ApsSystemException {
        ServletContext servletContext = ((ConfigurableWebApplicationContext) _applicationContext).getServletContext();
        ClassLoader cl = (ClassLoader) servletContext.getAttribute("componentInstallerClassLoader");
        List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
        ClassPathXmlApplicationContext appCtx = null;
        if (ctxList != null) {
            for (ClassPathXmlApplicationContext ctx : ctxList) {
                if (component.getCode().equals(ctx.getDisplayName())) {
                    appCtx = ctx;
                }
            }            
        }
        String appRootPath = servletContext.getRealPath("/");
        String backupDirPath = appRootPath + "componentinstaller" + File.separator + component.getArtifactId() + "-backup";
        Map<File, File> resourcesMap = new HashMap<File, File>();
        try {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                if (cl != null) {
                    Thread.currentThread().setContextClassLoader(cl);                    
                }
                if (null == component || null == component.getUninstallerInfo()) {
                    return false;
                }
                this.getDatabaseManager().createBackup();//backup database
                SystemInstallationReport report = super.extractReport();
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
                if (tableMapping != null) {
                    for (int j = 0; j < dataSourceNames.length; j++) {
                        String dataSourceName = dataSourceNames[j];
                        List<String> tableClasses = tableMapping.get(dataSourceName);
                        if (null != tableClasses && tableClasses.size() > 0) {
                            List<String> newList = new ArrayList<String>();
                            newList.addAll(tableClasses);
                            Collections.reverse(newList);
                            DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
                            IDatabaseManager.DatabaseType type = this.getDatabaseManager().getDatabaseType(dataSource);
                            TableFactory tableFactory = new TableFactory(dataSourceName, dataSource, type);
                            tableFactory.dropTables(newList);
                        }
                    }
                }

                //move resources (jar, files and folders) on temp folder  
                List<String> resourcesPaths = ui.getResourcesPaths();
                if (resourcesPaths != null) {
                    for (String resourcePath : resourcesPaths) {
                        try {
                            String fullResourcePath = servletContext.getRealPath(resourcePath);
                            File resFile = new File(fullResourcePath);
                            String relResPath = FilenameUtils.getPath(resFile.getAbsolutePath());
                            File newResFile = new File(backupDirPath + File.separator
                                    + relResPath + resFile.getName());
                            if (resFile.isDirectory()) {
                                FileUtils.copyDirectory(resFile, newResFile);
                                resourcesMap.put(resFile, newResFile);
                                FileUtils.deleteDirectory(resFile);
                            } else {
                                FileUtils.copyFile(resFile, newResFile);
                                resourcesMap.put(resFile, newResFile);
                                FileUtils.forceDelete(resFile);
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                //upgrade report
                ComponentInstallationReport cir = report.getComponentReport(component.getCode(), true);
                cir.getDataSourceReport().upgradeDatabaseStatus(SystemInstallationReport.Status.UNINSTALLED);
                cir.getDataReport().upgradeDatabaseStatus(SystemInstallationReport.Status.UNINSTALLED);
                this.saveReport(report);

                //remove plugin's xmlapplicationcontext if present
                if (appCtx != null) {
                    appCtx.close();
                    ctxList.remove(appCtx);
                }
                InitializerManager initializerManager = (InitializerManager) _applicationContext.getBean("InitializerManager");
                initializerManager.reloadCurrentReport();
                ComponentManager componentManager = (ComponentManager) _applicationContext.getBean("ComponentManager");
                componentManager.refresh();
            } catch (Exception e) {
				_logger.error("Unexpected error in component uninstallation process", e);
                throw new ApsSystemException("Unexpected error in component uninstallation process.", e);
            } finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
				ApsWebApplicationUtils.executeSystemRefresh(servletContext);
            }
        } catch (Throwable t) {
            //restore files on temp folder
            try {
                for (Object object : resourcesMap.entrySet()) {
                    File resFile = ((Map.Entry<File, File>) object).getKey();
                    File newResFile = ((Map.Entry<File, File>) object).getValue();
                    if (newResFile.isDirectory()) {
                        FileUtils.copyDirectoryToDirectory(newResFile, resFile.getParentFile());
                    } else {
                        FileUtils.copyFile(newResFile, resFile.getParentFile());
                    }
                }
            } catch (Exception e) {
            }
			_logger.error("Unexpected error in component uninstallation process", t);
            throw new ApsSystemException("Unexpected error in component uninstallation process.", t);
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
                _logger.error("Error while executing post process of index {}", i, t);
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

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        _applicationContext = ac;
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

    private ApplicationContext _applicationContext;

	//public static final String REPORT_CONFIG_ITEM = "entandoComponentsReport";
}
