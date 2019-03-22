/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboarddonutchart;

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

import org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.IDashboardDonutChartService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.model.DashboardDonutChartDto;
import org.entando.entando.plugins.dashboard.web.dashboarddonutchart.model.DashboardDonutChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboarddonutchart.validator.DashboardDonutChartValidator;

//@RestController
//@RequestMapping(value = "/plugins/dashboard/dashboardDonutCharts")
public class DashboardDonutChartController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardDonutChartService dashboardDonutChartService;

    @Autowired
    private DashboardDonutChartValidator dashboardDonutChartValidator;

    protected IDashboardDonutChartService getDashboardDonutChartService() {
        return dashboardDonutChartService;
    }

    public void setDashboardDonutChartService(IDashboardDonutChartService dashboardDonutChartService) {
        this.dashboardDonutChartService = dashboardDonutChartService;
    }

    protected DashboardDonutChartValidator getDashboardDonutChartValidator() {
        return dashboardDonutChartValidator;
    }

    public void setDashboardDonutChartValidator(DashboardDonutChartValidator dashboardDonutChartValidator) {
        this.dashboardDonutChartValidator = dashboardDonutChartValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<DashboardDonutChartDto>> > getDashboardDonutCharts(RestListRequest requestList) throws JsonProcessingException {
        this.getDashboardDonutChartValidator().validateRestListRequest(requestList, DashboardDonutChartDto.class);
        PagedMetadata<DashboardDonutChartDto> result = this.getDashboardDonutChartService().getDashboardDonutCharts(requestList);
        this.getDashboardDonutChartValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardDonutChartId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardDonutChartDto>> getDashboardDonutChart(@PathVariable String dashboardDonutChartId) {
		DashboardDonutChartDto dashboardDonutChart = this.getDashboardDonutChartService().getDashboardDonutChart(Integer.valueOf(dashboardDonutChartId));
        return new ResponseEntity<>(new SimpleRestResponse(dashboardDonutChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardDonutChartId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardDonutChartDto>> updateDashboardDonutChart(@PathVariable String dashboardDonutChartId, @Valid @RequestBody DashboardDonutChartRequest dashboardDonutChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDashboardDonutChartValidator().validateBodyName(String.valueOf(dashboardDonutChartId), dashboardDonutChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        DashboardDonutChartDto dashboardDonutChart = this.getDashboardDonutChartService().updateDashboardDonutChart(dashboardDonutChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dashboardDonutChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardDonutChartDto>> addDashboardDonutChart(@Valid @RequestBody DashboardDonutChartRequest dashboardDonutChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getDashboardDonutChartValidator().validate(dashboardDonutChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DashboardDonutChartDto dto = this.getDashboardDonutChartService().addDashboardDonutChart(dashboardDonutChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardDonutChartId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDashboardDonutChart(@PathVariable String dashboardDonutChartId) {
        logger.info("deleting {}", dashboardDonutChartId);
        this.getDashboardDonutChartService().removeDashboardDonutChart(Integer.valueOf(dashboardDonutChartId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(dashboardDonutChartId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

