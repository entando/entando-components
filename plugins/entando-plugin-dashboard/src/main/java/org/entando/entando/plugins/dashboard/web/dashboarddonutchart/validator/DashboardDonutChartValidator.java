/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboarddonutchart.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.dashboard.web.dashboarddonutchart.model.DashboardDonutChartRequest;


@Component
public class DashboardDonutChartValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_DASHBOARDDONUTCHART_NOT_FOUND = "2" ;
    public static final String ERRCODE_DASHBOARDDONUTCHART_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return DashboardDonutChartRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //DashboardDonutChartRequest request = (DashboardDonutChartRequest) target;
    }

    public void validateBodyName(String dashboardDonutChartId, DashboardDonutChartRequest dashboardDonutChartRequest, Errors errors) {
        if (!StringUtils.equals(dashboardDonutChartId, String.valueOf(dashboardDonutChartRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{dashboardDonutChartId, dashboardDonutChartRequest.getId()}, "dashboardDonutChart.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
