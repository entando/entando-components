/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.web.iotconfig.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.jpiot.web.iotconfig.model.IotConfigRequest;


@Component
public class IotConfigValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_IOTCONFIG_NOT_FOUND = "2" ;
    public static final String ERRCODE_IOTCONFIG_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return IotConfigRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //IotConfigRequest request = (IotConfigRequest) target;
    }

    public void validateBodyName(String iotConfigId, IotConfigRequest iotConfigRequest, Errors errors) {
        if (!StringUtils.equals(iotConfigId, String.valueOf(iotConfigRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{iotConfigId, iotConfigRequest.getId()}, "iotConfig.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
