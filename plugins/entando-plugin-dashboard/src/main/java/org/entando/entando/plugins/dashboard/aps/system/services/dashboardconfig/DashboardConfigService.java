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
import org.entando.entando.plugins.dashboard.web.dashboardconfig.validator.DashboardConfigValidator;
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

      return pagedMetadata;
    } catch (Throwable t) {
      logger.error(DASHBOARD_CONFIGS_ERROR_SEARCHING, t);
      throw new RestServerError(DASHBOARD_CONFIGS_ERROR_SEARCHING, t);
    }
  }

  @Override
  public DashboardConfigDto updateDashboardConfig(DashboardConfigRequest dashboardConfigRequest) {
    try {
      DashboardConfig dashboardConfig = this.getDashboardConfigManager()
          .getDashboardConfig(dashboardConfigRequest.getId());
      
      if (null == dashboardConfig) {
        throw new ResourceNotFoundException(
            DashboardConfigValidator.ERRCODE_DASHBOARDCONFIG_NOT_FOUND, "dashboardConfig",
            String.valueOf(dashboardConfigRequest.getId()));
      }

      dashboardConfig = DashboardConfigBuilder.fromRequestToEntity(dashboardConfigRequest);

      BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardConfig);
      if (validationResult.hasErrors()) {
        throw new ValidationGenericException(validationResult);
      }
      this.getDashboardConfigManager().updateDashboardConfig(dashboardConfig);
      return this.getDtoBuilder().convert(dashboardConfig);
    }
   catch(ApsSystemException e)
  {
    logger.error(DASHBOARD_CONFIG_ERROR_UPDATING_S, dashboardConfigRequest.getId(), e);
    throw new RestServerError(DASHBOARD_CONFIG_ERROR_UPDATING_S, e);
  }

}

  @Override
  public DashboardConfigDto addDashboardConfig(DashboardConfigRequest dashboardConfigRequest) {
    try {
      DashboardConfig dashboardConfig = this.createDashboardConfig(dashboardConfigRequest);
      BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardConfig);
      if (validationResult.hasErrors()) {
        throw new ValidationGenericException(validationResult);
      }
      this.getDashboardConfigManager().addDashboardConfig(dashboardConfig);
      DashboardConfigDto dto = this.getDtoBuilder().convert(dashboardConfig);
      return dto;
    } catch (ApsSystemException e) {
      logger.error(DASHBOARD_CONFIG_ERROR_ON_INSERT, e);
      throw new RestServerError(DASHBOARD_CONFIG_ERROR_ON_INSERT, e);
    }
  }

  @Override
  public void removeDashboardConfig(int id) {
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
    } catch (ApsSystemException e) {
      logger.error(String.format(DASHBOARD_CONFIG_ERROR_DELETING_S, id), e);
      throw new RestServerError(String.format(DASHBOARD_CONFIG_ERROR_DELETING_S, id), e);
    }
  }

  @Override
  public DashboardConfigDto getDashboardConfig(int id) {
    try {
      DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(id);
      if (null == dashboardConfig) {
        logger.warn(String.format(
            DashboardConfigExceptionMessages.DASHBOARD_CONFIG_NOT_FOUND_WITH_CODE_S, id));
        throw new ResourceNotFoundException(
            DashboardConfigValidator.ERRCODE_DASHBOARDCONFIG_NOT_FOUND, "dashboardConfig",
            String.valueOf(id));
      }
      DashboardConfigDto dto = this.getDtoBuilder().convert(dashboardConfig);
      return dto;
    } catch (ApsSystemException e) {
      logger.error(String.format(DASHBOARD_CONFIG_ERROR_LOADING__WITH_ID_S, id), e);
      throw new RestServerError(String.format(DASHBOARD_CONFIG_ERROR_LOADING__WITH_ID_S, id), e);
    }
  }

  @Override
  public boolean existsById(int id) {
    return this.getDashboardConfigManager().existsById(id);
  }

  @Override
  public boolean existsByIdAndIsActive(int id) {
    return this.getDashboardConfigManager().existsByIdAndActive(id, 1);
  }

  @Override
  public boolean datasourceExistsByDatasourceName(String datasourceName) {
    return this.getDashboardConfigManager().datasourceExistsByDatasourceName(datasourceName);
  }

  @Override
  public boolean datasourceExistsByDashboardIdAndDatasourceName(int dashboardId,
      String datasourceName) {
    return this.getDashboardConfigManager()
        .datasourceExistsByDashboardIdAndDatasourceName(dashboardId, datasourceName);
  }

  @Override
  public boolean datasourceExistsByDatasourceCode(String datasourceCode) {
    return this.getDashboardConfigManager().datasourceExistsByDatasourceCode(datasourceCode);
  }

  @Override
  public void updateDatasource(DatasourcesConfigDto datasource) {
    this.getDashboardConfigManager().updateDatasource(datasource);
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
          errors.addError(new ObjectError("dashboardConfig.datasources",String.format(
              DashboardConfigExceptionMessages.DATASOURCE_ALREADY_EXISTS_FIELD_WITH_NAME_S, d.getDatasource())));
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

  protected BeanPropertyBindingResult validateForUpdate(DashboardConfig dashboardConfig) {
    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardConfig,
        "dashboardConfig");
    if (dashboardConfig.getDatasources() != null && dashboardConfig.getDatasources().size() > 0) {
      dashboardConfig.getDatasources().forEach(d -> {
        DatasourcesConfigDto matchedByName = dashboardConfigManager
            .getDatasourceByDatasourceName(d.getDatasource());
        if (matchedByName != null && !matchedByName.equals(new DatasourcesConfigDto())) {
          if (matchedByName.getFk_dashboard_config() != dashboardConfig.getId()) {
             errors.addError(new ObjectError("dashboardConfig.datasources",String.format(
                 DashboardConfigExceptionMessages.DATASOURCE_ALREADY_EXISTS_FIELD_WITH_NAME_S, d.getDatasource())));
          }
          else {
            if(dashboardConfig.getDatasources().stream().filter(data -> matchedByName.getDatasource().equals(data.getDatasource())).count() > 1) {
              errors.addError(new ObjectError("dashboardConfig.datasources",String.format(
                  DashboardConfigExceptionMessages.DATASOURCE_ALREADY_EXISTS_FIELD_WITH_NAME_S, d.getDatasource())));
            }
          }
        }
      });
    }
    return errors;
  }

}

