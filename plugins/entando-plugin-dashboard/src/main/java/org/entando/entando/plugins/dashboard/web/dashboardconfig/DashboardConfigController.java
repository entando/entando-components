
/*
 *
 *  * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *  *
 *  * This library is free software; you can redistribute it and/or modify it under
 *  * the terms of the GNU Lesser General Public License as published by the Free
 *  * Software Foundation; either version 2.1 of the License, or (at your option)
 *  * any later version.
 *  *
 *  * This library is distributed in the hope that it will be useful, but WITHOUT
 *  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  * details.
 *
 */

package org.entando.entando.plugins.dashboard.web.dashboardconfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementColumn;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DatasourcesConfigRequest;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.validator.DashboardConfigValidator;
import org.entando.entando.plugins.dashboard.web.iot.IoTExceptionHandler;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.ErrorRestResponse;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(value = "/plugins/dashboard/dashboardConfigs")
@Profile("!mock")
public class DashboardConfigController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private IDashboardConfigService dashboardConfigService;

  @Autowired
  private DashboardConfigValidator dashboardConfigValidator;

  @Autowired
  private IConnectorService connectorService;
  
  protected IDashboardConfigService getDashboardConfigService() {
    return dashboardConfigService;
  }

  public void setDashboardConfigService(IDashboardConfigService dashboardConfigService) {
    this.dashboardConfigService = dashboardConfigService;
  }

  protected DashboardConfigValidator getDashboardConfigValidator() {
    return dashboardConfigValidator;
  }

  public void setDashboardConfigValidator(DashboardConfigValidator dashboardConfigValidator) {
    this.dashboardConfigValidator = dashboardConfigValidator;
  }

  protected IConnectorService getConnectorService() {
    return connectorService;
  }

  public void setConnectorService(IConnectorService connectorService) {
    this.connectorService = connectorService;
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/servertypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<ServerType>> getDashboardTypes() {
//    ServerType sitewhere = new ServerType();
//    sitewhere.setCode("SITEWHERE");
//    sitewhere.setDescription("SITEWHERE");
//    ServerType kaa = new ServerType();
//    kaa.setCode("KAA");
//    kaa.setDescription("KAA");
//    List<ServerType> lista = new ArrayList<ServerType>();
//    lista.add(kaa);
//    lista.add(sitewhere);
    List<ServerType> lista = connectorService.getDashboardTypes();
    return new ResponseEntity<>(new SimpleRestResponse(lista), HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PagedRestResponse<PagedMetadata<DashboardConfigDto>>> getDashboardConfigs(
      RestListRequest requestList) throws JsonProcessingException {
    this.getDashboardConfigValidator()
        .validateRestListRequest(requestList, DashboardConfigDto.class);
    PagedMetadata<DashboardConfigDto> result = this.getDashboardConfigService()
        .getDashboardConfigs(requestList);
    this.getDashboardConfigValidator().validateRestListResult(requestList, result);
    logger.debug("Main Response -> {}", result);
    return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/{dashboardConfigId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<DashboardConfigDto>> getDashboardConfig(
      @PathVariable String dashboardConfigId) {
    DashboardConfigDto dashboardConfig = this.getDashboardConfigService()
        .getDashboardConfig(Integer.valueOf(dashboardConfigId));
    return new ResponseEntity<>(new SimpleRestResponse(dashboardConfig), HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/{dashboardConfigId}/datasources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<DatasourcesConfigDto>> getDashboardConfigDatasource(
      @PathVariable String dashboardConfigId) {
    DashboardConfigDto dashboardConfig = this.getDashboardConfigService()
        .getDashboardConfig(Integer.valueOf(dashboardConfigId));
    return new ResponseEntity<>(new SimpleRestResponse(dashboardConfig.getDatasources()),
        HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/{dashboardConfigId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<DashboardConfigDto>> updateDashboardConfig(
      @PathVariable String dashboardConfigId,
      @Valid @RequestBody DashboardConfigRequest dashboardConfigRequest,
      BindingResult bindingResult) throws ApsSystemException {
    // field validations
    if (bindingResult.hasErrors()) {
      throw new ValidationGenericException(bindingResult);
    }
    this.getDashboardConfigValidator()
        .validateBodyName(String.valueOf(dashboardConfigId), dashboardConfigRequest,
            bindingResult);
    if (bindingResult.hasErrors()) {
      throw new ValidationGenericException(bindingResult);
    }
    
    connectorService.setDevicesMetadata(dashboardConfigRequest);
    DashboardConfigDto dashboardConfig = this.getDashboardConfigService()
        .updateDashboardConfig(dashboardConfigRequest);
    return new ResponseEntity<>(new SimpleRestResponse(dashboardConfig), HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<DashboardConfigDto>> addDashboardConfig( 
      @RequestBody DashboardConfigRequest dashboardConfigRequest,
      BindingResult bindingResult) throws ApsSystemException {
    // field validations
    if (bindingResult.hasErrors()) {
      throw new ValidationGenericException(bindingResult);
    }
    // business validations
    getDashboardConfigValidator().validate(dashboardConfigRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      throw new ValidationConflictException(bindingResult);
    }
    dashboardConfigRequest = connectorService.setDevicesMetadata(dashboardConfigRequest);
    DashboardConfigDto dto = this.getDashboardConfigService()
        .addDashboardConfig(dashboardConfigRequest);
    return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/{dashboardConfigId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<Map>> deleteDashboardConfig(
      @PathVariable String dashboardConfigId) {
    logger.info("deleting {}", dashboardConfigId);
    this.getDashboardConfigService().removeDashboardConfig(Integer.valueOf(dashboardConfigId));
    Map<String, Integer> result = new HashMap<>();
    result.put("id", Integer.valueOf(dashboardConfigId));
    return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/server/{serverId}/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<Boolean>> pingDashboardConfig(
      @PathVariable int serverId) throws IOException {
    logger.debug("{} ping to {}", this.getClass().getSimpleName(), serverId);

    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardConfigDto dto = dashboardConfigService.getDashboardConfig(serverId);

    boolean pingResult = connectorService.pingServer(dto);
    return new ResponseEntity<>(new SimpleRestResponse<>(pingResult), HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/server/{serverId}/datasource/{datasourceCode}/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<Boolean>> pingDatasource(
      @PathVariable int serverId, @PathVariable String datasourceCode) throws IOException {
    logger.debug("{} ping to server, datasource : {}, {}", this.getClass().getSimpleName(),
        serverId, datasourceCode);

    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardDatasourceDto dto = dashboardConfigService
        .getDashboardDatasourceDto(serverId, datasourceCode);

    if (dto.getDatasourcesConfigDto() == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    boolean pingResult = connectorService.pingDevice(dto);
    return new ResponseEntity<>(new SimpleRestResponse<>(pingResult), HttpStatus.OK);
  }

  /**
   * ATTUALE
   *  "payload": [
   *        {
   *            "key": "temperature",
   *            "value": "temperature"
   *        },
   *        {
   *            "key": "timestamp",
   *            "value": "timestamp"
   *        }
   *    ],
   *    "metaData": {},
   *    "errors": []
   *    
   * @param serverId
   * @param datasourceCode
   * @return
   * @throws IOException
   */
  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/server/{serverId}/datasource/{datasourceCode}/preview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<MeasurementColumn>> getMeasurementPreview(
      @PathVariable int serverId, @PathVariable String datasourceCode) throws IOException {
    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardDatasourceDto dto = dashboardConfigService.getDashboardDatasourceDto(serverId, datasourceCode);
    if (dto.getDatasourcesConfigDto() == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    MeasurementTemplate template = connectorService
        .getDeviceMeasurementSchema(dto);
    return new ResponseEntity<>(new SimpleRestResponse(template), HttpStatus.OK);
  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/server/{serverId}/datasource/{datasourceCode}/columns", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<MeasurementColumn>> getDatasourceColumns(
      @PathVariable int serverId, @PathVariable String datasourceCode) throws IOException {
    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardDatasourceDto dto = dashboardConfigService.getDashboardDatasourceDto(serverId, datasourceCode);
    if (dto.getDatasourcesConfigDto() == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    MeasurementConfig config = connectorService.getMeasurementsConfig(dto);
    return new ResponseEntity<>(new SimpleRestResponse(config), HttpStatus.OK);
  }


  @RestAccessControl(permission = "superuser")
  @RequestMapping(value = "/server/{serverId}/getAllDevices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<MeasurementColumn>> getAllDevices(
      @PathVariable int serverId) throws ApiResourceNotAvailableException {
    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    DashboardConfigDto dashboard = dashboardConfigService.getDashboardConfig(serverId);
    List<DatasourcesConfigDto> config = connectorService.getAllDevices(dashboard);
    return new ResponseEntity<>(new SimpleRestResponse(config), HttpStatus.OK);
  }



}
