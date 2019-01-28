/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardpiechart.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.dashboard.web.dashboardpiechart.model.DashboardPieChartRequest;


@Component
public class DashboardPieChartValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_DASHBOARDPIECHART_NOT_FOUND = "2" ;
    public static final String ERRCODE_DASHBOARDPIECHART_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return DashboardPieChartRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //DashboardPieChartRequest request = (DashboardPieChartRequest) target;
    }

    public void validateBodyName(String dashboardPieChartId, DashboardPieChartRequest dashboardPieChartRequest, Errors errors) {
        if (!StringUtils.equals(dashboardPieChartId, String.valueOf(dashboardPieChartRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{dashboardPieChartId, dashboardPieChartRequest.getId()}, "dashboardPieChart.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
