package org.entando.entando.plugins.dashboard.aps.system.services.iot.dao;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;

import java.util.List;

public interface IMeasurementConfigDAO {

  MeasurementConfig loadMeasurementConfig(String measurementConfigId);

  MeasurementConfig loadMeasurementConfigByDashboardAndDatasourceAndMeasurementTemplate(
      int dashboardId, String datasourceCode, String measurementTemplate);

  void insertMeasurementConfig(MeasurementConfig measurementConfig);
  
  void updateMeasurementConfig(String measurementConfigId, MeasurementConfig measurementConfig);

  int countMeasurementConfigByDashboardidAndDatasource(int dashboardId, String datasourceCode);
}
