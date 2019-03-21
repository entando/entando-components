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
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
  public boolean pingDevice(IDashboardDatasourceDto device) throws IOException {
    logger.info("{} pingDevice on {}", this.getClass().getSimpleName(), device.getDashboardUrl());
    return connectorFactory.getConnector(device.getDashboardConfigDto().getServerDescription()).pingDevice(device);
  }

  @Override
  public <T extends DashboardConfigDto> boolean pingServer(T dashboardConfigDto)
      throws IOException {
    logger.info("{} ping to {}", this.getClass().getSimpleName(), dashboardConfigDto.getServerURI());  
    return super.isServerReacheable(dashboardConfigDto);
  }

  @Override
  public <T extends DashboardConfigDto> List<? extends AbstractDashboardDatasourceDto> getAllDevices(
      T dashboardConfigDto) {
    logger.info("{} getAllDevices to {}", this.getClass().getSimpleName(), dashboardConfigDto.getServerURI());
    return connectorFactory.getConnector(dashboardConfigDto.getServerDescription()).getAllDevices(dashboardConfigDto);
  }
  
  
  @Override
  public PagedMetadata<Map<String, Object>> getMeasurements(RestListRequest requestList) {
      try {
          List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
          filters
                 .stream()
                 .filter(i -> i.getKey() != null)
                 .forEach(i -> i.setKey(DashboardConfigDto.getEntityFieldName(i.getKey())));
          
          List<Map<String, Object>> lista = new ArrayList<Map<String,Object>>();
          
          Map<String, Object> lisa1 = new HashMap<String, Object>();
          Map<String, Object> lisa2 = new HashMap<String, Object>();
      lisa1.put("temperature", "5");
      lisa1.put("date", "12/12/2012");

      lisa2.put("temperature", "15");
      lisa2.put("date", "12/12/2013");
      lista.add(lisa1);
      lista.add(lisa2);
  		
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
      IDashboardDatasourceDto dashboardDatasourceDto) throws ApsSystemException {
    logger.info("{} getSchema to {}", this.getClass().getSimpleName(), dashboardDatasourceDto.getDashboardConfigDto().getServerURI());
    connectorFactory.getConnector(dashboardDatasourceDto.getDashboardConfigDto().getServerDescription()).saveMeasurementTemplate(dashboardDatasourceDto);;
  }

  @Override
  public JsonObject saveDeviceMeasurement(
      IDashboardDatasourceDto dashboardDatasourceDto, JsonArray measurementBody)
      throws Exception {
    logger.info("{} saveDeviceMeasurement to {}", this.getClass().getSimpleName(), dashboardDatasourceDto.getDashboardConfigDto().getServerURI());
    return connectorFactory.getConnector(dashboardDatasourceDto.getDashboardConfigDto().getServerDescription()).saveDeviceMeasurement(dashboardDatasourceDto, measurementBody);
  }

  @Override
  public IDashboardDatasourceDto getDashboardDatasourceDtobyIdAndCodeAndServerType(DashboardConfigDto dashboardConfigDto,
      String datasourceCode, String serverType) {
    logger.info("{} getDashboardDatasourceDtoByIdAndCode ids :{}, {}", this.getClass().getSimpleName(), dashboardConfigDto.getId() ,datasourceCode);
    return connectorFactory.getConnector(serverType).getDashboardDatasourceDtoByIdAndCode(dashboardConfigDto, datasourceCode);
  }

  @Override
  public List<MeasurementObject> getDeviceMeasurements(IDashboardDatasourceDto dto, Long nMeasurements, Date startDate, Date endDate) {
    logger.info("{} getDeviceMeasurement By Dashboard and datasource ids :{}, {}", this.getClass().getSimpleName(), dto.getDashboardId() ,dto.getDatasourceCode());
    return connectorFactory.getConnector(dto.getServerType()).getMeasurements(dto,nMeasurements,startDate,endDate);
  }
}
