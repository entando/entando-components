/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.web.iotlistdevices.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.jpiot.web.iotlistdevices.model.IotListDevicesRequest;


@Component
public class IotListDevicesValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_IOTLISTDEVICES_NOT_FOUND = "2" ;
    public static final String ERRCODE_IOTLISTDEVICES_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return IotListDevicesRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //IotListDevicesRequest request = (IotListDevicesRequest) target;
    }

    public void validateBodyName(String iotListDevicesId, IotListDevicesRequest iotListDevicesRequest, Errors errors) {
        if (!StringUtils.equals(iotListDevicesId, String.valueOf(iotListDevicesRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{iotListDevicesId, iotListDevicesRequest.getId()}, "iotListDevices.id.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
