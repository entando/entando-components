package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;

public interface IMeasurementTemplateManager {

  MeasurementTemplate getMeasurementTemplate(String id) throws ApsSystemException;

  void insertMeasurementTemplate(MeasurementTemplate measurementTemplate) throws ApsSystemException;
  
}
