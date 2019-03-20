package org.entando.entando.plugins.dashboard.aps.system.services.iot.dao;

import com.agiletec.aps.system.common.FieldSearchFilter;

import org.entando.entando.plugins.dashboard.aps.system.init.servdb.DashboardConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IMeasurementTemplateDAO {

  List<Integer> searchMeasurementTemplate(FieldSearchFilter[] filters);

  MeasurementTemplate loadMeasurementTemplate(String id);

  void updateMeasurementTemplate(String id, List<MeasurementType> fields, final Connection conn);

  void insertMeasurementTemplate (MeasurementTemplate measurementTemplate);
}
