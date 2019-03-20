package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementMapping;

import java.util.List;

public interface IMeasurementConfigService {

  void save(MeasurementConfig measurementConfig) throws ApsSystemException;

  void addMapping(String measurementConfigId, List<MeasurementMapping> measurementMappings)
      throws ApsSystemException;

  MeasurementConfig getById(String measurementConfigId);

  MeasurementConfig getByDashboardIdAndDatasourceCodeAndMeasurementTemplateId(
      int dashboardId, String datasourceCode, String measurementTemplateId);
}
