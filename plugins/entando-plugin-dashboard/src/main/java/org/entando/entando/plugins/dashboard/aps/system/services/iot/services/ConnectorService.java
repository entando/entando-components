package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceStatus;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.getDatasource;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.logEndMethod;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.logStartMethod;

@Service
public class ConnectorService extends AbstractConnectorService implements IConnectorService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  ConnectorFactory connectorFactory;

  @Autowired
  DashboardConfigService dashboardConfigService;

  protected ConnectorFactory getConnectorFactory() {
    return connectorFactory;
  }

  public void setConnectorFactory(
      ConnectorFactory connectorFactory) {
    this.connectorFactory = connectorFactory;
  }

  @Override
  public DashboardConfigDto pingDevice(DashboardConfigDto dto, String datasourceCode) {
    logStartMethod(dto.getId(), datasourceCode, this.getClass());
    DashboardConfigDto result = null;
    try {
      result = pingDatasource(dto, datasourceCode);
    } catch (ApiResourceNotAvailableException e) {
      setDatasourceStatus(dto, datasourceCode, DatasourceStatus.OFFLINE);
      throw e;
    }
    dashboardConfigService.updateDatasource(getDatasource(dto, datasourceCode));
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
    return result;
  }

  @Override
  public <T extends DashboardConfigDto> boolean pingServer(T dto) throws IOException {
    logStartMethod(dto.getId(), null, this.getClass());
    return super.isServerReacheable(dto);
  }

  @Override
  public List<DatasourcesConfigDto> getAllDevices(
      DashboardConfigDto dto) {
    logStartMethod(dto.getId(), null, this.getClass());
    List<DatasourcesConfigDto> devices = connectorFactory.getConnector(dto.getType())
        .getAllDevices(dto);
    logEndMethod(dto.getId(), null, true, getClass());
    return devices;
  }

  @Override
  public void setDeviceMeasurementSchema(
      DashboardConfigDto dto, String datasourceCode) {
    logStartMethod(dto.getId(), datasourceCode, this.getClass());
    connectorFactory.getConnector(dto.getType())
        .saveMeasurementTemplate(dto, datasourceCode, DatasourceType.GENERIC);
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
  }

  @Override
  public void saveDeviceMeasurement(
      DashboardConfigDto dto, String datasourceCode,
      String measurementBody) {
    logStartMethod(dto.getId(), datasourceCode, this.getClass());
    connectorFactory.getConnector(dto.getType())
        .saveDeviceMeasurement(dto, datasourceCode, measurementBody);
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
  }

  @Override
  public List<Map<String, Object>> getDeviceMeasurements(DashboardConfigDto dto,
      String datasourceCode, Date startDate, Date endDate,
      RestListRequest restListRequest, DatasourceType type) {
    logStartMethod(dto.getId(), datasourceCode, this.getClass());
    List<Map<String, Object>> measurements;
    try {
      measurements = getDatasourceMeasurements(dto, datasourceCode,
          startDate, endDate,
          restListRequest, type);
    } catch (ApiResourceNotAvailableException e) {
      setDatasourceStatus(dto, datasourceCode, DatasourceStatus.OFFLINE);
      throw e;
    }
    setDatasourceStatus(dto, datasourceCode, DatasourceStatus.ONLINE);
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
    return measurements;
  }

  @Override
  public MeasurementConfig getMeasurementsConfig(DashboardConfigDto dto,
      String datasourceCode, DatasourceType type) {
    logStartMethod(dto.getId(), datasourceCode, this.getClass());
    MeasurementConfig measurementConfig = null;
    try {
      measurementConfig = getMeasurementConfig(dto, datasourceCode, type);
    } catch (ApiResourceNotAvailableException e) {
      setDatasourceStatus(dto, datasourceCode, DatasourceStatus.OFFLINE);
      throw e;
    }
    setDatasourceStatus(dto, datasourceCode, DatasourceStatus.ONLINE);
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
    return measurementConfig;
  }

  @Override
  public MeasurementTemplate getDeviceMeasurementSchema(DashboardConfigDto dto,
      String datasourceCode, DatasourceType type) {
    logStartMethod(dto.getId(), datasourceCode, this.getClass());
    MeasurementTemplate measurementTemplate = null;
    try {
      measurementTemplate = getDatasourceMeasurementTemplate(dto, datasourceCode,
          type);
    } catch (ApiResourceNotAvailableException e) {
      setDatasourceStatus(dto, datasourceCode, DatasourceStatus.OFFLINE);
      throw e;
    }
    setDatasourceStatus(dto, datasourceCode, DatasourceStatus.ONLINE);
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
    return measurementTemplate;
  }

  @Override
  public List<ServerType> getDashboardTypes() {
    return connectorFactory.getServerType();
  }

  @Override
  public DashboardConfigRequest setDevicesMetadata(DashboardConfigRequest dashboardConfigRequest) {
    return connectorFactory.getConnector(dashboardConfigRequest.getType())
        .setDevicesMetadata(dashboardConfigRequest);
  }

  @Override
  public DashboardConfigDto refreshMetadata(DashboardConfigDto dto,
      String datasourceCode) {
    logStartMethod(dto.getId(), datasourceCode, getClass());
    try {
      dto = connectorFactory.getConnector(dto.getType()).refreshMetadata(dto, datasourceCode);
    } catch (ApiResourceNotAvailableException e) {
      setDatasourceStatus(dto, datasourceCode, DatasourceStatus.OFFLINE);
      throw e;
    }
    setDatasourceStatus(dto, datasourceCode, DatasourceStatus.ONLINE);
    DatasourcesConfigDto datasource = getDatasource(dto, datasourceCode);
    dashboardConfigService.updateDatasource(datasource);
    dto = dashboardConfigService.getDashboardConfig(dto.getId());
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
    return dto;
  }

  public void setGeodataDeviceStatuses(DashboardConfigDto dto, String datasourceCode,
      JsonObject body) {
    logStartMethod(dto.getId(), datasourceCode, getClass());
    try {
      getGeodataDatasourceStatuses(dto, datasourceCode, body);
    } catch (ApiResourceNotAvailableException e) {
      setDatasourceStatus(dto, datasourceCode, DatasourceStatus.OFFLINE);
      throw e;
    }
    setDatasourceStatus(dto, datasourceCode, DatasourceStatus.ONLINE);
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
  }

  @Override
  public DeviceLocations getDeviceLocations(DashboardConfigDto dto, String datasourceCode) {
    logStartMethod(dto.getId(), datasourceCode, getClass());
    DeviceLocations deviceLocations = null;
    try {
      deviceLocations = getDatasourceLocations(dto, datasourceCode);
    } catch (ApiResourceNotAvailableException e) {
      setDatasourceStatus(dto, datasourceCode, DatasourceStatus.OFFLINE);
      throw e;
    }
    setDatasourceStatus(dto, datasourceCode, DatasourceStatus.ONLINE);
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
    return deviceLocations;
  }

  @Override
  public boolean isParcheggioAvailable(DashboardConfigDto dto, String datasourceCode) {
    logStartMethod(dto.getId(), datasourceCode, getClass());
    boolean result = false;
    try {
      result = isParcheggioAvaialable(dto, datasourceCode);
    } catch (ApiResourceNotAvailableException e) {
      setDatasourceStatus(dto, datasourceCode, DatasourceStatus.OFFLINE);
      throw e;
    }
    setDatasourceStatus(dto, datasourceCode, DatasourceStatus.ONLINE);
    logEndMethod(dto.getId(), datasourceCode, true, getClass());
    return result;
  }

  private void setDatasourceStatus(DashboardConfigDto dto, String datasourceCode,
      DatasourceStatus online) {
    DatasourcesConfigDto datasource = getDatasource(dto, datasourceCode);
    datasource.setStatus(online);
    dashboardConfigService.updateDatasource(datasource);
  }

  private boolean isParcheggioAvaialable(DashboardConfigDto dto, String datasourceCode) {
    boolean result;
    try {
      result = connectorFactory.getConnector(dto.getType())
          .isParcheggioAvailable(dto, datasourceCode);
    } catch (ApiResourceNotAvailableException e) {
      dto = this.refreshMetadata(dto, datasourceCode);
      result = connectorFactory.getConnector(dto.getType())
          .isParcheggioAvailable(dto, datasourceCode);
    }
    return result;
  }

  private DeviceLocations getDatasourceLocations(DashboardConfigDto dto, String datasourceCode) {
    DeviceLocations deviceLocations;
    try {
      deviceLocations = connectorFactory.getConnector(dto.getType())
          .getDeviceLocations(dto, datasourceCode);
    } catch (ApiResourceNotAvailableException e) {
      dto = this.refreshMetadata(dto, datasourceCode);
      deviceLocations = connectorFactory.getConnector(dto.getType())
          .getDeviceLocations(dto, datasourceCode);
    }
    return deviceLocations;
  }

  private void getGeodataDatasourceStatuses(DashboardConfigDto dto, String datasourceCode,
      JsonObject body) {
    try {
      connectorFactory.getConnector(dto.getType())
          .setGeodataDeviceStatuses(dto, datasourceCode, body);
    } catch (ApiResourceNotAvailableException e) {
      dto = this.refreshMetadata(dto, datasourceCode);
      connectorFactory.getConnector(dto.getType())
          .setGeodataDeviceStatuses(dto, datasourceCode, body);
    }
  }

  private MeasurementTemplate getDatasourceMeasurementTemplate(DashboardConfigDto dto,
      String datasourceCode, DatasourceType type) {
    MeasurementTemplate measurementTemplate = null;
    try {
      measurementTemplate = connectorFactory.getConnector(dto.getType())
          .getDeviceMeasurementSchema(dto, datasourceCode, type);
    } catch (ApiResourceNotAvailableException e) {
      dto = this.refreshMetadata(dto, datasourceCode);
      measurementTemplate = connectorFactory.getConnector(dto.getType())
          .getDeviceMeasurementSchema(dto, datasourceCode, type);
    }
    return measurementTemplate;
  }

  private MeasurementConfig getMeasurementConfig(DashboardConfigDto dto, String datasourceCode,
      DatasourceType type) {
    MeasurementConfig measurementConfig;
    try {
      measurementConfig = connectorFactory.getConnector(dto.getType())
          .getMeasurementConfig(dto, datasourceCode, type);
    } catch (ApiResourceNotAvailableException e) {
      dto = this.refreshMetadata(dto, datasourceCode);
      measurementConfig = connectorFactory.getConnector(dto.getType())
          .getMeasurementConfig(dto, datasourceCode, type);
    }
    return measurementConfig;
  }

  public DashboardConfigDto pingDatasource(DashboardConfigDto dto, String datasourceCode) {
    DashboardConfigDto result;
    try {
      result = connectorFactory.getConnector(dto.getType()).pingDevice(dto, datasourceCode);
    } catch (ApiResourceNotAvailableException e) {
      dto = this.refreshMetadata(dto, datasourceCode);
      result = connectorFactory.getConnector(dto.getType()).pingDevice(dto, datasourceCode);
      dashboardConfigService.updateDatasource(getDatasource(result, datasourceCode));
    }
    return result;
  }

  private List<Map<String, Object>> getDatasourceMeasurements(DashboardConfigDto dto,
      String datasourceCode, Date startDate, Date endDate, RestListRequest restListRequest,
      DatasourceType type) {
    List<Map<String, Object>> measurements;
    try {
      measurements = connectorFactory.getConnector(dto.getType())
          .getMeasurements(dto, datasourceCode, startDate, endDate, restListRequest, type);
    } catch (ApiResourceNotAvailableException e) {
      dto = this.refreshMetadata(dto, datasourceCode);
      measurements = connectorFactory.getConnector(dto.getType())
          .getMeasurements(dto, datasourceCode, startDate, endDate, restListRequest, type);
    }
    return measurements;
  }
}
