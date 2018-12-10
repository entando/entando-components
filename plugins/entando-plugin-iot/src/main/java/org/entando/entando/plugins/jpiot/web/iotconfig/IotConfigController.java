/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.web.iotconfig;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IIotConfigService;
import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.model.IotConfigDto;
import org.entando.entando.plugins.jpiot.web.iotconfig.model.IotConfigRequest;
import org.entando.entando.plugins.jpiot.web.iotconfig.validator.IotConfigValidator;

@RestController
@RequestMapping(value = "/jpiot/iotConfigs")
public class IotConfigController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IIotConfigService iotConfigService;

    @Autowired
    private IotConfigValidator iotConfigValidator;

    protected IIotConfigService getIotConfigService() {
        return iotConfigService;
    }

    public void setIotConfigService(IIotConfigService iotConfigService) {
        this.iotConfigService = iotConfigService;
    }

    protected IotConfigValidator getIotConfigValidator() {
        return iotConfigValidator;
    }

    public void setIotConfigValidator(IotConfigValidator iotConfigValidator) {
        this.iotConfigValidator = iotConfigValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<IotConfigDto>> > getIotConfigs(RestListRequest requestList) throws JsonProcessingException {
        this.getIotConfigValidator().validateRestListRequest(requestList, IotConfigDto.class);
        PagedMetadata<IotConfigDto> result = this.getIotConfigService().getIotConfigs(requestList);
        this.getIotConfigValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{iotConfigId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<IotConfigDto>> getIotConfig(@PathVariable String iotConfigId) {
		IotConfigDto iotConfig = this.getIotConfigService().getIotConfig(Integer.valueOf(iotConfigId));
        return new ResponseEntity<>(new SimpleRestResponse(iotConfig), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{iotConfigId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<IotConfigDto>> updateIotConfig(@PathVariable String iotConfigId, @Valid @RequestBody IotConfigRequest iotConfigRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getIotConfigValidator().validateBodyName(String.valueOf(iotConfigId), iotConfigRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        IotConfigDto iotConfig = this.getIotConfigService().updateIotConfig(iotConfigRequest);
        return new ResponseEntity<>(new SimpleRestResponse(iotConfig), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<IotConfigDto>> addIotConfig(@Valid @RequestBody IotConfigRequest iotConfigRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getIotConfigValidator().validate(iotConfigRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        IotConfigDto dto = this.getIotConfigService().addIotConfig(iotConfigRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{iotConfigId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteIotConfig(@PathVariable String iotConfigId) {
        logger.info("deleting {}", iotConfigId);
        this.getIotConfigService().removeIotConfig(Integer.valueOf(iotConfigId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(iotConfigId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

