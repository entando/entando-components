package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigManager;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigDAO;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.event.DashboardConfigChangedEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dao.IMeasurementTemplateDAO;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasurementTemplateManager extends AbstractService implements IMeasurementTemplateManager {

  private static final Logger logger = LoggerFactory.getLogger(MeasurementTemplateManager.class);
  
  @Override
  public void init() throws Exception {
    logger.debug("{} ready.", this.getClass().getName());
  }

  @Override
  public MeasurementTemplate getMeasurementTemplate(String id) throws ApsSystemException {
    MeasurementTemplate measurementTemplate = null;
    try{
      measurementTemplate = this.getMeasurementTemplateDAO().loadMeasurementTemplate(id);
    }
    catch (Throwable t) {
      logger.error(String.format("Error loading measurementTemplate with id '{}'", id), t);
      throw new ApsSystemException(String.format("Error loading measurementTemplate with id '{}'", id), t);
    }
    return measurementTemplate;
  }

  @Override
  public void insertMeasurementTemplate(MeasurementTemplate measurementTemplate)
      throws ApsSystemException {
    try {
      String key = String.valueOf(this.getKeyGeneratorManager().getUniqueKeyCurrentValue());
      measurementTemplate.setId(key);
      this.getMeasurementTemplateDAO().insertMeasurementTemplate(measurementTemplate);
//      this.notifyDashboardConfigChangedEvent(dashboardConfig, DashboardConfigChangedEvent.INSERT_OPERATION_CODE);
    } catch (Throwable t) {
      logger.error("Error adding MeasurementTemplate", t);
      throw new ApsSystemException("Error adding MeasurementTemplate", t);
    }
  }
  
  protected IMeasurementTemplateDAO getMeasurementTemplateDAO(){
    return _measurementTemplateDAO;
  }
  
  protected IKeyGeneratorManager getKeyGeneratorManager() {
    return _keyGeneratorManager;
  }
  
  public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
    this._keyGeneratorManager = keyGeneratorManager;
  }
  
  private IMeasurementTemplateDAO _measurementTemplateDAO;
  private IKeyGeneratorManager _keyGeneratorManager;
}
