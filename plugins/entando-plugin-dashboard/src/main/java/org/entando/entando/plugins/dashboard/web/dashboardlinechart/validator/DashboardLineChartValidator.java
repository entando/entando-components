/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardlinechart.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.dashboard.web.dashboardlinechart.model.DashboardLineChartRequest;


@Component
public class DashboardLineChartValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_DASHBOARDLINECHART_NOT_FOUND = "2" ;
    public static final String ERRCODE_DASHBOARDLINECHART_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return DashboardLineChartRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //DashboardLineChartRequest request = (DashboardLineChartRequest) target;
    }

    public void validateBodyName(String dashboardLineChartId, DashboardLineChartRequest dashboardLineChartRequest, Errors errors) {
        if (!StringUtils.equals(dashboardLineChartId, String.valueOf(dashboardLineChartRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{dashboardLineChartId, dashboardLineChartRequest.getId()}, "dashboardLineChart.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
