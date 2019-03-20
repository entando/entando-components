package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.dao.IMeasurementConfigDAO;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MeasurementConfigManager extends AbstractService implements IMeasurementConfigManager{

  private static final Logger logger = LoggerFactory.getLogger(MeasurementTemplateManager.class);

  @Override
  public void init() throws Exception {
    logger.debug("{} ready.", this.getClass().getName());
  }
  
  @Override
  public MeasurementConfig getById(String measurementConfigId) {
    return this.getMeasurementConfigDAO().loadMeasurementConfig(measurementConfigId);
  }

  @Override
  public MeasurementConfig getByDashboardIdAndDatasourceCdoeAndMeasurementTemplateId(
      int dashboardId, String datasourceCode, String measurementTemplateId) {
    return this.getMeasurementConfigDAO().loadMeasurementConfigByDashboardAndDatasourceAndMeasurementTemplate(dashboardId,datasourceCode, measurementTemplateId);
  }
  
  @Override
  public void insertMeasurementConfig(MeasurementConfig measurementConfig) throws ApsSystemException {
    try {
      String key = String.valueOf(this.getKeyGeneratorManager().getUniqueKeyCurrentValue());
      measurementConfig.setMeasurementConfigId(key);
      this.getMeasurementConfigDAO().insertMeasurementConfig(measurementConfig);
//      this.notifyDashboardConfigChangedEvent(dashboardConfig, DashboardConfigChangedEvent.INSERT_OPERATION_CODE);
    } catch (Throwable t) {
      logger.error("Error adding MeasurementConfig", t);
      throw new ApsSystemException("Error adding MeasurementConfig", t);
    }
  }

  @Override
  public void updateMeasurementConfig(String measurementConfigId,
      MeasurementConfig measurementConfig)
      throws ApsSystemException {
    try {
      this.getMeasurementConfigDAO().updateMeasurementConfig(measurementConfigId, measurementConfig);
//      this.notifyDashboardConfigChangedEvent(dashboardConfig, DashboardConfigChangedEvent.INSERT_OPERATION_CODE);
    } catch (Throwable t) {
      logger.error("Error updating MeasurementConfig", t);
      throw new ApsSystemException("Error updating MeasurementConfig", t);
    }
  }

  protected IKeyGeneratorManager getKeyGeneratorManager() {
    return _keyGeneratorManager;
  }

  public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
    this._keyGeneratorManager = keyGeneratorManager;
  }

  public IMeasurementConfigDAO getMeasurementConfigDAO() {
    return _measurementConfigDAO;
  }

  public void set_measurementConfigDAO(
      IMeasurementConfigDAO _measurementConfigDAO) {
    this._measurementConfigDAO = _measurementConfigDAO;
  }

  public IKeyGeneratorManager get_keyGeneratorManager() {
    return _keyGeneratorManager;
  }

  public void set_keyGeneratorManager(
      IKeyGeneratorManager _keyGeneratorManager) {
    this._keyGeneratorManager = _keyGeneratorManager;
  }

  private IMeasurementConfigDAO _measurementConfigDAO;
  private IKeyGeneratorManager _keyGeneratorManager;
}
