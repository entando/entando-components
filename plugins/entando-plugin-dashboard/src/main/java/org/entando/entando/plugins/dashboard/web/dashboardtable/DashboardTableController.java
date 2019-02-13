/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardtable;

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

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.IDashboardTableService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.model.DashboardTableDto;
import org.entando.entando.plugins.dashboard.web.dashboardtable.model.DashboardTableRequest;
import org.entando.entando.plugins.dashboard.web.dashboardtable.validator.DashboardTableValidator;

@RestController
@RequestMapping(value = "/plugins/dashboard/dashboardTables")
public class DashboardTableController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardTableService dashboardTableService;

    @Autowired
    private DashboardTableValidator dashboardTableValidator;

    protected IDashboardTableService getDashboardTableService() {
        return dashboardTableService;
    }

    public void setDashboardTableService(IDashboardTableService dashboardTableService) {
        this.dashboardTableService = dashboardTableService;
    }

    protected DashboardTableValidator getDashboardTableValidator() {
        return dashboardTableValidator;
    }

    public void setDashboardTableValidator(DashboardTableValidator dashboardTableValidator) {
        this.dashboardTableValidator = dashboardTableValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<DashboardTableDto>> > getDashboardTables(RestListRequest requestList) throws JsonProcessingException {
        this.getDashboardTableValidator().validateRestListRequest(requestList, DashboardTableDto.class);
        PagedMetadata<DashboardTableDto> result = this.getDashboardTableService().getDashboardTables(requestList);
        this.getDashboardTableValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardTableId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardTableDto>> getDashboardTable(@PathVariable String dashboardTableId) {
		DashboardTableDto dashboardTable = this.getDashboardTableService().getDashboardTable(Integer.valueOf(dashboardTableId));
        return new ResponseEntity<>(new SimpleRestResponse(dashboardTable), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardTableId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardTableDto>> updateDashboardTable(@PathVariable String dashboardTableId, @Valid @RequestBody DashboardTableRequest dashboardTableRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDashboardTableValidator().validateBodyName(String.valueOf(dashboardTableId), dashboardTableRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        DashboardTableDto dashboardTable = this.getDashboardTableService().updateDashboardTable(dashboardTableRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dashboardTable), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardTableDto>> addDashboardTable(@Valid @RequestBody DashboardTableRequest dashboardTableRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getDashboardTableValidator().validate(dashboardTableRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DashboardTableDto dto = this.getDashboardTableService().addDashboardTable(dashboardTableRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardTableId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDashboardTable(@PathVariable String dashboardTableId) {
        logger.info("deleting {}", dashboardTableId);
        this.getDashboardTableService().removeDashboardTable(Integer.valueOf(dashboardTableId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(dashboardTableId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

