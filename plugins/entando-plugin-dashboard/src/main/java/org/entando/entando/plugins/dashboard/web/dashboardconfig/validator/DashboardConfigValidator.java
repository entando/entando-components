/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardconfig.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;


@Component
public class DashboardConfigValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_DASHBOARDCONFIG_NOT_FOUND = "2" ;
    public static final String ERRCODE_DASHBOARDCONFIG_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return DashboardConfigRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //DashboardConfigRequest request = (DashboardConfigRequest) target;
    }

    public void validateBodyName(String dashboardConfigId, DashboardConfigRequest dashboardConfigRequest, Errors errors) {
        if (!StringUtils.equals(dashboardConfigId, String.valueOf(dashboardConfigRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{dashboardConfigId, dashboardConfigRequest.getId()}, "dashboardConfig.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
