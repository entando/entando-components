package org.entando.entando.plugins.dashboard.aps.system.services.iot.dao;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.bytebuddy.implementation.bytecode.Throw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MeasurementTemplateDAO extends AbstractSearcherDAO implements IMeasurementTemplateDAO {

  private static final Logger logger = LoggerFactory.getLogger(MeasurementTemplateDAO.class);

  @Override
  public List<Integer> searchMeasurementTemplate(FieldSearchFilter[] filters) {
    return null;
  }

  @Override
  public MeasurementTemplate loadMeasurementTemplateByDashboardIdAndDatasourceCode(int dashboardId, String datasourceCode) {
    MeasurementTemplate measurementTemplate = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    Connection conn = null;
    try{
      conn= this.getConnection();
      measurementTemplate = this.loadMeasurementTemplateByDashboardIdAndDatasourceCode(dashboardId, datasourceCode,conn);
    } catch (Throwable t) {
      logger.error("Error loading MeasurementTemplate with ids {} {} ", dashboardId, datasourceCode, t);
      throw new RuntimeException(String.format("Error loading MeasurementTemplate with ids {} {} ", dashboardId, datasourceCode), t);
    } finally {
      closeDaoResources(res, stat, conn);
    }
    return measurementTemplate;
  }

  private MeasurementTemplate loadMeasurementTemplateByDashboardIdAndDatasourceCode(int dashboardId,
      String datasourceCode, Connection conn) {
    MeasurementTemplate measurementTemplate = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      stat = conn.prepareStatement(LOAD_MEASUREMENT_TEMPLATE_BY_DASHBOARDID_AND_DATASOURCE_CODE);
      stat.setInt(1, dashboardId);
      stat.setString(2, datasourceCode);
      res = stat.executeQuery();
      measurementTemplate.setId(res.getString("id"));
      measurementTemplate.setFields(res.getString("fields"));
    } catch (Throwable t) {
      logger.error("Error loading MeasurementTemplate with ids {} {} ", dashboardId, datasourceCode, t);
      throw new RuntimeException(String.format("Error loading MeasurementTemplate with ids {} {} ", dashboardId, datasourceCode), t);
    } finally {
      closeDaoResources(res, stat, null);
    }
    return measurementTemplate;
  }


  @Override
  public MeasurementTemplate loadMeasurementTemplate(String id) {
    MeasurementTemplate measurementTemplate = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    Connection conn = null;
    try{
      conn = this.getConnection();
      measurementTemplate = this.loadMeasurementTemplate(id,conn);
    }
    catch(Throwable t){
      logger.error("Error loading MeasurementTemplate with id {}", id, t);
      throw new RuntimeException(String.format("Error loading MeasurementTemplate with id {} ", id), t);
    } finally {
      closeDaoResources(res, stat, conn);
    }
    return measurementTemplate;
  }

  private MeasurementTemplate loadMeasurementTemplate(String id, Connection conn) {
    MeasurementTemplate measurementTemplate = null;
    PreparedStatement stat = null;
    ResultSet res = null;
    try {
      stat = conn.prepareStatement(LOAD_MEASUREMENT_TEMPLATE);
      stat.setString(1, id);
      res = stat.executeQuery();
      measurementTemplate.setId(res.getString("id"));
      measurementTemplate.setFields(res.getString("fields"));
    } catch (Throwable t) {
      logger.error("Error loading measurementTemplate with Id {}", id, t);
      throw new RuntimeException("Error loading measurementTemplate with Id " + id, t);
    } finally {
      closeDaoResources(res, stat, null);
    }
    return measurementTemplate;
  }

  @Override
  public void updateMeasurementTemplate(String id, List<MeasurementType> fields, final Connection conn) {
  }

  @Override
  public void insertMeasurementTemplate(MeasurementTemplate measurementTemplate) {
    PreparedStatement stat = null;
    Connection conn = null;
    try {
      conn = this.getConnection();
      conn.setAutoCommit(false);
      this.insertMeasurementTemplate(measurementTemplate, conn);
      conn.commit();
    } catch (Throwable t) {
      this.executeRollback(conn);
      logger.error("Error on insert MeasurementTemplate", t);
      throw new RuntimeException("Error on insert Measurementtemplate", t);
    } finally {
      this.closeDaoResources(null, stat, conn);
    }
  }

  public void insertMeasurementTemplate(MeasurementTemplate measurementTemplate, final Connection conn){
    final PreparedStatement statement;
    try {
      statement = conn.prepareStatement(ADD_MEASUREMENT_TEMPLATE);

      statement.setString(1, measurementTemplate.getId());
      statement.setString(2, new Gson().toJson(measurementTemplate.getFields()));

      statement.executeUpdate();
    }
     catch (Throwable t) {
       logger.error("Error on insert MeasurementTemplate", t);
       throw new RuntimeException("Error on insert MeasurementTemplate", t);
    }
  }
  
  private static final String ADD_MEASUREMENT_TEMPLATE = "INSERT INTO measurement_template(id, fields) values ( ? , ? )";
  private static final String LOAD_MEASUREMENT_TEMPLATE = "SELECT * FROM measurement_template WHERE id = ?";
  private static final String LOAD_MEASUREMENT_TEMPLATE_BY_DASHBOARDID_AND_DATASOURCE_CODE = "SELECT * FROM measurement_template WHERE dashboardid = ? AND datasourcecode = ?";

  @Override
  protected String getTableFieldName(String metadataFieldKey) {
    return metadataFieldKey;
  }

  @Override
  protected String getMasterTableName() {
    return "measurement_template";
  }

  @Override
  protected String getMasterTableIdFieldName() {
    return "id";
  }
}
