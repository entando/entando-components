/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardmap;

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

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.IDashboardMapService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.model.DashboardMapDto;
import org.entando.entando.plugins.dashboard.web.dashboardmap.model.DashboardMapRequest;
import org.entando.entando.plugins.dashboard.web.dashboardmap.validator.DashboardMapValidator;

//@RestController
//@RequestMapping(value = "/plugins/dashboard/dashboardMaps")
public class DashboardMapController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardMapService dashboardMapService;

    @Autowired
    private DashboardMapValidator dashboardMapValidator;

    protected IDashboardMapService getDashboardMapService() {
        return dashboardMapService;
    }

    public void setDashboardMapService(IDashboardMapService dashboardMapService) {
        this.dashboardMapService = dashboardMapService;
    }

    protected DashboardMapValidator getDashboardMapValidator() {
        return dashboardMapValidator;
    }

    public void setDashboardMapValidator(DashboardMapValidator dashboardMapValidator) {
        this.dashboardMapValidator = dashboardMapValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<DashboardMapDto>> > getDashboardMaps(RestListRequest requestList) throws JsonProcessingException {
        this.getDashboardMapValidator().validateRestListRequest(requestList, DashboardMapDto.class);
        PagedMetadata<DashboardMapDto> result = this.getDashboardMapService().getDashboardMaps(requestList);
        this.getDashboardMapValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardMapId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardMapDto>> getDashboardMap(@PathVariable String dashboardMapId) {
		DashboardMapDto dashboardMap = this.getDashboardMapService().getDashboardMap(Integer.valueOf(dashboardMapId));
        return new ResponseEntity<>(new SimpleRestResponse(dashboardMap), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardMapId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardMapDto>> updateDashboardMap(@PathVariable String dashboardMapId, @Valid @RequestBody DashboardMapRequest dashboardMapRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDashboardMapValidator().validateBodyName(String.valueOf(dashboardMapId), dashboardMapRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        DashboardMapDto dashboardMap = this.getDashboardMapService().updateDashboardMap(dashboardMapRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dashboardMap), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardMapDto>> addDashboardMap(@Valid @RequestBody DashboardMapRequest dashboardMapRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getDashboardMapValidator().validate(dashboardMapRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DashboardMapDto dto = this.getDashboardMapService().addDashboardMap(dashboardMapRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardMapId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDashboardMap(@PathVariable String dashboardMapId) {
        logger.info("deleting {}", dashboardMapId);
        this.getDashboardMapService().removeDashboardMap(Integer.valueOf(dashboardMapId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(dashboardMapId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

