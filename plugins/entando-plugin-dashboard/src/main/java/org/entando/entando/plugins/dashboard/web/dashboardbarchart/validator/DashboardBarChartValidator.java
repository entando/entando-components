/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardbarchart.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.dashboard.web.dashboardbarchart.model.DashboardBarChartRequest;


@Component
public class DashboardBarChartValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_DASHBOARDBARCHART_NOT_FOUND = "2" ;
    public static final String ERRCODE_DASHBOARDBARCHART_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return DashboardBarChartRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //DashboardBarChartRequest request = (DashboardBarChartRequest) target;
    }

    public void validateBodyName(String dashboardBarChartId, DashboardBarChartRequest dashboardBarChartRequest, Errors errors) {
        if (!StringUtils.equals(dashboardBarChartId, String.valueOf(dashboardBarChartRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{dashboardBarChartId, dashboardBarChartRequest.getId()}, "dashboardBarChart.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
