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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.validator.DashboardConfigValidator;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;

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
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(DashboardConfigDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<DashboardConfig> dashboardConfigs = this.getDashboardConfigManager().getDashboardConfigs(filters);
            List<DashboardConfigDto> dtoList = dtoBuilder.convert(dashboardConfigs.getList());

            PagedMetadata<DashboardConfigDto> pagedMetadata = new PagedMetadata<>(requestList, dashboardConfigs);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search dashboardConfigs", t);
            throw new RestServerError("error in search dashboardConfigs", t);
        }
    }

    @Override
    public DashboardConfigDto updateDashboardConfig(DashboardConfigRequest dashboardConfigRequest) {
        try {


	        DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(dashboardConfigRequest.getId());
	        if (null == dashboardConfig) {
	            throw new ResourceNotFoundException(DashboardConfigValidator.ERRCODE_DASHBOARDCONFIG_NOT_FOUND, "dashboardConfig", String.valueOf(dashboardConfigRequest.getId()));
	        }
        	BeanUtils.copyProperties(dashboardConfigRequest, dashboardConfig);

            List<DatasourcesConfigDto> datasources = convertDatasourceRequestToDto(dashboardConfigRequest);
            dashboardConfig.setDatasources(datasources);


            BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardConfig);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardConfigManager().updateDashboardConfig(dashboardConfig);
            return this.getDtoBuilder().convert(dashboardConfig);
        } catch (ApsSystemException e) {
            logger.error("Error updating dashboardConfig {}", dashboardConfigRequest.getId(), e);
            throw new RestServerError("error in update dashboardConfig", e);
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
            logger.error("Error adding a dashboardConfig", e);
            throw new RestServerError("error in add dashboardConfig", e);
        }
    }

    @Override
    public void removeDashboardConfig(int  id) {
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
            logger.error("Error in delete dashboardConfig {}", id, e);
            throw new RestServerError("error in delete dashboardConfig", e);
        }
    }

    @Override
    public DashboardConfigDto getDashboardConfig(int  id) {
        try {
	        DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(id);
	        if (null == dashboardConfig) {
	            logger.warn("no dashboardConfig found with code {}", id);
	            throw new ResourceNotFoundException(DashboardConfigValidator.ERRCODE_DASHBOARDCONFIG_NOT_FOUND, "dashboardConfig", String.valueOf(id));
	        }
	        DashboardConfigDto dto = this.getDtoBuilder().convert(dashboardConfig);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardConfig {}", id, e);
            throw new RestServerError("error in loading dashboardConfig", e);
        }
    }

// TODO MARCO 
    
    
    @Override
    public  DatasourcesConfigDto getDashboardDataSourceDtobyIdAndCode(
        int dashboardId, String datasourceCode) {
        try {
            DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(dashboardId);
            if (null == dashboardConfig) {
                logger.warn("no dashboardConfig found with code {}", dashboardId);
                throw new ResourceNotFoundException(DashboardConfigValidator.ERRCODE_DASHBOARDCONFIG_NOT_FOUND, "dashboardConfig", String.valueOf(dashboardId));
            }
            
            // DashboardConfigDto dashboardConfigDto = this.getDtoBuilder().convert(dashboardConfig);
            
            return (DatasourcesConfigDto) this.getDashboardConfigManager().getDatasourceByDatasourcecodeAndDashboard(datasourceCode, dashboardId);
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardConfig {}", dashboardId, e);
            throw new RestServerError("error in loading dashboardConfig", e);
        }
    }
    
//    
//    @Override
//    public <T extends AbstractDashboardDatasourceDto> T getDashboardDataSourceDtobyIdAndCode(
//        int dashboardId, String datasourceCode) {
//        try {
//            DashboardConfig dashboardConfig = this.getDashboardConfigManager().getDashboardConfig(dashboardId);
//            if (null == dashboardConfig) {
//                logger.warn("no dashboardConfig found with code {}", dashboardId);
//                throw new ResourceNotFoundException(DashboardConfigValidator.ERRCODE_DASHBOARDCONFIG_NOT_FOUND, "dashboardConfig", String.valueOf(dashboardId));
//            }
//            DashboardConfigDto dashboardConfigDto = this.getDtoBuilder().convert(dashboardConfig);
//            switch(dashboardConfigDto.getServerDescription()) {
//                case KAA_SERVER_TYPE: {
//                    DashboardKaaDatasourceDto dashboardKaaDatasourceDto = new DashboardKaaDatasourceDto();
//                    dashboardKaaDatasourceDto.setDashboardConfigDto(dashboardConfigDto);
//                    dashboardKaaDatasourceDto.setKaaDatasourceConfigDto(
//                        (KaaApplicationConfigDto) this.getDashboardConfigManager().getDatasourceByDatasourcecodeAndDashboard(datasourceCode, dashboardId)); //TODO verificare lettura
//                    return (T) dashboardKaaDatasourceDto;
//                }
//                case SITEWHERE_SERVER_TYPE: {
//                    DashboardSitewhereDatasourceDto dashboardSitewhereDatasourceDto = new DashboardSitewhereDatasourceDto();
//                    dashboardSitewhereDatasourceDto.setDashboardConfigDto(dashboardConfigDto);
//                    dashboardSitewhereDatasourceDto.setSitewhereDatasourceConfigDto(
//                        (SitewhereApplicationConfigDto) this.getDashboardConfigManager().getDatasourceByDatasourcecodeAndDashboard(datasourceCode, dashboardId)); //TODO verificare lettura
//                    return (T) dashboardSitewhereDatasourceDto;
//                }
//            }
//        } catch (ApsSystemException e) {
//            logger.error("Error loading dashboardConfig {}", dashboardId, e);
//            throw new RestServerError("error in loading dashboardConfig", e);
//        }
//        
//        return null;
//    }

    private DashboardConfig createDashboardConfig(DashboardConfigRequest dashboardConfigRequest) {
        DashboardConfig dashboardConfig = new DashboardConfig();
        BeanUtils.copyProperties(dashboardConfigRequest, dashboardConfig);
        List<DatasourcesConfigDto> datasources = convertDatasourceRequestToDto(dashboardConfigRequest);
        dashboardConfig.setDatasources(datasources);
        return dashboardConfig;
    }

    private  List<DatasourcesConfigDto> convertDatasourceRequestToDto (final DashboardConfigRequest dashboardConfigRequest) {
        List<DatasourcesConfigDto> datasources = new ArrayList<>();
        dashboardConfigRequest.getDatasources().forEach(c->{
            final DatasourcesConfigDto ds = new DatasourcesConfigDto();
            ds.setDatasource(c.getDatasource());
            ds.setDatasourceURI(c.getDatasourceURI());
            ds.setStatus(c.getStatus());
            datasources.add(ds);
        });
        return datasources;
    }

    protected BeanPropertyBindingResult validateForAdd(DashboardConfig dashboardConfig) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardConfig, "dashboardConfig");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(DashboardConfig dashboardConfig) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardConfig, "dashboardConfig");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(DashboardConfig dashboardConfig) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardConfig, "dashboardConfig");
        return errors;
    }

}

