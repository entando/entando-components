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

package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.event.DashboardConfigChangedEvent;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIGS_ERROR_SEARCHING;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_DELETING_S;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_LOADING_LIST;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_LOADING__WITH_ID_S;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_ON_INSERT;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_UPDATING_S;

public class DashboardConfigManager extends AbstractService implements IDashboardConfigManager {

  private static final Logger logger = LoggerFactory.getLogger(DashboardConfigManager.class);

  @Override
  public void init() throws Exception {
    logger.debug("{} ready.", this.getClass().getName());
  }

  @Override
  public DashboardConfig getDashboardConfig(int id) throws ApsSystemException {
    DashboardConfig dashboardConfig = null;
    try {
      dashboardConfig = this.getDashboardConfigDAO().loadDashboardConfig(id);
    } catch (Throwable t) {
      logger.error(String.format(DASHBOARD_CONFIG_ERROR_LOADING__WITH_ID_S, id), t);
      throw new ApsSystemException(String.format(DASHBOARD_CONFIG_ERROR_LOADING__WITH_ID_S, id), t);
    }
    return dashboardConfig;
  }

  @Override
  public List<Integer> getDashboardConfigs() throws ApsSystemException {
    List<Integer> dashboardConfigs = new ArrayList<Integer>();
    try {
      dashboardConfigs = this.getDashboardConfigDAO().loadDashboardConfigs();
    } catch (Throwable t) {
      logger.error(DASHBOARD_CONFIG_ERROR_LOADING_LIST, t);
      throw new ApsSystemException(DASHBOARD_CONFIG_ERROR_LOADING_LIST, t);
    }
    return dashboardConfigs;
  }

  @Override
  public List<Integer> searchDashboardConfigs(FieldSearchFilter filters[])
      throws ApsSystemException {
    List<Integer> dashboardConfigs = new ArrayList<Integer>();
    try {
      dashboardConfigs = this.getDashboardConfigDAO().searchDashboardConfigs(filters);
    } catch (Throwable t) {
      logger.error(DASHBOARD_CONFIGS_ERROR_SEARCHING, t);
      throw new ApsSystemException(
          DASHBOARD_CONFIGS_ERROR_SEARCHING, t);
    }
    return dashboardConfigs;
  }

  @Override
  public void addDashboardConfig(DashboardConfig dashboardConfig) throws ApsSystemException {
    try {
      int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
      dashboardConfig.setId(key);
      this.getDashboardConfigDAO().insertDashboardConfig(dashboardConfig);
      this.notifyDashboardConfigChangedEvent(dashboardConfig,
          DashboardConfigChangedEvent.INSERT_OPERATION_CODE);
    } catch (Throwable t) {
      logger.error(DASHBOARD_CONFIG_ERROR_ON_INSERT, t);
      throw new ApsSystemException(DASHBOARD_CONFIG_ERROR_ON_INSERT, t);
    }
  }

  @Override
  public void updateDashboardConfig(DashboardConfig dashboardConfig) throws ApsSystemException {
    try {
      this.getDashboardConfigDAO().updateDashboardConfig(dashboardConfig);
      this.notifyDashboardConfigChangedEvent(dashboardConfig,
          DashboardConfigChangedEvent.UPDATE_OPERATION_CODE);
    } catch (Throwable t) {
      logger.error(String.format(DASHBOARD_CONFIG_ERROR_UPDATING_S, dashboardConfig.getId()), t);
      throw new ApsSystemException(
          String.format(DASHBOARD_CONFIG_ERROR_UPDATING_S, dashboardConfig.getId()), t);
    }
  }

  @Override
  public void deleteDashboardConfig(int id) throws ApsSystemException {
    try {
      DashboardConfig dashboardConfig = this.getDashboardConfig(id);
      this.getDashboardConfigDAO().removeDashboardConfig(id);
      this.notifyDashboardConfigChangedEvent(dashboardConfig,
          DashboardConfigChangedEvent.REMOVE_OPERATION_CODE);
    } catch (Throwable t) {
      logger.error(String.format(DASHBOARD_CONFIG_ERROR_DELETING_S, id), t);
      throw new ApsSystemException(String.format(DASHBOARD_CONFIG_ERROR_DELETING_S, id), t);
    }
  }


  private void notifyDashboardConfigChangedEvent(DashboardConfig dashboardConfig,
      int operationCode) {
    DashboardConfigChangedEvent event = new DashboardConfigChangedEvent();
    event.setDashboardConfig(dashboardConfig);
    event.setOperationCode(operationCode);
    this.notifyEvent(event);
  }

