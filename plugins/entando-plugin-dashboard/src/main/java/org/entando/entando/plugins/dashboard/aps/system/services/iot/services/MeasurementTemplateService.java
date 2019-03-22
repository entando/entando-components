package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.Measurement;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasurementTemplateService implements IMeasurementTemplateService {

  @Autowired
  private IMeasurementTemplateManager dashboardConfigManager;

  protected IMeasurementTemplateManager getMeasurementTemplateManager() {
    return dashboardConfigManager;
  }

  public void setMeasurementTemplateManager(IMeasurementTemplateManager measurementTemplateManager) {
    this.dashboardConfigManager = measurementTemplateManager;
  }


  @Override
  public void save(MeasurementTemplate measurementTemplate)
      throws ApsSystemException {
    
    getMeasurementTemplateManager().insertMeasurementTemplate(measurementTemplate);
  }

  @Override
  public MeasurementTemplate getById(String id) throws ApsSystemException {
    return getMeasurementTemplateManager().getMeasurementTemplate(id);
  }
  
  @Override
  public MeasurementTemplate getByDashboardIdAndDatasourceCode(int dashboardId, String datasourceCode)
      throws ApsSystemException {
    return getMeasurementTemplateManager().getMeasurementTemplateByDashboardIdAndDatasourceCode(dashboardId, datasourceCode);
  }
}
