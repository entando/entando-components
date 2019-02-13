/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardgaugechart;

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

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.IDashboardGaugeChartService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.model.DashboardGaugeChartDto;
import org.entando.entando.plugins.dashboard.web.dashboardgaugechart.model.DashboardGaugeChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboardgaugechart.validator.DashboardGaugeChartValidator;

@RestController
@RequestMapping(value = "/plugins/dashboard/dashboardGaugeCharts")
public class DashboardGaugeChartController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardGaugeChartService dashboardGaugeChartService;

    @Autowired
    private DashboardGaugeChartValidator dashboardGaugeChartValidator;

    protected IDashboardGaugeChartService getDashboardGaugeChartService() {
        return dashboardGaugeChartService;
    }

    public void setDashboardGaugeChartService(IDashboardGaugeChartService dashboardGaugeChartService) {
        this.dashboardGaugeChartService = dashboardGaugeChartService;
    }

    protected DashboardGaugeChartValidator getDashboardGaugeChartValidator() {
        return dashboardGaugeChartValidator;
    }

    public void setDashboardGaugeChartValidator(DashboardGaugeChartValidator dashboardGaugeChartValidator) {
        this.dashboardGaugeChartValidator = dashboardGaugeChartValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<DashboardGaugeChartDto>> > getDashboardGaugeCharts(RestListRequest requestList) throws JsonProcessingException {
        this.getDashboardGaugeChartValidator().validateRestListRequest(requestList, DashboardGaugeChartDto.class);
        PagedMetadata<DashboardGaugeChartDto> result = this.getDashboardGaugeChartService().getDashboardGaugeCharts(requestList);
        this.getDashboardGaugeChartValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardGaugeChartId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardGaugeChartDto>> getDashboardGaugeChart(@PathVariable String dashboardGaugeChartId) {
		DashboardGaugeChartDto dashboardGaugeChart = this.getDashboardGaugeChartService().getDashboardGaugeChart(Integer.valueOf(dashboardGaugeChartId));
        return new ResponseEntity<>(new SimpleRestResponse(dashboardGaugeChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardGaugeChartId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardGaugeChartDto>> updateDashboardGaugeChart(@PathVariable String dashboardGaugeChartId, @Valid @RequestBody DashboardGaugeChartRequest dashboardGaugeChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDashboardGaugeChartValidator().validateBodyName(String.valueOf(dashboardGaugeChartId), dashboardGaugeChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        DashboardGaugeChartDto dashboardGaugeChart = this.getDashboardGaugeChartService().updateDashboardGaugeChart(dashboardGaugeChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dashboardGaugeChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardGaugeChartDto>> addDashboardGaugeChart(@Valid @RequestBody DashboardGaugeChartRequest dashboardGaugeChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getDashboardGaugeChartValidator().validate(dashboardGaugeChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DashboardGaugeChartDto dto = this.getDashboardGaugeChartService().addDashboardGaugeChart(dashboardGaugeChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardGaugeChartId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDashboardGaugeChart(@PathVariable String dashboardGaugeChartId) {
        logger.info("deleting {}", dashboardGaugeChartId);
        this.getDashboardGaugeChartService().removeDashboardGaugeChart(Integer.valueOf(dashboardGaugeChartId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(dashboardGaugeChartId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

