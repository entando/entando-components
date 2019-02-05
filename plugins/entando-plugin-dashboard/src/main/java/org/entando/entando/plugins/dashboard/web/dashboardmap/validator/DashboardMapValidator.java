/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardmap.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.dashboard.web.dashboardmap.model.DashboardMapRequest;


@Component
public class DashboardMapValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_DASHBOARDMAP_NOT_FOUND = "2" ;
    public static final String ERRCODE_DASHBOARDMAP_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return DashboardMapRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //DashboardMapRequest request = (DashboardMapRequest) target;
    }

    public void validateBodyName(String dashboardMapId, DashboardMapRequest dashboardMapRequest, Errors errors) {
        if (!StringUtils.equals(dashboardMapId, String.valueOf(dashboardMapRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{dashboardMapId, dashboardMapRequest.getId()}, "dashboardMap.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
