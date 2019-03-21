/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardpiechart;

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

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.IDashboardPieChartService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.model.DashboardPieChartDto;
import org.entando.entando.plugins.dashboard.web.dashboardpiechart.model.DashboardPieChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboardpiechart.validator.DashboardPieChartValidator;
//
//@RestController
//@RequestMapping(value = "/plugins/dashboard/dashboardPieCharts")
public class DashboardPieChartController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardPieChartService dashboardPieChartService;

    @Autowired
    private DashboardPieChartValidator dashboardPieChartValidator;

    protected IDashboardPieChartService getDashboardPieChartService() {
        return dashboardPieChartService;
    }

    public void setDashboardPieChartService(IDashboardPieChartService dashboardPieChartService) {
        this.dashboardPieChartService = dashboardPieChartService;
    }

    protected DashboardPieChartValidator getDashboardPieChartValidator() {
        return dashboardPieChartValidator;
    }

    public void setDashboardPieChartValidator(DashboardPieChartValidator dashboardPieChartValidator) {
        this.dashboardPieChartValidator = dashboardPieChartValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PagedMetadata<DashboardPieChartDto>> > getDashboardPieCharts(RestListRequest requestList) throws JsonProcessingException {
        this.getDashboardPieChartValidator().validateRestListRequest(requestList, DashboardPieChartDto.class);
        PagedMetadata<DashboardPieChartDto> result = this.getDashboardPieChartService().getDashboardPieCharts(requestList);
        this.getDashboardPieChartValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardPieChartId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardPieChartDto>> getDashboardPieChart(@PathVariable String dashboardPieChartId) {
		DashboardPieChartDto dashboardPieChart = this.getDashboardPieChartService().getDashboardPieChart(Integer.valueOf(dashboardPieChartId));
        return new ResponseEntity<>(new SimpleRestResponse(dashboardPieChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardPieChartId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardPieChartDto>> updateDashboardPieChart(@PathVariable String dashboardPieChartId, @Valid @RequestBody DashboardPieChartRequest dashboardPieChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDashboardPieChartValidator().validateBodyName(String.valueOf(dashboardPieChartId), dashboardPieChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        DashboardPieChartDto dashboardPieChart = this.getDashboardPieChartService().updateDashboardPieChart(dashboardPieChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dashboardPieChart), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DashboardPieChartDto>> addDashboardPieChart(@Valid @RequestBody DashboardPieChartRequest dashboardPieChartRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getDashboardPieChartValidator().validate(dashboardPieChartRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DashboardPieChartDto dto = this.getDashboardPieChartService().addDashboardPieChart(dashboardPieChartRequest);
        return new ResponseEntity<>(new SimpleRestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{dashboardPieChartId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDashboardPieChart(@PathVariable String dashboardPieChartId) {
        logger.info("deleting {}", dashboardPieChartId);
        this.getDashboardPieChartService().removeDashboardPieChart(Integer.valueOf(dashboardPieChartId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(dashboardPieChartId));
        return new ResponseEntity<>(new SimpleRestResponse(result), HttpStatus.OK);
    }

}

