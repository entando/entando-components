package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;

import java.util.List;

public interface IMeasurementConfigManager {

  MeasurementConfig getById(String measurementConfigId);

  MeasurementConfig getByDashboardIdAndDatasourceCdoeAndMeasurementTemplateId(
      int dashboardId, String datasourceCode, String measurementTemplateId);

  void insertMeasurementConfig(MeasurementConfig measurementConfig) throws ApsSystemException;
  
  void updateMeasurementConfig(String measurementConfigId, MeasurementConfig measurementConfig)
      throws ApsSystemException;

}
