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

package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigBuilder;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DatasourcesConfigRequest;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIGS_ERROR_SEARCHING;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_DELETING_S;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_LOADING__WITH_ID_S;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_ON_INSERT;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DASHBOARD_CONFIG_ERROR_UPDATING_S;
import static org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigExceptionMessages.DATASOURCE_ALREADY_EXISTS_FIELD_WITH_NAME_S;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.logEndMethod;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.logStartMethod;
import static org.entando.entando.plugins.dashboard.web.dashboardconfig.validator.DashboardConfigValidator.ERRCODE_DASHBOARDCONFIG_NOT_FOUND;

public class DashboardConfigService implements IDashboardConfigService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private IDashboardConfigManager dashboardConfigManager;
  private IDtoBuilder<DashboardConfig, DashboardConfigDto> dtoBuilder;


  protected IDashboardConfigManager getDashboardConfigManager() {
    return dashboardConfigManager;
  }

  public void setDashboardConfigManager(IDashboardConfigManager dashboardConfigManager) {
    this.dashboardConfigManager = dashboardConfigManager;
  }

  protected IDtoBuilder<DashboardConfig, DashboardConfigDto> getDtoBuilder() {
    return dtoBuilder;
  }

  public void setDtoBuilder(IDtoBuilder<DashboardConfig, DashboardConfigDto> dtoBuilder) {
    this.dtoBuilder = dtoBuilder;
  }

  @PostConstruct
  public void onInit() {
    this.setDtoBuilder(new DtoBuilder<DashboardConfig, DashboardConfigDto>() {

      @Override
      protected DashboardConfigDto toDto(DashboardConfig src) {
        DashboardConfigDto dto = new DashboardConfigDto();
        BeanUtils.copyProperties(src, dto);
        return dto;
      }
    });
  }

  @Override
  public PagedMetadata<DashboardConfigDto> getDashboardConfigs(RestListRequest requestList) {
    logStartMethod(getClass());
    try {
      List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(
          requestList.buildFieldSearchFilters());
      filters
          .stream()
          .filter(i -> i.getKey() != null)
          .forEach(i -> i.setKey(DashboardConfigDto.getEntityFieldName(i.getKey())));

      SearcherDaoPaginatedResult<DashboardConfig> dashboardConfigs = this
          .getDashboardConfigManager().getDashboardConfigs(filters);
      List<DashboardConfigDto> dtoList = dtoBuilder.convert(dashboardConfigs.getList());

      PagedMetadata<DashboardConfigDto> pagedMetadata = new PagedMetadata<>(requestList,
          dashboardConfigs);
      pagedMetadata.setBody(dtoList);
      logEndMethod(true, getClass());
      return pagedMetadata;
    } catch (Throwable t) {
      logEndMethod(false, getClass());
      logger.error(DASHBOARD_CONFIGS_ERROR_SEARCHING, t);
      throw new RestServerError(DASHBOARD_CONFIGS_ERROR_SEARCHING, t);
    }
  }

  @Override
  public DashboardConfigDto updateDashboardConfig(DashboardConfigRequest dashboardConfigRequest) {
    logStartMethod(dashboardConfigRequest.getId(), null, getClass());
    try {
      DashboardConfig dashboardConfig = this.getDashboardConfigManager()
          .getDashboardConfig(dashboardConfigRequest.getId());

      BeanPropertyBindingResult validationResult = this
          .validateForUpdate(dashboardConfigRequest, dashboardConfig);
      if (validationResult.hasErrors()) {
        throw new ValidationGenericException(validationResult);
      }

      dashboardConfig = DashboardConfigBuilder.fromRequestToEntity(dashboardConfigRequest);
      this.getDashboardConfigManager().updateDashboardConfig(dashboardConfig);
      logEndMethod(dashboardConfig.getId(), null, true, getClass());
      return this.getDtoBuilder().convert(dashboardConfig);
    } catch (ApsSystemException e) {
      logEndMethod(dashboardConfigRequest.getId(), null, false, getClass());
      logger.error(DASHBOARD_CONFIG_ERROR_UPDATING_S, dashboardConfigRequest.getId(), e);
      throw new RestServerError(DASHBOARD_CONFIG_ERROR_UPDATING_S, e);
    }

  }

  @Override
  public DashboardConfigDto addDashboardConfig(DashboardConfigRequest dashboardConfigRequest) {
    logStartMethod(getClass());
    try {
      DashboardConfig dashboardConfig = this.createDashboardConfig(dashboardConfigRequest);
      BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardConfig);
      if (validationResult.hasErrors()) {
        throw new ValidationGenericException(validationResult);
      }
      this.getDashboardConfigManager().addDashboardConfig(dashboardConfig);
      DashboardConfigDto dto = this.getDtoBuilder().convert(dashboardConfig);
      logEndMethod(true, getClass());
      return dto;
    } catch (ApsSystemException e) {
      logEndMethod(false, getClass());
      logger.error(DASHBOARD_CONFIG_ERROR_ON_INSERT, e);
      throw new RestServerError(DASHBOARD_CONFIG_ERROR_ON_INSERT, e);
    }
  }

  @Override
  public void removeDashboardConfig(int id) {
    logStartMethod(id, null, getClass());
    try {
      DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(id);
      if (null == dashboardConfig) {
        logger.info("dashboardConfig {} does not exists", id);
        return;
      }
      BeanPropertyBindingResult validationResult = this.validateForDelete(dashboardConfig);
      if (validationResult.hasErrors()) {
        throw new ValidationGenericException(validationResult);
      }
      this.getDashboardConfigManager().deleteDashboardConfig(id);
      logEndMethod(id, null, true, getClass());
    } catch (ApsSystemException e) {
      logEndMethod(id, null, false, getClass());
      logger.error(String.format(DASHBOARD_CONFIG_ERROR_DELETING_S, id), e);
      throw new RestServerError(String.format(DASHBOARD_CONFIG_ERROR_DELETING_S, id), e);
    }
  }

  @Override
  public DashboardConfigDto getDashboardConfig(int id) {
    logStartMethod(id, null, getClass());
    try {
      DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(id);
      if (null == dashboardConfig) {
        logger.warn(String.format(
            DashboardConfigExceptionMessages.DASHBOARD_CONFIG_NOT_FOUND_WITH_CODE_S, id));
        throw new ResourceNotFoundException(
            ERRCODE_DASHBOARDCONFIG_NOT_FOUND, "dashboardConfig",
            String.valueOf(id));
      }
      DashboardConfigDto dto = this.getDtoBuilder().convert(dashboardConfig);
      logEndMethod(id, null, true, getClass());
      return dto;
    } catch (ApsSystemException e) {
      logEndMethod(id, null, false, getClass());
      logger.error(String.format(DASHBOARD_CONFIG_ERROR_LOADING__WITH_ID_S, id), e);
      throw new RestServerError(String.format(DASHBOARD_CONFIG_ERROR_LOADING__WITH_ID_S, id), e);
    }
  }

  @Override
  public boolean existsById(int id) {
    logStartMethod(id, null, getClass());
    boolean res = this.getDashboardConfigManager().existsById(id);
    logEndMethod(id, null, res, getClass());
    return res;
  }

  @Override
  public boolean existsByIdAndIsActive(int id) {
    logStartMethod(id, null, getClass());
    boolean res = this.getDashboardConfigManager().existsByIdAndActive(id, 1);
    logEndMethod(id, null, res, getClass());
    return res;
  }

  @Override
  public boolean datasourceExistsByDatasourceName(String datasourceName) {
    logStartMethod(null, datasourceName, getClass());
    boolean res = this.getDashboardConfigManager().datasourceExistsByDatasourceName(datasourceName);
    logEndMethod(null, datasourceName, res, getClass());
    return res;
  }

  @Override
  public boolean datasourceExistsByDashboardIdAndDatasourceName(int dashboardId,
      String datasourceName) {
    logStartMethod(dashboardId, datasourceName, getClass());
    boolean res = this.getDashboardConfigManager()
        .datasourceExistsByDashboardIdAndDatasourceName(dashboardId, datasourceName);
    logEndMethod(dashboardId, datasourceName, res, getClass());
    return res;
  }

  @Override
  public boolean datasourceExistsByDatasourceCode(String datasourceCode) {
    logStartMethod(null, datasourceCode, getClass());
    boolean res = this.getDashboardConfigManager().datasourceExistsByDatasourceCode(datasourceCode);
    logEndMethod(null, datasourceCode, res, getClass());
    return res;
  }

  @Override
  public void updateDatasource(DatasourcesConfigDto datasource) {
    logStartMethod(null, datasource.getDatasourceCode(), getClass());
    this.getDashboardConfigManager().updateDatasource(datasource);
    logEndMethod(null, datasource.getDatasourceCode(), true, getClass());
  }

  @Override
  public DatasourcesConfigDto getDatasourceById(String datasource) {
    logStartMethod(null, datasource, getClass());
    DatasourcesConfigDto dto = this.getDashboardConfigManager().getDatasourceByDatasourceId(
        datasource);
    logEndMethod(null, datasource, true, getClass());
    return dto;
  }


  private DashboardConfig createDashboardConfig(DashboardConfigRequest dashboardConfigRequest) {
    DashboardConfig dashboardConfig = new DashboardConfig();
    BeanUtils.copyProperties(dashboardConfigRequest, dashboardConfig);
    List<DatasourcesConfigDto> datasources = convertDatasourceRequestToDto(dashboardConfigRequest);
    dashboardConfig.setDatasources(datasources);
    return dashboardConfig;
  }

  private List<DatasourcesConfigDto> convertDatasourceRequestToDto(
      final DashboardConfigRequest dashboardConfigRequest) {
    List<DatasourcesConfigDto> datasources = new ArrayList<>();
    if (dashboardConfigRequest.getDatasources() != null) {
      for (DatasourcesConfigRequest datasource : dashboardConfigRequest.getDatasources()) {
        DatasourcesConfigDto ds = new DatasourcesConfigDto();
        ds.setDatasource(datasource.getDatasource());
        ds.setDatasourceCode(datasource.getDatasourceCode());
        ds.setDatasourceURI(datasource.getDatasourceURI());
        ds.setStatus(datasource.getStatus());
        ds.setMetadata(datasource.getMetadata());
        datasources.add(ds);
      }
    }
    return datasources;
  }

  protected BeanPropertyBindingResult validateForAdd(DashboardConfig dashboardConfig) {
    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardConfig,
        "dashboardConfig");
    if (dashboardConfig.getDatasources() != null
        && dashboardConfig.getDatasources().size() > 0) {
      dashboardConfig.getDatasources().forEach(d -> {
        if (this.datasourceExistsByDatasourceName(d.getDatasourceCode())) {
          errors.addError(new ObjectError("dashboardConfig.datasources", String.format(
              DATASOURCE_ALREADY_EXISTS_FIELD_WITH_NAME_S, d.getDatasource())));
        }
      });
    }
    return errors;
  }

  protected BeanPropertyBindingResult validateForDelete(DashboardConfig dashboardConfig) {
    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardConfig,
        "dashboardConfig");
    return errors;
  }

  protected BeanPropertyBindingResult validateForUpdate(DashboardConfigRequest request,
      DashboardConfig fromDB) {
    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(fromDB,
        "dashboardConfig");

    if (null == fromDB) {
      errors.addError(new ObjectError("dashboardConfig.null", DASHBOARD_CONFIGS_ERROR_SEARCHING));
      return errors;
    }

    if (request.getDatasources() != null && request.getDatasources().size() > 0) {
      request.getDatasources().forEach(d -> {
        DatasourcesConfigDto matchedByName = dashboardConfigManager
            .getDatasourceByDatasourceName(d.getDatasource());
        if (matchedByName != null && !matchedByName.equals(new DatasourcesConfigDto())) {
          if (matchedByName.getFk_dashboard_config() != fromDB.getId()) {
            errors.addError(new ObjectError("dashboardConfig.datasources", String.format(
                DATASOURCE_ALREADY_EXISTS_FIELD_WITH_NAME_S, d.getDatasource())));
          } else {
            if (request.getDatasources().stream()
                .filter(data -> matchedByName.getDatasource().equals(data.getDatasource())).count()
                > 1) {
              errors.addError(new ObjectError("dashboardConfig.datasources", String.format(
                  DATASOURCE_ALREADY_EXISTS_FIELD_WITH_NAME_S, d.getDatasource())));
            }
          }
        }
      });
    }
    return errors;
  }

}

