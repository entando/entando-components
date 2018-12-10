/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices;

import  org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.model.IotListDevicesDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.jpiot.web.iotlistdevices.model.IotListDevicesRequest;
import org.entando.entando.plugins.jpiot.web.iotlistdevices.validator.IotListDevicesValidator;

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

public class IotListDevicesService implements IIotListDevicesService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IIotListDevicesManager iotListDevicesManager;
    private IDtoBuilder<IotListDevices, IotListDevicesDto> dtoBuilder;


    protected IIotListDevicesManager getIotListDevicesManager() {
        return iotListDevicesManager;
    }

    public void setIotListDevicesManager(IIotListDevicesManager iotListDevicesManager) {
        this.iotListDevicesManager = iotListDevicesManager;
    }

    protected IDtoBuilder<IotListDevices, IotListDevicesDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<IotListDevices, IotListDevicesDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<IotListDevices, IotListDevicesDto>() {

            @Override
            protected IotListDevicesDto toDto(IotListDevices src) {
                IotListDevicesDto dto = new IotListDevicesDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<IotListDevicesDto> getIotListDevicess(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(IotListDevicesDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<IotListDevices> iotListDevicess = this.getIotListDevicesManager().getIotListDevicess(filters);
            List<IotListDevicesDto> dtoList = dtoBuilder.convert(iotListDevicess.getList());

            PagedMetadata<IotListDevicesDto> pagedMetadata = new PagedMetadata<>(requestList, iotListDevicess);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search iotListDevicess", t);
            throw new RestServerError("error in search iotListDevicess", t);
        }
    }

    @Override
    public IotListDevicesDto updateIotListDevices(IotListDevicesRequest iotListDevicesRequest) {
        try {
	        IotListDevices iotListDevices = this.getIotListDevicesManager().getIotListDevices(iotListDevicesRequest.getId());
	        if (null == iotListDevices) {
	            throw new RestRourceNotFoundException(IotListDevicesValidator.ERRCODE_IOTLISTDEVICES_NOT_FOUND, "iotListDevices", String.valueOf(iotListDevicesRequest.getId()));
	        }
        	BeanUtils.copyProperties(iotListDevicesRequest, iotListDevices);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(iotListDevices);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getIotListDevicesManager().updateIotListDevices(iotListDevices);
            return this.getDtoBuilder().convert(iotListDevices);
        } catch (ApsSystemException e) {
            logger.error("Error updating iotListDevices {}", iotListDevicesRequest.getId(), e);
            throw new RestServerError("error in update iotListDevices", e);
        }
    }

    @Override
    public IotListDevicesDto addIotListDevices(IotListDevicesRequest iotListDevicesRequest) {
        try {
            IotListDevices iotListDevices = this.createIotListDevices(iotListDevicesRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(iotListDevices);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getIotListDevicesManager().addIotListDevices(iotListDevices);
            IotListDevicesDto dto = this.getDtoBuilder().convert(iotListDevices);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a iotListDevices", e);
            throw new RestServerError("error in add iotListDevices", e);
        }
    }

    @Override
    public void removeIotListDevices(int  id) {
        try {
            IotListDevices iotListDevices = this.getIotListDevicesManager().getIotListDevices(id);
            if (null == iotListDevices) {
                logger.info("iotListDevices {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(iotListDevices);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getIotListDevicesManager().deleteIotListDevices(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete iotListDevices {}", id, e);
            throw new RestServerError("error in delete iotListDevices", e);
        }
    }

    @Override
    public IotListDevicesDto getIotListDevices(int  id) {
        try {
	        IotListDevices iotListDevices = this.getIotListDevicesManager().getIotListDevices(id);
	        if (null == iotListDevices) {
	            logger.warn("no iotListDevices found with code {}", id);
	            throw new RestRourceNotFoundException(IotListDevicesValidator.ERRCODE_IOTLISTDEVICES_NOT_FOUND, "iotListDevices", String.valueOf(id));
	        }
	        IotListDevicesDto dto = this.getDtoBuilder().convert(iotListDevices);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading iotListDevices {}", id, e);
            throw new RestServerError("error in loading iotListDevices", e);
        }
    }

    private IotListDevices createIotListDevices(IotListDevicesRequest iotListDevicesRequest) {
        IotListDevices iotListDevices = new IotListDevices();
        BeanUtils.copyProperties(iotListDevicesRequest, iotListDevices);
        return iotListDevices;
    }


    protected BeanPropertyBindingResult validateForAdd(IotListDevices iotListDevices) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(iotListDevices, "iotListDevices");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(IotListDevices iotListDevices) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(iotListDevices, "iotListDevices");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(IotListDevices iotListDevices) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(iotListDevices, "iotListDevices");
        return errors;
    }

}

