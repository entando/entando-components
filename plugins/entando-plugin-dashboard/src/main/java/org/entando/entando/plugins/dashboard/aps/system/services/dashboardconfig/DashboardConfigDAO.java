
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
  public int countDatasourceByName(String datasourceName) {
    Integer datasource = null;
    Connection conn = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      conn = this.getConnection();
      stat = conn.prepareStatement(COUNT_DATASOURCE_BY_NAME);
      int index = 1;
      stat.setString(index++, datasourceName);
      res = stat.executeQuery();
      while (res.next()) {
        datasource = res.getInt("count");
      }
    } catch (Throwable t) {
      logger.error("Error in count datasource", t);
      throw new RuntimeException("Error in count datasource", t);
    } finally {
      closeDaoResources(res, stat, conn);
    }
    return datasource;
  }

  @Override
  public int countDatasourceByDashboardIdAndName(int dashboardId, String datasourceName) {
    Integer datasource = null;
    Connection conn = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      conn = this.getConnection();
      stat = conn.prepareStatement(COUNT_DATASOURCE_BY_DASHBOARD_ID_AND_NAME);
      int index = 1;
      stat.setInt(index++, dashboardId);
      stat.setString(index++, datasourceName);
      res = stat.executeQuery();
      while (res.next()) {
        datasource = res.getInt("count");
      }
    } catch (Throwable t) {
      logger.error("Error in count datasource", t);
      throw new RuntimeException("Error in count datasource", t);
    } finally {
      closeDaoResources(res, stat, conn);
    }
    return datasource;
  }

  @Override
  public int countDatasourceByCode(String datasourceCode) {
    Integer datasource = null;
    Connection conn = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      conn = this.getConnection();
      stat = conn.prepareStatement(COUNT_DATASOURCE_BY_CODE);
      int index = 1;
      stat.setString(index++, datasourceCode);
      res = stat.executeQuery();
      while (res.next()) {
        datasource = res.getInt("count");
      }
    } catch (Throwable t) {
      logger.error("Error in count datasource", t);
      throw new RuntimeException("Error in count datasource", t);
    } finally {
      closeDaoResources(res, stat, conn);
    }
    return datasource;
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
    Connection conn = null;
    try {
      conn = this.getConnection();
      conn.setAutoCommit(false);
      this.insertDashboardConfig(dashboardConfig, conn);
      if (dashboardConfig.getDatasources() != null && dashboardConfig.getDatasources().size() > 0) {
        this.insertDashboardConfigDatasource(dashboardConfig, conn);
      }
      conn.commit();
    } catch (Throwable t) {
      this.executeRollback(conn);
      logger.error("Error on insert dashboardConfig", t);
      throw new RuntimeException("Error on insert dashboardConfig", t);
    } finally {
      this.closeDaoResources(null, null, conn);
    }
  }

  //region deletabili
