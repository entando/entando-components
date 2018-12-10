/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.web.iotlistdevices;

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

import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IIotListDevicesService;
import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.model.IotListDevicesDto;
import org.entando.entando.plugins.jpiot.web.iotlistdevices.model.IotListDevicesRequest;
import org.entando.entando.plugins.jpiot.web.iotlistdevices.validator.IotListDevicesValidator;

@RestController
@RequestMapping(value = "/jpiot/iotListDevicess")
public class IotListDevicesController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IIotListDevicesService iotListDevicesService;

    @Autowired
    private IotListDevicesValidator iotListDevicesValidator;

    protected IIotListDevicesService getIotListDevicesService() {
        return iotListDevicesService;
    }

    public void setIotListDevicesService(IIotListDevicesService iotListDevicesService) {
        this.iotListDevicesService = iotListDevicesService;
    }

    protected IotListDevicesValidator getIotListDevicesValidator() {
        return iotListDevicesValidator;
    }

    public void setIotListDevicesValidator(IotListDevicesValidator iotListDevicesValidator) {
        this.iotListDevicesValidator = iotListDevicesValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<IotListDevicesDto>> > getIotListDevicess(RestListRequest requestList) throws JsonProcessingException {
        this.getIotListDevicesValidator().validateRestListRequest(requestList, IotListDevicesDto.class);
        PagedMetadata<IotListDevicesDto> result = this.getIotListDevicesService().getIotListDevicess(requestList);
        this.getIotListDevicesValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{iotListDevicesId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<IotListDevicesDto>> getIotListDevices(@PathVariable String iotListDevicesId) {
		IotListDevicesDto iotListDevices = this.getIotListDevicesService().getIotListDevices(Integer.valueOf(iotListDevicesId));
        return new ResponseEntity<>(new SimpleRestResponse(iotListDevices), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{iotListDevicesId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<IotListDevicesDto>> updateIotListDevices(@PathVariable String iotListDevicesId, @Valid @RequestBody IotListDevicesRequest iotListDevicesRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getIotListDevicesValidator().validateBodyName(String.valueOf(iotListDevicesId), iotListDevicesRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        IotListDevicesDto iotListDevices = this.getIotListDevicesService().updateIotListDevices(iotListDevicesRequest);
        return new ResponseEntity<>(new SimpleRestResponse(iotListDevices), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<IotListDevicesDto>> addIotListDevices(@Valid @RequestBody IotListDevicesRequest iotListDevicesRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getIotListDevicesValidator().validate(iotListDevicesRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        IotListDevicesDto dto = this.getIotListDevicesService().addIotListDevices(iotListDevicesRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{iotListDevicesId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteIotListDevices(@PathVariable String iotListDevicesId) {
        logger.info("deleting {}", iotListDevicesId);
        this.getIotListDevicesService().removeIotListDevices(Integer.valueOf(iotListDevicesId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(iotListDevicesId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

