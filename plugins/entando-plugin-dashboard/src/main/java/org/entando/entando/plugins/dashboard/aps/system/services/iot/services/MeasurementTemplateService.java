package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MeasurementTemplateService implements IMeasurementTemplateService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private IMeasurementTemplateManager dashboardConfigManager;

  protected IMeasurementTemplateManager getMeasurementTemplateManager() {
    return dashboardConfigManager;
  }

  public void setMeasurementTemplateManager(IMeasurementTemplateManager measurementTemplateManager) {
    this.dashboardConfigManager = measurementTemplateManager;
  }


  @Override
  public void save(MeasurementTemplate measurementTemplate) {
    try {
      getMeasurementTemplateManager().insertMeasurementTemplate(measurementTemplate);
    } catch (ApsSystemException e) {
      logger.error("error measurementTemplate",e);
    }
  }

  @Override
  public MeasurementTemplate getById(String id) {
    try {
      return getMeasurementTemplateManager().getMeasurementTemplate(id);
    } catch (ApsSystemException e) {
      logger.error("error measurementTemplate",e);
    }
    return null;
  }
  
  @Override
  public MeasurementTemplate getByDashboardIdAndDatasourceCode(int dashboardId, String datasourceCode) {
    try {
      return getMeasurementTemplateManager().getMeasurementTemplateByDashboardIdAndDatasourceCode(dashboardId, datasourceCode);
    } catch (ApsSystemException e) {
      logger.error("error measurementTemplate",e);
    }
    return null;
  }
}