/*
//  private void insertDashboardConfigDatasource(DatasourcesConfigDto datasource,
//      int dashboardConfigId,
//      final Connection conn)
//      throws SQLException {
//    final PreparedStatement stat = conn.prepareStatement(ADD_DATASOURCE);
//    try {
//      int index = 1;
//      stat.setInt(index++, dashboardConfigId);
//      stat.setString(index++, datasource.getDatasourceCode());
//      stat.setString(index++, datasource.getDatasource());
//      if (StringUtils.isNotBlank(datasource.getDatasourceURI())) {
//        stat.setString(index++, datasource.getDatasourceURI());
//      } else {
//        stat.setNull(index++, Types.VARCHAR);
//      }
//      if (StringUtils.isNotBlank(datasource.getStatus())) {
//        stat.setString(index++, datasource.getStatus());
//      } else {
//        stat.setNull(index++, Types.VARCHAR);
//      }
//      if (StringUtils.isNotBlank(datasource.getName())) {
//        stat.setString(index++, datasource.getName());
//      } else {
//        stat.setNull(index++, Types.VARCHAR);
//      }
//      String metadata = new Gson().toJson(datasource.getMetadata());
//      if (datasource.getMetadata() != null && StringUtils.isNotBlank(metadata)) {
//        stat.setString(index++, metadata);
//      } else {
//        stat.setNull(index++, Types.VARCHAR);
//      }
//      stat.executeUpdate();
//    } catch (Throwable t) {
//      logger.error("Error on insert dashboardConfig Datasource", t);
//      throw new RuntimeException("Error on insert dashboardConfig DataSource", t);
//    }
//  }
//
//  private void insertDashboardConfigDatasource(DashboardConfig dashboardConfig)
//      throws SQLException {
//    Connection conn = null;
//    try {
//      conn = this.getConnection();
//      insertDashboardConfigDatasource(dashboardConfig, conn);
//    } catch (Throwable t) {
//      logger.error("Error on insert dashboardConfig Datasource", t);
//      throw new RuntimeException("Error on insert dashboardConfig DataSource", t);
//    }
//    finally {
//      this.closeDaoResources(null, null, conn);
//    }
//  }
//  
*/
//endregion

  private void insertDashboardConfigDatasource(DatasourcesConfigDto datasource)
      throws SQLException {
    Connection conn = null;
    try {
      conn = this.getConnection();
      insertDashboardConfigDatasource(datasource, conn);
    } catch (Throwable t) {
      logger.error("Error on insert dashboardConfig Datasource", t);
      throw new RuntimeException("Error on insert dashboardConfig DataSource", t);
    } finally {
      this.closeDaoResources(null, null, conn);
    }
  }

  private void insertDashboardConfigDatasource(DatasourcesConfigDto datasourcesConfigDto,
      Connection conn) {
    try {
      final PreparedStatement stat = conn.prepareStatement(ADD_DATASOURCE);
      int index = 1;
      stat.setInt(index++, datasourcesConfigDto.getFk_dashboard_config());
      stat.setString(index++, datasourcesConfigDto.getDatasourceCode());
      stat.setString(index++, datasourcesConfigDto.getDatasource());
      if (StringUtils.isNotBlank(datasourcesConfigDto.getDatasourceURI())) {
        stat.setString(index++, datasourcesConfigDto.getDatasourceURI());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      if (StringUtils.isNotBlank(datasourcesConfigDto.getStatus())) {
        stat.setString(index++, datasourcesConfigDto.getStatus());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      if (StringUtils.isNotBlank(datasourcesConfigDto.getName())) {
        stat.setString(index++, datasourcesConfigDto.getName());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      String metadata = new Gson().toJson(datasourcesConfigDto.getMetadata());
      if (datasourcesConfigDto.getMetadata() != null && StringUtils.isNotBlank(metadata)) {
        stat.setString(index++, metadata);
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      stat.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error on insert dashboardConfig Datasource", t);
      throw new RuntimeException("Error on insert dashboardConfig DataSource", t);
    }
  }

  private void insertDashboardConfigDatasource(DashboardConfig dashboardConfig, Connection conn) {
    dashboardConfig.getDatasources().forEach(c -> {
      try {
        final PreparedStatement stat = conn.prepareStatement(ADD_DATASOURCE);
        int index = 1;
        stat.setInt(index++, dashboardConfig.getId());
        stat.setString(index++, c.getDatasourceCode());
        stat.setString(index++, c.getDatasource());
        if (StringUtils.isNotBlank(c.getDatasourceURI())) {
          stat.setString(index++, c.getDatasourceURI());
        } else {
          stat.setNull(index++, Types.VARCHAR);
        }
        if (StringUtils.isNotBlank(c.getStatus())) {
          stat.setString(index++, c.getStatus());
        } else {
          stat.setNull(index++, Types.VARCHAR);
        }
        if (StringUtils.isNotBlank(c.getName())) {
          stat.setString(index++, c.getName());
        } else {
          stat.setNull(index++, Types.VARCHAR);
        }
        String metadata = new Gson().toJson(c.getMetadata());
        if (c.getMetadata() != null && StringUtils.isNotBlank(metadata)) {
          stat.setString(index++, metadata);
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
      if (dashboardConfig.getType() != null && StringUtils.isNotBlank(dashboardConfig.getType())) {
        stat.setString(index++, dashboardConfig.getType());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      stat.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error on insert dashboardConfig", t);
      throw new RuntimeException("Error on insert dashboardConfig", t);
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

  private void updateDashboardConfigDatasource(final DashboardConfig dashboardConfig, final Connection conn) {
    DashboardConfig dashboardFromDB = this
        .loadDashboardConfig(dashboardConfig.getId());
    
    dashboardConfig.getDatasources().forEach(d -> {
      d.setFk_dashboard_config(dashboardConfig.getId());
      long countSameDatasource = dashboardFromDB.getDatasources().stream()
          .filter(data -> data.getDatasource().equals(d.getDatasource())).count();
      
      if (dashboardFromDB.getDatasources() != null && !dashboardFromDB.getDatasources().equals(new ArrayList<>())) {
        if (countSameDatasource > 0) {
          try {
            updateDatasource(d, conn);
          } catch (Throwable t) {
            logger.error("Error on updateDatasource datasource", t);
            throw new RuntimeException("Error on updateDatasource datasource", t);
          }
        }
      } 
      if(countSameDatasource <= 0) {
        try {
          d.setFk_dashboard_config(dashboardConfig.getId());
          insertDashboardConfigDatasource(d,conn);
        } catch (Throwable t) {
          logger.error("Error on insert datasource", t);
          throw new RuntimeException("Error on insert datasource", t);
        }
      }
    });
    
    List<DatasourcesConfigDto> toRemove = dashboardFromDB.getDatasources().stream()
        .filter(
            data -> dashboardConfig.getDatasources().stream().noneMatch(d -> d.getDatasource().equals(data.getDatasource())
            )
        ).collect(
            Collectors.toList());
    removeDashboardConfigDatasources(toRemove,conn);
  }

  @Override
  public void updateDatasource(DatasourcesConfigDto d) {
    Connection conn = null;
    try {
      conn = this.getConnection();
      updateDatasource(d, conn);
    } catch (Throwable t) {
      logger.error("Error on updateDatasource datasource", t);
      throw new RuntimeException("Error on updateDatasource datasource", t);
    } finally {
      this.closeDaoResources(null, null, conn);
    }
  }

  private void updateDatasource(DatasourcesConfigDto d,
      Connection conn)
      throws SQLException {
    PreparedStatement stat = conn.prepareStatement(UPDATE_DATASOURCE);
    int index = 1;
    stat.setInt(index++, d.getFk_dashboard_config());
    if (StringUtils.isNotBlank(d.getDatasourceCode())) {
      stat.setString(index++, d.getDatasourceCode());
    } else {
      stat.setNull(index++, Types.VARCHAR);
    }
    if (StringUtils.isNotBlank(d.getDatasourceURI())) {
      stat.setString(index++, d.getDatasourceURI());
    } else {
      stat.setNull(index++, Types.VARCHAR);
    }
    if (StringUtils.isNotBlank(d.getStatus())) {
      stat.setString(index++, d.getStatus());
    } else {
      stat.setNull(index++, Types.VARCHAR);
    }
    if (StringUtils.isNotBlank(d.getName())) {
      stat.setString(index++, d.getName());
    } else {
      stat.setNull(index++, Types.VARCHAR);
    }
    String metadata = new Gson().toJson(d.getMetadata());
    if (d.getMetadata() != null && StringUtils.isNotBlank(metadata)) {
      stat.setString(index++, metadata);
    } else {
      stat.setNull(index++, Types.VARCHAR);
    }
    stat.setString(index++, d.getDatasource());
    stat.executeUpdate();
  }

  @Override
  public void removeDashboardConfigDatasources(List<DatasourcesConfigDto> datasources) {
    Connection conn = null;
    try {
      conn = this.getConnection();
      removeDashboardConfigDatasources(datasources, conn);
    } catch (Throwable t) {
      logger.error("Error removing datasources {}", datasources, t);
      throw new RuntimeException("Error removing datasource", t);
    } finally {
      this.closeDaoResources(null, null, conn);
    }
  }

  private void removeDashboardConfigDatasources(List<DatasourcesConfigDto> datasource,
      final Connection conn) {
    datasource.forEach(d -> {
      try {
        PreparedStatement stat = null;
        try {
          stat = conn.prepareStatement(DELETE_DATASOURCE_BY_ID);
          int index = 1;
          stat.setString(index++, d.getDatasource());
          stat.executeUpdate();
        } catch (Throwable t) {
          logger.error("Error removing datasource {}", d.getDatasource(), t);
          throw new RuntimeException("Error updating datasource", t);
        }
      } catch (Throwable t) {
        logger.error("Error removing datasource {}", d.getDatasource(), t);
        throw new RuntimeException("Error removing datasource", t);
      }
    });
  }


  private void updateDashboardConfig(DashboardConfig dashboardConfig, Connection conn) {
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
      if (dashboardConfig.getType() != null && StringUtils.isNotBlank(dashboardConfig.getType())) {
        stat.setString(index++, dashboardConfig.getType());
      } else {
        stat.setNull(index++, Types.VARCHAR);
      }
      stat.setInt(index++, dashboardConfig.getId());
      stat.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error updating dashboardConfig {}", dashboardConfig.getId(), t);
      throw new RuntimeException("Error updating dashboardConfig", t);
    }
  }

  @Override
  public void removeDashboardConfig(int id) {
    PreparedStatement stat = null;
    Connection conn = null;
    try {
      conn = this.getConnection();
      conn.setAutoCommit(false);
      this.removeDashboardConfigDatasources(id, conn);
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

  private void removeDashboardConfigDatasources(int id, Connection conn) {
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

  private void removeDashboardConfig(int id, Connection conn) {
    PreparedStatement stat = null;
    try {
      stat = conn.prepareStatement(DELETE_DASHBOARD_CONFIG);
      int index = 1;
      stat.setInt(index++, id);
      stat.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error deleting dashboardConfig {}", id, t);
      throw new RuntimeException("Error deleting dashboardConfig", t);
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
  public DatasourcesConfigDto loadDatasourceConfigByDatasourceCodeAndDashboardConfig(
      int dashboardId,
      String datasourceCode) {
    Connection conn = null;
    DatasourcesConfigDto datasource = new DatasourcesConfigDto();
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      conn = this.getConnection();
      stat = conn.prepareStatement(LOAD_DATASOURCE_BY_DASHBOARD_AND_DATASOURCECODE);
      stat.setInt(1, dashboardId);
      stat.setString(2, datasourceCode);

      res = stat.executeQuery();
      datasource.setDatasourceCode(res.getString("datasourcecode"));
      datasource.setDatasource(res.getString("datasource"));
      datasource.setDatasourceURI(res.getString("datasourceuri"));
      datasource.setStatus(res.getString("status"));
    } catch (Throwable t) {
      logger.error("Error loading dashboardConfig with dashboardId {}", dashboardId, t);
      throw new RuntimeException("Error loading dashboardConfig with dashboardId " + dashboardId,
          t);
    } finally {
      closeDaoResources(null, null, conn);
    }
    return datasource;
  }

  @Override
  public DatasourcesConfigDto getDatasourceByDatasourceId(String datasourceId) {
    Connection conn = null;
    DatasourcesConfigDto datasource = new DatasourcesConfigDto();
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      conn = this.getConnection();
      stat = conn.prepareStatement(LOAD_DATASOURCE_BY_DATASOURCE_NAME);
      stat.setString(1, datasourceId);
      res = stat.executeQuery();
      while (res.next()) {
        datasource = new DatasourcesConfigDto();
        datasource.setFk_dashboard_config(res.getInt("fk_dashboard_config"));
        datasource.setDatasourceCode(res.getString("datasourcecode"));
        datasource.setDatasource(res.getString("datasource"));
        datasource.setDatasourceURI(res.getString("datasourceuri"));
        datasource.setStatus(res.getString("status"));
        Map<String, Object> metadata = JsonUtils
            .getMapFromJson(new Gson().fromJson(res.getString("metadata"), JsonObject.class));
        datasource.setMetadata(metadata);
      }
    } catch (Throwable t) {
      logger.error("Error loading datasource with id {}", datasourceId, t);
      throw new RuntimeException("Error loading datasource with id " + datasourceId, t);
    } finally {
      closeDaoResources(null, null, conn);
    }
    return datasource;
  }

  private DashboardConfig loadDashboardConfig(int id, Connection conn) {
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
      closeDaoResources(res, stat);
      stat = conn.prepareStatement(LOAD_DATASOURCE);
      stat.setInt(1, id);
      res = stat.executeQuery();
      while (res.next()) {
        DatasourcesConfigDto datasource = new DatasourcesConfigDto();
        datasource.setFk_dashboard_config(res.getInt("fk_dashboard_config"));
        datasource.setDatasourceCode(res.getString("datasourcecode"));
        datasource.setDatasource(res.getString("datasource"));
        datasource.setDatasourceURI(res.getString("datasourceuri"));
        datasource.setStatus(res.getString("status"));
        datasource.setName(res.getString("name"));
        JsonObject metadata = new Gson().fromJson(res.getString("metadata"), JsonObject.class);
        datasource.setMetadata(JsonUtils.getMapFromJson(metadata));
        dashboardConfig.getDatasources().add(datasource);
      }

    } catch (Throwable t) {
      logger.error("Error loading dashboardConfig with id {}", id, t);
      throw new RuntimeException("Error loading dashboardConfig with id " + id, t);
    }
    return dashboardConfig;
  }

  private DashboardConfig buildDashboardConfigFromRes(ResultSet res) {
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
      dashboardConfig.setType(res.getString("type"));
    } catch (Throwable t) {
      logger.error("Error in buildDashboardConfigFromRes", t);
    }
    return dashboardConfig;
  }

  private static final String ADD_DASHBOARD_CONFIG = "INSERT INTO dashboard_config (id, serverdescription, serveruri, username, password, token, timeconnection, active, debug, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String UPDATE_DASHBOARD_CONFIG = "UPDATE dashboard_config SET  serverdescription=?,  serveruri=?,  username=?,  password=?,  token=?,  timeconnection=?,  active=?, debug=?, type = ? WHERE id = ?";

  private static final String DELETE_DASHBOARD_CONFIG = "DELETE FROM dashboard_config WHERE id = ?";

  private static final String LOAD_DASHBOARD_CONFIG = "SELECT id, serverdescription, serveruri, username, password, token, timeconnection, active, debug, type  FROM dashboard_config WHERE id = ?";

  private static final String LOAD_DASHBOARD_CONFIGS_ID = "SELECT id FROM dashboard_config";

  private static final String ADD_DATASOURCE = "INSERT INTO dashboard_config_datasource (fk_dashboard_config, datasourcecode ,datasource, datasourceuri, status , name, metadata) VALUES (?, ?, ?, ?, ?, ? , ?)";

  private static final String DELETE_DATASOURCE = "DELETE FROM dashboard_config_datasource WHERE fk_dashboard_config = ?";

  private static final String LOAD_DATASOURCE = "SELECT fk_dashboard_config, datasourcecode ,datasource, datasourceuri, status , name, metadata FROM dashboard_config_datasource WHERE fk_dashboard_config = ?";

  private static final String LOAD_DATASOURCE_BY_DASHBOARD_AND_DATASOURCECODE = "SELECT * FROM dashboard_config_datasource WHERE fk_dashboard_config = ? AND datasourcecode = ?";

  private static final String COUNT_DATASOURCE_BY_NAME = "SELECT COUNT(*) FROM dashboard_config_datasource WHERE datasource = ?";

  private static final String COUNT_DATASOURCE_BY_CODE = "SELECT COUNT(*) FROM dashboard_config_datasource WHERE datasourcecode = ?";

  private static final String UPDATE_DATASOURCE = "UPDATE dashboard_config_datasource SET fk_dashboard_config = ?, datasourcecode = ?, datasourceuri = ?, status = ?, name = ?, metadata = ? WHERE datasource = ?";

  private static final String COUNT_DATASOURCE_BY_DASHBOARD_ID_AND_NAME = "SELECT COUNT(*) FROM dashboard_config_datasource WHERE dashboard_config_datasource = ? AND datasourcecode = ?";

  private static final String LOAD_DATASOURCE_BY_DATASOURCE_NAME = "SELECT fk_dashboard_config, datasourcecode ,datasource, datasourceuri, status , name, metadata FROM dashboard_config_datasource WHERE datasource = ?";

  private static final String DELETE_DATASOURCE_BY_ID = "DELETE FROM dashboard_config_datasource WHERE datasource = ?";
}