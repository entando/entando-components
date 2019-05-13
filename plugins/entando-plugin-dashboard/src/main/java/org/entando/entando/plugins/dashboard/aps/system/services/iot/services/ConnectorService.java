package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.ParkingStatuses;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.logStartMethod;

@Service
public class ConnectorService extends AbstractConnectorService implements IConnectorService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  ConnectorFactory connectorFactory;

  protected ConnectorFactory getConnectorFactory() {
    return connectorFactory;
  }

  public void setConnectorFactory(
      ConnectorFactory connectorFactory) {
    this.connectorFactory = connectorFactory;
  }

  @Override
  public LinkedHashMap<String, String> getConnectorTypes() throws IOException {
    return null;
  }

  @Override
  public DashboardConfigDto pingDevice(DashboardConfigDto dto, String datasourceCode)
      throws IOException, ApsSystemException {
    logStartMethod(dto.getId(),datasourceCode,this.getClass());
    DashboardConfigDto result = connectorFactory.getConnector(dto.getType())
        .pingDevice(dto, datasourceCode);
    return result;
  }

  @Override
  public <T extends DashboardConfigDto> boolean pingServer(T dto)
      throws IOException {
    logStartMethod(dto.getId(), null ,this.getClass());
    return super.isServerReacheable(dto);
  }

  @Override
  public List<DatasourcesConfigDto> getAllDevices(
      DashboardConfigDto dto) {
    logStartMethod(dto.getId(),null,this.getClass());
    return connectorFactory.getConnector(dto.getType()).getAllDevices(dto);
  }


  @Override
  public PagedMetadata<Map<String, Object>> getMeasurements(RestListRequest requestList) {
    try {
      List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
      filters
          .stream()
          .filter(i -> i.getKey() != null)
          .forEach(i -> i.setKey(DashboardConfigDto.getEntityFieldName(i.getKey())));

      List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();

      Map<String, Object> lisa1 = new HashMap<String, Object>();
      Map<String, Object> lisa2 = new HashMap<String, Object>();
      Map<String, Object> lisa3 = new HashMap<String, Object>();
      lisa1.put("temperature", "5");
      lisa1.put("timestamp",  System.currentTimeMillis());

      lisa2.put("temperature", "15");
      lisa2.put("timestamp",  System.currentTimeMillis() + "5000");
      lisa3.put("temperature", "25");
      lisa3.put("timestamp", System.currentTimeMillis() + "10000");
      lista.add(lisa1);
      lista.add(lisa2);
      lista.add(lisa3);

      SearcherDaoPaginatedResult<Map<String, Object>> langsResult = new SearcherDaoPaginatedResult<>(lista.size(), lista);
      langsResult.setCount(lista.size());


      PagedMetadata<Map<String, Object>> pagedMetadata = new PagedMetadata<>(requestList, langsResult);
      pagedMetadata.setBody(lista);
      return pagedMetadata;
    } catch (Throwable t) {
      logger.error("error in search dashboardConfigs", t);
      throw new RestServerError("error in search dashboardConfigs", t);
    }
  }


  @Override
  public void setDeviceMeasurementSchema(
      DashboardConfigDto dto, String datasourceCode) throws ApsSystemException {
    logStartMethod(dto.getId(),datasourceCode,this.getClass());
    connectorFactory.getConnector(dto.getType()).saveMeasurementTemplate(dto, datasourceCode);
    ;
  }

  @Override
  public void saveDeviceMeasurement(
      DashboardConfigDto dto, String datasourceCode,
      String measurementBody){
    logStartMethod(dto.getId(),datasourceCode,this.getClass());
    connectorFactory.getConnector(dto.getType()).saveDeviceMeasurement(dto, datasourceCode, measurementBody);
  }

  @Override
  public List<Map<String, Object>> getDeviceMeasurements(DashboardConfigDto dto,
      String datasourceCode, Date startDate, Date endDate,
      RestListRequest restListRequest) {
    logStartMethod(dto.getId(),datasourceCode,this.getClass());
    return connectorFactory.getConnector(dto.getType()).getMeasurements(dto, datasourceCode, startDate, endDate, restListRequest);
  }

  @Override
  public MeasurementConfig getMeasurementsConfig(DashboardConfigDto dto,
      String datasourceCode) {
    logStartMethod(dto.getId(),datasourceCode,this.getClass());
    return connectorFactory.getConnector(dto.getType()).getMeasurementConfig(dto, datasourceCode);
  }

  @Override
  public MeasurementTemplate getDeviceMeasurementSchema(DashboardConfigDto dto,
      String datasourceCode) {
    logStartMethod(dto.getId(),datasourceCode,this.getClass());
    return connectorFactory.getConnector(dto.getType()).getDeviceMeasurementSchema(dto, datasourceCode);
  }

  @Override
  public List<ServerType> getDashboardTypes() {
    return connectorFactory.getServerType();
  }

  @Override
  public DashboardConfigRequest setDevicesMetadata(DashboardConfigRequest dashboardConfigRequest)
      throws ApsSystemException {
    return connectorFactory.getConnector(dashboardConfigRequest.getType()).setDevicesMetadata(dashboardConfigRequest);
  }

  @Override
  public DashboardConfigDto refreshMetadata(DashboardConfigDto dto,
      String datasourceCode) throws ApsSystemException {
    return connectorFactory.getConnector(dto.getType()).refreshMetadata(dto,datasourceCode);
  }

  @Override
  public void setDeviceStatuses(DashboardConfigDto dto, String datasourceCode, ParkingStatuses parkingStatuses) {
    connectorFactory.getConnector(dto.getType()).setDeviceStatuses(dto, datasourceCode, parkingStatuses);
  }

  @Override
  public DeviceLocations getDeviceLocations(DashboardConfigDto dto, String datasourceCode) {
    return connectorFactory.getConnector(dto.getType()).getDeviceLocations(dto,datasourceCode);
  }
}
