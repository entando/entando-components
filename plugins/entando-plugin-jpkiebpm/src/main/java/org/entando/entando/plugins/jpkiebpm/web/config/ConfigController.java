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
package org.entando.entando.plugins.jpkiebpm.web.config;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieConfigService;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieServerConfigDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.plugins.jpkiebpm.web.config.validator.ConfigValidator;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kiebpm/serverConfigs")
public class ConfigController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IKieConfigService kieConfigService;

    @Autowired
    private ConfigValidator configValidator;

    public IKieConfigService getKieConfigService() {
        return kieConfigService;
    }

    public void setKieConfigService(IKieConfigService kieConfigService) {
        this.kieConfigService = kieConfigService;
    }

    public ConfigValidator getConfigValidator() {
        return configValidator;
    }

    public void setConfigValidator(ConfigValidator configValidator) {
        this.configValidator = configValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getConfigs(RestListRequest requestList) throws JsonProcessingException {
        this.getConfigValidator().validateRestListRequest(requestList, KieServerConfigDto.class);
        PagedMetadata<KieServerConfigDto> result = this.getKieConfigService().getConfigs(requestList);
        this.getConfigValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{configCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getConfig(@PathVariable String configCode) {
        KieServerConfigDto group = this.getKieConfigService().getConfig(configCode);
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addConfig(@Valid @RequestBody KieServerConfigDto configRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        this.getConfigValidator().validate(configRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        KieServerConfigDto dto = this.getKieConfigService().addConfig(configRequest);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{configCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateConfig(@PathVariable String configCode, @Valid @RequestBody KieServerConfigDto configRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getConfigValidator().validateBodyName(configCode, configRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        KieServerConfigDto config = this.getKieConfigService().updateConfig(configRequest);
        return new ResponseEntity<>(new RestResponse(config), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{configCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteConfig(@PathVariable String configCode) throws ApsSystemException {
        logger.info("deleting {}", configCode);
        this.getKieConfigService().removeConfig(configCode);
        Map<String, String> result = new HashMap<>();
        result.put("code", configCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}
