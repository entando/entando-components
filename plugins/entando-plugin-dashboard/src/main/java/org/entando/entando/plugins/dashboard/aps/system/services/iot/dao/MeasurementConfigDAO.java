package org.entando.entando.plugins.dashboard.aps.system.services.iot.dao;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.google.gson.Gson;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MeasurementConfigDAO extends AbstractSearcherDAO implements IMeasurementConfigDAO{

  private static final String LOAD_MEASUREMENT_CONFIG = "SELECT * FROM measurement_config WHERE measurement_config_id = ?";

  private static final String LOAD_MEASUREMENT_CONFIG_BY_DASHBOARDID_AND_DATASOURCECODE_AND_MEASUREMENTTYPEID= "SELECT * FROM measurement_config WHERE dashboard_id = ? AND datasource_code = ? AND measurement_template_id = ?";

  private static final String INSERT_MEASUREMENT_CONFIG = "INSERT INTO measurement_config(measurement_config_id, dashboard_id, datasource_code, measurement_template_id, mappings, active) VALUES ?, ?, ?, ?, ?, ?";

  private static final String UPDATE_MEASUREMENT_CONFIG = "UPDATE measurement_config SET dashboard_id = ?, datasource_code = ?, measurement_template_id = ?, mappings = ? WHERE measurement_config_id = ?";

  private static final String COUNT_MEASUREMENT_CONFIG_BY_DASHBOARDID_AND_DATASOURCE = "SELECT COUNT(*) FROM measurement_config WHERE dashboard_id = ? AND datasource_code = ?";

  private static final Logger logger = LoggerFactory.getLogger(MeasurementTemplateDAO.class);

  @Override
  protected String getTableFieldName(String metadataFieldKey) {
    return metadataFieldKey;
  }

  @Override
  protected String getMasterTableName() {
    return "measurement_config";
  }

  @Override
  protected String getMasterTableIdFieldName() {
    return "measurementConfigId";
  }

  @Override
  public MeasurementConfig loadMeasurementConfig(String measurementConfigId) {
    MeasurementConfig measurementConfig = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    Connection conn = null;
    try{
      conn = this.getConnection();
      measurementConfig = this.loadMeasurementConfig(measurementConfigId,conn);
    }
    catch(Throwable t){
      logger.error("Error loading MeasurementTemplate with id {}", measurementConfigId, t);
      throw new RuntimeException(String.format("Error loading MeasurementTemplate with id {} ", measurementConfigId), t);
    } finally {
      closeDaoResources(res, stat, null);
    }
    return measurementConfig;
  }

  private MeasurementConfig loadMeasurementConfig(String measurementConfigId, Connection conn) {
    MeasurementConfig measurementConfig = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      stat = conn.prepareStatement(LOAD_MEASUREMENT_CONFIG);
      stat.setString(1, measurementConfigId);
      res = stat.executeQuery();
      measurementConfig.setDashboardId(res.getInt("dashboard_id"));
      measurementConfig.setDatasourceCode(res.getString("datasource_code"));
      measurementConfig.setMeasurementTemplateId(res.getString("measurement_template_id"));
      measurementConfig.setMappings(res.getString("mappings"));
    } catch (Throwable t) {
      logger.error("Error loading dashboardConfig with dashboardId {}", measurementConfigId, t);
      throw new RuntimeException("Error loading dashboardConfig with dashboardId " + measurementConfigId, t);
    } finally {
      closeDaoResources(res, stat, null);
    }
    return measurementConfig;
  }

  @Override
  public MeasurementConfig loadMeasurementConfigByDashboardAndDatasourceAndMeasurementTemplate(
      int dashboardId, String datasourceCode, String measurementTemplate) {
    MeasurementConfig measurementConfig = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    Connection conn = null;
    try{
      conn = this.getConnection();
      measurementConfig = this.loadMeasurementConfigByDashboardAndDatasourceAndMeasurementTemplate(dashboardId, datasourceCode, measurementTemplate, conn);
    }
    catch(Throwable t){
      logger.error("Error loading MeasurementTemplate By Dashboard Datasource, MeasurementTemplate with ids {},{},{}", dashboardId,datasourceCode,measurementTemplate, t);
      throw new RuntimeException(String.format("Error loading MeasurementTemplate By Dashboard Datasource, MeasurementTemplate with ids {},{},{}", dashboardId,datasourceCode,measurementTemplate), t);
    } finally {
      closeDaoResources(res, stat, null);
    }
    return measurementConfig;
  }

  private MeasurementConfig loadMeasurementConfigByDashboardAndDatasourceAndMeasurementTemplate(int dashboardId, String datasourceCode, String measurementTemplate, Connection conn) {
    MeasurementConfig measurementConfig = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      stat = conn.prepareStatement(LOAD_MEASUREMENT_CONFIG_BY_DASHBOARDID_AND_DATASOURCECODE_AND_MEASUREMENTTYPEID);
      stat.setInt(1, dashboardId);
      stat.setString(2, datasourceCode);
      stat.setString(3, measurementTemplate);
      res = stat.executeQuery();
      measurementConfig.setDashboardId(res.getInt("dashboard_id"));
      measurementConfig.setDatasourceCode(res.getString("datasource_code"));
      measurementConfig.setMeasurementTemplateId(res.getString("measurement_template_id"));
      measurementConfig.setMappings(res.getString("mappings"));
    } catch (Throwable t) {
      logger.error("Error loading MeasurementTemplate By Dashboard Datasource, MeasurementTemplate with ids {},{},{}", dashboardId,datasourceCode,measurementTemplate, t);
      throw new RuntimeException(String.format("Error loading MeasurementTemplate By Dashboard Datasource, MeasurementTemplate with ids {},{},{}", dashboardId,datasourceCode,measurementTemplate), t);
    } finally {
      closeDaoResources(res, stat, null);
    }
    return measurementConfig;
  }

  @Override
  public void insertMeasurementConfig(MeasurementConfig measurementConfig) {
    PreparedStatement stat = null;
    Connection conn = null;
    try {
      conn = this.getConnection();
      conn.setAutoCommit(false);
      this.insertMeasurementConfig(measurementConfig, conn);
      conn.commit();
    } catch (Throwable t) {
      this.executeRollback(conn);
      logger.error("Error on insert MeasurementConfig", t);
      throw new RuntimeException("Error on insert MeasurementConfig", t);
    } finally {
      this.closeDaoResources(null, stat, conn);
    }
  }

  public void insertMeasurementConfig(MeasurementConfig measurementConfig, final Connection conn){
    final PreparedStatement statement;
    try {
      statement = conn.prepareStatement(INSERT_MEASUREMENT_CONFIG);

      statement.setString(1, measurementConfig.getMeasurementConfigId());
      statement.setInt(2, measurementConfig.getDashboardId());
      statement.setString(3, measurementConfig.getDatasourceCode());
      statement.setString(4, measurementConfig.getMeasurementTemplateId());
      statement.setString(5, new Gson().toJson(measurementConfig.getMappings()));
      statement.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error on insert MeasurementTemplate", t);
      throw new RuntimeException("Error on insert MeasurementTemplate", t);
    }
  }

  @Override
  public void updateMeasurementConfig(String measurementConfigId,
      MeasurementConfig measurementConfig) {
    PreparedStatement stat = null;
    Connection conn = null;
    try {
      conn = this.getConnection();
      conn.setAutoCommit(false);
      this.updateMeasurementConfig(measurementConfigId, measurementConfig, conn);
      conn.commit();
    } catch (Throwable t) {
      this.executeRollback(conn);
      logger.error("Error on update MeasurementConfig", t);
      throw new RuntimeException("Error on update MeasurementConfig", t);
    } finally {
      this.closeDaoResources(null, stat, conn);
    }
  }

  public void updateMeasurementConfig(String measurementConfigId, MeasurementConfig measurementConfig, final Connection conn){
    final PreparedStatement statement;
    try {
      statement = conn.prepareStatement(UPDATE_MEASUREMENT_CONFIG);

      statement.setString(5, measurementConfigId);

      statement.setInt(1, measurementConfig.getDashboardId());
      statement.setString(2, measurementConfig.getDatasourceCode());
      statement.setString(3, measurementConfig.getMeasurementTemplateId());
      statement.setString(4, new Gson().toJson(measurementConfig.getMappings()));
      statement.executeUpdate();
    } catch (Throwable t) {
      logger.error("Error on update MeasurementTemplate", t);
      throw new RuntimeException("Error on update MeasurementTemplate", t);
    }
  }

  @Override
  public int countMeasurementConfigByDashboardidAndDatasource(int dashboardId, String datasourceCode) {
    int res = 0;
    PreparedStatement stat = null;
    Connection conn = null;
    try {
      conn = this.getConnection();
      return this.countMeasurementConfigByDashboardidAndDatasource(dashboardId, datasourceCode,conn);
    } catch (Throwable t) {
      this.executeRollback(conn);
      logger.error("Error on count MeasurementConfig", t);
      throw new RuntimeException("Error on count MeasurementConfig", t);
    } finally {
      this.closeDaoResources(null, stat, conn);
    }
  }

  public int countMeasurementConfigByDashboardidAndDatasource(int dashboardId, String datasourceCode, Connection conn) {
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      stat = conn.prepareStatement(COUNT_MEASUREMENT_CONFIG_BY_DASHBOARDID_AND_DATASOURCE);
      stat.setInt(1, dashboardId);
      stat.setString(2, datasourceCode);
      res = stat.executeQuery();
      return res.getInt(1);
    } catch (Throwable t) {
      logger.error("Error counting MeasurementTemplate By Dashboard Datasource with ids {},{}", dashboardId,datasourceCode, t);
      throw new RuntimeException(String.format("Error counting MeasurementTemplate By Dashboard Datasource with ids {},{} ", dashboardId,datasourceCode), t);
    } finally {
      closeDaoResources(res, stat, null);
    }
  }
}
