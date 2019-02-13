/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardlinechart;

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

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.IDashboardLineChartService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.model.DashboardLineChartDto;
import org.entando.entando.plugins.dashboard.web.dashboardlinechart.model.DashboardLineChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboardlinechart.validator.DashboardLineChartValidator;

@RestController
@RequestMapping(value = "/plugins/dashboard/dashboardLineCharts")
public class DashboardLineChartController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardLineChartService dashboardLineChartService;

    @Autowired
    private DashboardLineChartValidator dashboardLineChartValidator;

    protected IDashboardLineChartService getDashboardLineChartService() {
        return dashboardLineChartService;
    }

    public void setDashboardLineChartService(IDashboardLineChartService dashboardLineChartService) {
        this.dashboardLineChartService = dashboardLineChartService;
    }

    protected DashboardLineChartValidator getDashboardLineChartValidator() {
        return dashboardLineChartValidator;
    }

    public void setDashboardLineChartValidator(DashboardLineChartValidator dashboardLineChartValidator) {
        this.dashboardLineChartValidator = dashboardLineChartValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<DashboardLineChartDto>> > getDashboardLineCharts(RestListRequest requestList) throws JsonProcessingException {
        this.getDashboardLineChartValidator().validateRestListRequest(requestList, DashboardLineChartDto.class);
        PagedMetadata<DashboardLineChartDto> result = this.getDashboardLineChartService().getDashboardLineCharts(requestList);
        this.getDashboardLineChartValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardLineChartId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardLineChartDto>> getDashboardLineChart(@PathVariable String dashboardLineChartId) {
		DashboardLineChartDto dashboardLineChart = this.getDashboardLineChartService().getDashboardLineChart(Integer.valueOf(dashboardLineChartId));
        return new ResponseEntity<>(new SimpleRestResponse(dashboardLineChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardLineChartId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardLineChartDto>> updateDashboardLineChart(@PathVariable String dashboardLineChartId, @Valid @RequestBody DashboardLineChartRequest dashboardLineChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDashboardLineChartValidator().validateBodyName(String.valueOf(dashboardLineChartId), dashboardLineChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        DashboardLineChartDto dashboardLineChart = this.getDashboardLineChartService().updateDashboardLineChart(dashboardLineChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dashboardLineChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardLineChartDto>> addDashboardLineChart(@Valid @RequestBody DashboardLineChartRequest dashboardLineChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getDashboardLineChartValidator().validate(dashboardLineChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DashboardLineChartDto dto = this.getDashboardLineChartService().addDashboardLineChart(dashboardLineChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardLineChartId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDashboardLineChart(@PathVariable String dashboardLineChartId) {
        logger.info("deleting {}", dashboardLineChartId);
        this.getDashboardLineChartService().removeDashboardLineChart(Integer.valueOf(dashboardLineChartId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(dashboardLineChartId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

