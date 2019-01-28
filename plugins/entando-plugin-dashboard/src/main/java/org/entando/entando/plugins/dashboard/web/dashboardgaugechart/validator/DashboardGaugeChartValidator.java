/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardgaugechart.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.dashboard.web.dashboardgaugechart.model.DashboardGaugeChartRequest;


@Component
public class DashboardGaugeChartValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_DASHBOARDGAUGECHART_NOT_FOUND = "2" ;
    public static final String ERRCODE_DASHBOARDGAUGECHART_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return DashboardGaugeChartRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //DashboardGaugeChartRequest request = (DashboardGaugeChartRequest) target;
    }

    public void validateBodyName(String dashboardGaugeChartId, DashboardGaugeChartRequest dashboardGaugeChartRequest, Errors errors) {
        if (!StringUtils.equals(dashboardGaugeChartId, String.valueOf(dashboardGaugeChartRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{dashboardGaugeChartId, dashboardGaugeChartRequest.getId()}, "dashboardGaugeChart.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
