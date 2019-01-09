/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardtable.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.dashboard.web.dashboardtable.model.DashboardTableRequest;


@Component
public class DashboardTableValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_DASHBOARDTABLE_NOT_FOUND = "2" ;
    public static final String ERRCODE_DASHBOARDTABLE_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return DashboardTableRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //DashboardTableRequest request = (DashboardTableRequest) target;
    }

    public void validateBodyName(String dashboardTableId, DashboardTableRequest dashboardTableRequest, Errors errors) {
        if (!StringUtils.equals(dashboardTableId, String.valueOf(dashboardTableRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{dashboardTableId, dashboardTableRequest.getId()}, "dashboardTable.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