  @SuppressWarnings("rawtypes")
  public SearcherDaoPaginatedResult<DashboardConfig> getDashboardConfigs(
      FieldSearchFilter[] filters) throws ApsSystemException {
    SearcherDaoPaginatedResult<DashboardConfig> pagedResult = null;
    try {
      List<DashboardConfig> dashboardConfigs = new ArrayList<>();
      int count = this.getDashboardConfigDAO().countDashboardConfigs(filters);

      List<Integer> dashboardConfigNames = this.getDashboardConfigDAO()
          .searchDashboardConfigs(filters);
      for (Integer dashboardConfigName : dashboardConfigNames) {
        dashboardConfigs.add(this.getDashboardConfig(dashboardConfigName));
      }
      pagedResult = new SearcherDaoPaginatedResult<DashboardConfig>(count, dashboardConfigs);
    } catch (Throwable t) {
      logger.error(DASHBOARD_CONFIGS_ERROR_SEARCHING, t);
      throw new ApsSystemException(DASHBOARD_CONFIGS_ERROR_SEARCHING, t);
    }
    return pagedResult;
  }

  @Override
  public SearcherDaoPaginatedResult<DashboardConfig> getDashboardConfigs(
      List<FieldSearchFilter> filters) throws ApsSystemException {
    FieldSearchFilter[] array = null;
    if (null != filters) {
      array = filters.toArray(new FieldSearchFilter[filters.size()]);
    }
    return this.getDashboardConfigs(array);
  }

  @Override
  public DatasourcesConfigDto getDatasourceByDatasourcecodeAndDashboard(
      int dashboardId, String datasourceCode) {

    return this.getDashboardConfigDAO()
        .loadDatasourceConfigByDatasourceCodeAndDashboardConfig(dashboardId,
            datasourceCode);
  }

  @Override
  public boolean existsById(int id) {
    FieldSearchFilter[] filters = new FieldSearchFilter[0];
    FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), (Integer) id, false);
    filters = IoTUtils.addFilter(filters, filterToAdd);
    return this.getDashboardConfigDAO().countDashboardConfigs(filters) > 0;
  }

  @Override
  public boolean existsByIdAndActive(int id, int active) {
    FieldSearchFilter[] filters = new FieldSearchFilter[0];
    FieldSearchFilter filterId = new FieldSearchFilter(("id"), (Integer) id, false);
    FieldSearchFilter filterActive = new FieldSearchFilter(("active"), (Integer) active, false);
    filters = IoTUtils.addFilter(filters, filterId);
    filters = IoTUtils.addFilter(filters, filterActive);
    return this.getDashboardConfigDAO().countDashboardConfigs(filters) > 0;
  }

  @Override
  public boolean datasourceExistsByDatasourceName(String datasourceName) {
    return this.getDashboardConfigDAO().countDatasourceByName(datasourceName) > 0;
  }

  @Override
  public boolean datasourceExistsByDashboardIdAndDatasourceName(int dashboardId,
      String datasourceName) {
    return this.getDashboardConfigDAO()
        .countDatasourceByDashboardIdAndName(dashboardId, datasourceName) > 0;
  }


  @Override
  public boolean datasourceExistsByDatasourceCode(String datasourceCode) {
    return this.getDashboardConfigDAO().countDatasourceByCode(datasourceCode) > 0;
  }

  @Override
  public DatasourcesConfigDto getDatasourceByDatasourceName(String datasourceName) {
    return this.getDashboardConfigDAO().getDatasourceByDatasourceId(datasourceName);
  }

  @Override
  public void updateDatasource(DatasourcesConfigDto datasource) {
    this.getDashboardConfigDAO().updateDatasource(datasource);
  }

  protected IKeyGeneratorManager getKeyGeneratorManager() {
    return _keyGeneratorManager;
  }

  public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
    this._keyGeneratorManager = keyGeneratorManager;
  }

  public void setDashboardConfigDAO(IDashboardConfigDAO dashboardConfigDAO) {
    this._dashboardConfigDAO = dashboardConfigDAO;
  }

  protected IDashboardConfigDAO getDashboardConfigDAO() {
    return _dashboardConfigDAO;
  }

  private IKeyGeneratorManager _keyGeneratorManager;
  private IDashboardConfigDAO _dashboardConfigDAO;
}
