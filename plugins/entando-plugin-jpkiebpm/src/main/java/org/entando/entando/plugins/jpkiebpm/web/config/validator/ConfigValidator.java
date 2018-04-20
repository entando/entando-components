/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpkiebpm.web.config.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieServerConfigDto;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ConfigValidator extends AbstractPaginationValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String ERRCODE_CONFIG_NOT_FOUND = "1";
    public static final String ERRCODE_CONFIG_ALREADY_EXISTS = "2";
    public static final String ERRCODE_URINAME_MISMATCH = "3";

    //@Autowired
    //private IKieFormManager formManager;
    @Override
    public boolean supports(Class<?> paramClass) {
        return KieServerConfigDto.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        KieServerConfigDto request = (KieServerConfigDto) target;
        String configCode = request.getId();
        /*
        try {
            if (null != formManager.getKieServerConfigurations().get(configCode)) {
                errors.reject(ERRCODE_CONFIG_ALREADY_EXISTS, new String[]{configCode}, "config.exists");
            }
        } catch (Exception e) {
            logger.error("Error loading config {}", configCode, e);
            throw new RestServerError("Error loading config", e);
        }
         */
    }

    public void validateBodyName(String configCode, KieServerConfigDto configRequest, Errors errors) {
        if (!StringUtils.equals(configCode, configRequest.getId())) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new String[]{configCode, configRequest.getName()}, "configp.code.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }

}
