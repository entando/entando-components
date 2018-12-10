/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig;

import  org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.model.IotConfigDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.jpiot.web.iotconfig.model.IotConfigRequest;
import org.entando.entando.plugins.jpiot.web.iotconfig.validator.IotConfigValidator;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class IotConfigService implements IIotConfigService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IIotConfigManager iotConfigManager;
    private IDtoBuilder<IotConfig, IotConfigDto> dtoBuilder;


    protected IIotConfigManager getIotConfigManager() {
        return iotConfigManager;
    }

    public void setIotConfigManager(IIotConfigManager iotConfigManager) {
        this.iotConfigManager = iotConfigManager;
    }

    protected IDtoBuilder<IotConfig, IotConfigDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<IotConfig, IotConfigDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<IotConfig, IotConfigDto>() {

            @Override
            protected IotConfigDto toDto(IotConfig src) {
                IotConfigDto dto = new IotConfigDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<IotConfigDto> getIotConfigs(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(IotConfigDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<IotConfig> iotConfigs = this.getIotConfigManager().getIotConfigs(filters);
            List<IotConfigDto> dtoList = dtoBuilder.convert(iotConfigs.getList());

            PagedMetadata<IotConfigDto> pagedMetadata = new PagedMetadata<>(requestList, iotConfigs);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search iotConfigs", t);
            throw new RestServerError("error in search iotConfigs", t);
        }
    }

    @Override
    public IotConfigDto updateIotConfig(IotConfigRequest iotConfigRequest) {
        try {
	        IotConfig iotConfig = this.getIotConfigManager().getIotConfig(iotConfigRequest.getId());
	        if (null == iotConfig) {
	            throw new RestRourceNotFoundException(IotConfigValidator.ERRCODE_IOTCONFIG_NOT_FOUND, "iotConfig", String.valueOf(iotConfigRequest.getId()));
	        }
        	BeanUtils.copyProperties(iotConfigRequest, iotConfig);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(iotConfig);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getIotConfigManager().updateIotConfig(iotConfig);
            return this.getDtoBuilder().convert(iotConfig);
        } catch (ApsSystemException e) {
            logger.error("Error updating iotConfig {}", iotConfigRequest.getId(), e);
            throw new RestServerError("error in update iotConfig", e);
        }
    }

    @Override
    public IotConfigDto addIotConfig(IotConfigRequest iotConfigRequest) {
        try {
            IotConfig iotConfig = this.createIotConfig(iotConfigRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(iotConfig);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getIotConfigManager().addIotConfig(iotConfig);
            IotConfigDto dto = this.getDtoBuilder().convert(iotConfig);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a iotConfig", e);
            throw new RestServerError("error in add iotConfig", e);
        }
    }

    @Override
    public void removeIotConfig(int  id) {
        try {
            IotConfig iotConfig = this.getIotConfigManager().getIotConfig(id);
            if (null == iotConfig) {
                logger.info("iotConfig {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(iotConfig);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getIotConfigManager().deleteIotConfig(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete iotConfig {}", id, e);
            throw new RestServerError("error in delete iotConfig", e);
        }
    }

    @Override
    public IotConfigDto getIotConfig(int  id) {
        try {
	        IotConfig iotConfig = this.getIotConfigManager().getIotConfig(id);
	        if (null == iotConfig) {
	            logger.warn("no iotConfig found with code {}", id);
	            throw new RestRourceNotFoundException(IotConfigValidator.ERRCODE_IOTCONFIG_NOT_FOUND, "iotConfig", String.valueOf(id));
	        }
	        IotConfigDto dto = this.getDtoBuilder().convert(iotConfig);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading iotConfig {}", id, e);
            throw new RestServerError("error in loading iotConfig", e);
        }
    }

    private IotConfig createIotConfig(IotConfigRequest iotConfigRequest) {
        IotConfig iotConfig = new IotConfig();
        BeanUtils.copyProperties(iotConfigRequest, iotConfig);
        return iotConfig;
    }


    protected BeanPropertyBindingResult validateForAdd(IotConfig iotConfig) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(iotConfig, "iotConfig");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(IotConfig iotConfig) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(iotConfig, "iotConfig");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(IotConfig iotConfig) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(iotConfig, "iotConfig");
        return errors;
    }

}

