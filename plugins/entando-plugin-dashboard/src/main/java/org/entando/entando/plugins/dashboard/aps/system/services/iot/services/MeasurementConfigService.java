package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementConfigService implements IMeasurementConfigService {

  @Autowired
  MeasurementConfigManager measurementConfigManager;

  @Override
  public void save(MeasurementConfig measurementConfig) throws ApsSystemException {
    if (getByDashboardIdAndDatasourceCodeAndMeasurementTemplateId(
        measurementConfig.getDashboardId(), 
        measurementConfig.getDatasourceCode(),
        measurementConfig.getMeasurementTemplateId())!= null) {
      measurementConfigManager.insertMeasurementConfig(measurementConfig);
    }
  }

  @Override
  public void addMapping(String measurementConfigId, List<MeasurementMapping> measurementMappings)
      throws ApsSystemException {
    MeasurementConfig measurementConfig = measurementConfigManager.getById(measurementConfigId);
    measurementMappings.forEach(mapping -> {
      if (!measurementConfig.getMappings().contains(mapping)) {
        measurementConfig.getMappings().add(mapping);
      }
    });
    this.updateMappings(measurementConfig);
  }

  private void updateMappings(MeasurementConfig measurementConfig) throws ApsSystemException {
    measurementConfigManager.updateMeasurementConfig(measurementConfig.getMeasurementConfigId(), measurementConfig);
  }

  @Override
  public MeasurementConfig getById(String measurementConfigId) {
    return measurementConfigManager.getById(measurementConfigId);
  }

  @Override
  public MeasurementConfig getByDashboardIdAndDatasourceCodeAndMeasurementTemplateId(
      int dashboardId, String datasourceCode, String measurementTemplateId) {
    return measurementConfigManager.getByDashboardIdAndDatasourceCdoeAndMeasurementTemplateId(dashboardId, datasourceCode,
            measurementTemplateId);
  }
}
