
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

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardConfigDAO extends AbstractSearcherDAO implements IDashboardConfigDAO {

  private static final Logger logger = LoggerFactory.getLogger(DashboardConfigDAO.class);

  @Override
  public int countDashboardConfigs(FieldSearchFilter[] filters) {
    Integer dashboardConfigs = null;
    try {
      dashboardConfigs = super.countId(filters);
    } catch (Throwable t) {
      logger.error("error in count dashboardConfigs", t);
      throw new RuntimeException("error in count dashboardConfigs", t);
    }
    return dashboardConfigs;
  }

  @Override
  protected String getTableFieldName(String metadataFieldKey) {
    return metadataFieldKey;
  }

  @Override
  protected String getMasterTableName() {
    return "dashboard_config";
  }

  @Override
  protected String getMasterTableIdFieldName() {
    return "id";
  }

  @Override
  public List<Integer> searchDashboardConfigs(FieldSearchFilter[] filters) {
    List<Integer> dashboardConfigsId = new ArrayList<>();
    List<String> masterList = super.searchId(filters);
    masterList.stream().forEach(idString -> dashboardConfigsId.add(Integer.parseInt(idString)));
    return dashboardConfigsId;
  }


  @Override
  public List<Integer> loadDashboardConfigs() {
    List<Integer> dashboardConfigsId = new ArrayList<Integer>();
    Connection conn = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      conn = this.getConnection();
      stat = conn.prepareStatement(LOAD_DASHBOARD_CONFIGS_ID);
      res = stat.executeQuery();
      while (res.next()) {
        int id = res.getInt("id");
        dashboardConfigsId.add(id);
      }
    } catch (Throwable t) {
      logger.error("Error loading DashboardConfig list", t);
      throw new RuntimeException("Error loading DashboardConfig list", t);
    } finally {
      closeDaoResources(res, stat, conn);
    }
    return dashboardConfigsId;
  }

  @Override
  public void insertDashboardConfig(DashboardConfig dashboardConfig) {
    PreparedStatement stat = null;
    Connection conn = null;
    try {
      conn = this.getConnection();
      conn.setAutoCommit(false);
      this.insertDashboardConfigDatasource(dashboardConfig, conn);
      this.insertDashboardConfig(dashboardConfig, conn);
      conn.commit();
    } catch (Throwable t) {
      this.executeRollback(conn);
      logger.error("Error on insert dashboardConfig", t);
      throw new RuntimeException("Error on insert dashboardConfig", t);
    } finally {
      this.closeDaoResources(null, stat, conn);
    }
  }

  private void insertDashboardConfigDatasource(final DashboardConfig dashboardConfig, final Connection conn) throws SQLException {
    final PreparedStatement stat = conn.prepareStatement(ADD_DATASOURCE);
    dashboardConfig.getDatasources().forEach(c -> {
      try {
        int index = 1;
        stat.setInt(index++, dashboardConfig.getId());
        stat.setString(index++, c.getDatasource());
        stat.setString(index++, c.getDatasourceURI());
        if (StringUtils.isNotBlank(c.getStatus())) {
          stat.setString(index++, c.getStatus());
        } else {
          stat.setNull(index++, Types.VARCHAR);
        }
        stat.executeUpdate();
      } catch (Throwable t) {
        logger.error("Error on insert dashboardConfig Datasource", t);
        throw new RuntimeException("Error on insert dashboardConfig DataSource", t);
      }

    });

  }

  private void insertDashboardConfig(DashboardConfig dashboardConfig, Connection conn) {
    PreparedStatement stat = null;
    try {
      stat = conn.prepareStatement(ADD_DASHBOARD_CONFIG);
      int index = 1;
      stat.setInt(index++, dashboardConfig.getId());
      stat.setString(index++, dashboardConfig.getServerDescription());
      stat.setString(index++, dashboardConfig.getServerURI());
      if (StringUtils.isNotBlank(dashboardConfig.getUsername())) {
        stat.setString(index++, dashboardConfig.getUsername());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      if (StringUtils.isNotBlank(dashboardConfig.getPassword())) {
        stat.setString(index++, dashboardConfig.getPassword());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      if (StringUtils.isNotBlank(dashboardConfig.getToken())) {
        stat.setString(index++, dashboardConfig.getToken());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      stat.setInt(index++, dashboardConfig.getTimeConnection());
      stat.setInt(index++, dashboardConfig.getActive() ? 1 : 0);
      stat.setInt(index++, dashboardConfig.getDebug() ? 1 : 0);
      stat.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error on insert dashboardConfig", t);
      throw new RuntimeException("Error on insert dashboardConfig", t);
    } finally {
      this.closeDaoResources(null, stat, null);
    }
  }

  @Override
  public void updateDashboardConfig(DashboardConfig dashboardConfig) {
    PreparedStatement stat = null;
    Connection conn = null;
    try {
      conn = this.getConnection();
      conn.setAutoCommit(false);
      this.updateDashboardConfigDatasource(dashboardConfig, conn);
      this.updateDashboardConfig(dashboardConfig, conn);
      conn.commit();
    } catch (Throwable t) {
      this.executeRollback(conn);
      logger.error("Error updating dashboardConfig {}", dashboardConfig.getId(), t);
      throw new RuntimeException("Error updating dashboardConfig", t);
    } finally {
      this.closeDaoResources(null, stat, conn);
    }
  }

  private void updateDashboardConfigDatasource(final DashboardConfig dashboardConfig, final Connection conn) throws SQLException {
    this.removeDashboardConfigDatasource(dashboardConfig.getId(), conn);
    this.insertDashboardConfigDatasource(dashboardConfig, conn);

  }

  public void updateDashboardConfig(DashboardConfig dashboardConfig, Connection conn) {
    PreparedStatement stat = null;
    try {
      stat = conn.prepareStatement(UPDATE_DASHBOARD_CONFIG);
      int index = 1;

      stat.setString(index++, dashboardConfig.getServerDescription());
      stat.setString(index++, dashboardConfig.getServerURI());
      if (StringUtils.isNotBlank(dashboardConfig.getUsername())) {
        stat.setString(index++, dashboardConfig.getUsername());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      if (StringUtils.isNotBlank(dashboardConfig.getPassword())) {
        stat.setString(index++, dashboardConfig.getPassword());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      if (StringUtils.isNotBlank(dashboardConfig.getToken())) {
        stat.setString(index++, dashboardConfig.getToken());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      stat.setInt(index++, dashboardConfig.getTimeConnection());
      stat.setInt(index++, dashboardConfig.getActive() ? 1 : 0);
      stat.setInt(index++, dashboardConfig.getDebug() ? 1 : 0);
      stat.setInt(index++, dashboardConfig.getId());
      stat.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error updating dashboardConfig {}", dashboardConfig.getId(), t);
      throw new RuntimeException("Error updating dashboardConfig", t);
    } finally {
      this.closeDaoResources(null, stat, null);
    }
  }


  @Override
  public void removeDashboardConfig(int id) {
    PreparedStatement stat = null;
    Connection conn = null;
    try {
      conn = this.getConnection();
      conn.setAutoCommit(false);
      this.removeDashboardConfigDatasource(id, conn);
      this.removeDashboardConfig(id, conn);
      conn.commit();
    } catch (Throwable t) {
      this.executeRollback(conn);
      logger.error("Error deleting dashboardConfig {}", id, t);
      throw new RuntimeException("Error deleting dashboardConfig", t);
    } finally {
      this.closeDaoResources(null, stat, conn);
    }
  }

  private void removeDashboardConfigDatasource(int id, Connection conn) {
    PreparedStatement stat = null;
    try {
      stat = conn.prepareStatement(DELETE_DATASOURCE);
      stat.setInt(1, id);
      stat.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error deleting dashboardConfig Datasource{}", id, t);
      throw new RuntimeException("Error deleting dashboardConfig Datasource", t);
    }
  }

  public void removeDashboardConfig(int id, Connection conn) {
    PreparedStatement stat = null;
    try {
      stat = conn.prepareStatement(DELETE_DASHBOARD_CONFIG);
      int index = 1;
      stat.setInt(index++, id);
      stat.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error deleting dashboardConfig {}", id, t);
      throw new RuntimeException("Error deleting dashboardConfig", t);
    } finally {
      this.closeDaoResources(null, stat, null);
    }
  }

  public DashboardConfig loadDashboardConfig(int id) {
    DashboardConfig dashboardConfig = null;
    Connection conn = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      conn = this.getConnection();
      dashboardConfig = this.loadDashboardConfig(id, conn);
    } catch (Throwable t) {
      logger.error("Error loading dashboardConfig with id {}", id, t);
      throw new RuntimeException("Error loading dashboardConfig with id " + id, t);
    } finally {
      closeDaoResources(res, stat, conn);
    }
    return dashboardConfig;
  }

  @Override
  public DatasourcesConfigDto loadDatasourceConfigByDatasourceCodeAndDashboardConfig(String datasourceCode, int dashboardId) {
    Connection conn;
    DatasourcesConfigDto datasource = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      conn = this.getConnection();
      stat = conn.prepareStatement(LOAD_DATASOURCE_BY_DASHBOARD_AND_DATASOURCECODE);
      stat.setInt(1, dashboardId);
      stat.setString(2,datasourceCode);
      res = stat.executeQuery();
      while (res.next()) {
        datasource  = new DatasourcesConfigDto();
        datasource.setDatasourceCode(res.getString("datasourceCode"));
        datasource.setDatasource(res.getString("datasource"));
        datasource.setDatasourceURI(res.getString("datasourceuri"));
        datasource.setStatus(res.getString("status"));
      }
    } catch (Throwable t) {
      logger.error("Error loading dashboardConfig with dashboardId {}", dashboardId, t);
      throw new RuntimeException("Error loading dashboardConfig with dashboardId " + dashboardId, t);
    } finally {
      closeDaoResources(res, stat, null);
    }
    
    return datasource;
  }
  
  public DashboardConfig loadDashboardConfig(int id, Connection conn) {
    DashboardConfig dashboardConfig = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      stat = conn.prepareStatement(LOAD_DASHBOARD_CONFIG);
      stat.setInt(1, id);
      res = stat.executeQuery();
      if (res.next()) {
        dashboardConfig = this.buildDashboardConfigFromRes(res);
      }
      stat = conn.prepareStatement(LOAD_DATASOURCE);
      stat.setInt(1, id);
      res = stat.executeQuery();
      while (res.next()) {
        DatasourcesConfigDto datasource = new DatasourcesConfigDto();
        datasource.setDatasourceCode(res.getString("datasourceCode"));
        datasource.setDatasource(res.getString("datasource"));
        datasource.setDatasourceURI(res.getString("datasourceuri"));
        datasource.setStatus(res.getString("status"));
        dashboardConfig.getDatasources().add(datasource);
      }

    } catch (Throwable t) {
      logger.error("Error loading dashboardConfig with id {}", id, t);
      throw new RuntimeException("Error loading dashboardConfig with id " + id, t);
    } finally {
      closeDaoResources(res, stat, null);
    }
    return dashboardConfig;
  }

  protected DashboardConfig buildDashboardConfigFromRes(ResultSet res) {
    DashboardConfig dashboardConfig = null;
    try {
      dashboardConfig = new DashboardConfig();
      dashboardConfig.setId(res.getInt("id"));
      dashboardConfig.setServerDescription(res.getString("serverdescription"));
      dashboardConfig.setServerURI(res.getString("serveruri"));
      dashboardConfig.setUsername(res.getString("username"));
      dashboardConfig.setPassword(res.getString("password"));
      dashboardConfig.setToken(res.getString("token"));
      dashboardConfig.setTimeConnection(res.getInt("timeconnection"));
      dashboardConfig.setActive(res.getBoolean("active"));
      dashboardConfig.setDebug(res.getBoolean("debug"));

    } catch (Throwable t) {
      logger.error("Error in buildDashboardConfigFromRes", t);
    }
    return dashboardConfig;
  }

  private static final String ADD_DASHBOARD_CONFIG = "INSERT INTO dashboard_config (id, serverdescription, serveruri, username, password, token, timeconnection, active, debug ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";

  private static final String UPDATE_DASHBOARD_CONFIG = "UPDATE dashboard_config SET  serverdescription=?,  serveruri=?,  username=?,  password=?,  token=?,  timeconnection=?,  active=?, debug=? WHERE id = ?";

  private static final String DELETE_DASHBOARD_CONFIG = "DELETE FROM dashboard_config WHERE id = ?";

  private static final String LOAD_DASHBOARD_CONFIG = "SELECT id, serverdescription, serveruri, username, password, token, timeconnection, active, debug  FROM dashboard_config WHERE id = ?";

  private static final String LOAD_DASHBOARD_CONFIGS_ID = "SELECT id FROM dashboard_config";


  private static final String ADD_DATASOURCE = "INSERT INTO dashboard_config_datasource (fk_dashboard_config, datasourceCode ,datasource, datasourceuri, status ) VALUES (?, ?, ?, ?)";

  private static final String DELETE_DATASOURCE = "DELETE FROM dashboard_config_datasource WHERE fk_dashboard_config = ?";

  private static final String LOAD_DATASOURCE = "SELECT * FROM dashboard_config_datasource WHERE fk_dashboard_config = ?";
  
  private static final String LOAD_DATASOURCE_BY_DASHBOARD_AND_DATASOURCECODE = "SELECT * FROM dashboard_config_datasource WHERE fk_dashboard_config = ? AND datasourcecode = ?";


}