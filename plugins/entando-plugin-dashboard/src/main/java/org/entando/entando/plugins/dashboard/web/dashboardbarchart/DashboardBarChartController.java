/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardbarchart;

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

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.IDashboardBarChartService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.model.DashboardBarChartDto;
import org.entando.entando.plugins.dashboard.web.dashboardbarchart.model.DashboardBarChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboardbarchart.validator.DashboardBarChartValidator;

//@RestController
//@RequestMapping(value = "/plugins/dashboard/dashboardBarCharts")
public class DashboardBarChartController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardBarChartService dashboardBarChartService;

    @Autowired
    private DashboardBarChartValidator dashboardBarChartValidator;

    protected IDashboardBarChartService getDashboardBarChartService() {
        return dashboardBarChartService;
    }

    public void setDashboardBarChartService(IDashboardBarChartService dashboardBarChartService) {
        this.dashboardBarChartService = dashboardBarChartService;
    }

    protected DashboardBarChartValidator getDashboardBarChartValidator() {
        return dashboardBarChartValidator;
    }

    public void setDashboardBarChartValidator(DashboardBarChartValidator dashboardBarChartValidator) {
        this.dashboardBarChartValidator = dashboardBarChartValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<DashboardBarChartDto>> > getDashboardBarCharts(RestListRequest requestList) throws JsonProcessingException {
        this.getDashboardBarChartValidator().validateRestListRequest(requestList, DashboardBarChartDto.class);
        PagedMetadata<DashboardBarChartDto> result = this.getDashboardBarChartService().getDashboardBarCharts(requestList);
        this.getDashboardBarChartValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardBarChartId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardBarChartDto>> getDashboardBarChart(@PathVariable String dashboardBarChartId) {
		DashboardBarChartDto dashboardBarChart = this.getDashboardBarChartService().getDashboardBarChart(Integer.valueOf(dashboardBarChartId));
        return new ResponseEntity<>(new SimpleRestResponse(dashboardBarChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardBarChartId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardBarChartDto>> updateDashboardBarChart(@PathVariable String dashboardBarChartId, @Valid @RequestBody DashboardBarChartRequest dashboardBarChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDashboardBarChartValidator().validateBodyName(String.valueOf(dashboardBarChartId), dashboardBarChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        DashboardBarChartDto dashboardBarChart = this.getDashboardBarChartService().updateDashboardBarChart(dashboardBarChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dashboardBarChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardBarChartDto>> addDashboardBarChart(@Valid @RequestBody DashboardBarChartRequest dashboardBarChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getDashboardBarChartValidator().validate(dashboardBarChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DashboardBarChartDto dto = this.getDashboardBarChartService().addDashboardBarChart(dashboardBarChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardBarChartId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDashboardBarChart(@PathVariable String dashboardBarChartId) {
        logger.info("deleting {}", dashboardBarChartId);
        this.getDashboardBarChartService().removeDashboardBarChart(Integer.valueOf(dashboardBarChartId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(dashboardBarChartId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

