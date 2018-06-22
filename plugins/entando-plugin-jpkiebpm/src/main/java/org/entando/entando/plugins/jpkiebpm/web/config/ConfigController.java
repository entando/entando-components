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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieServerConfigDto;
import org.entando.entando.plugins.jpkiebpm.web.config.validator.ConfigValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.IKieBpmService;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.model.DatatableWidgetConfigDto;
import org.entando.entando.plugins.jpkiebpm.web.model.DatatableWidgetConfigRequest;
import org.springframework.validation.BeanPropertyBindingResult;

@RestController
public class ConfigController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IKieConfigService kieConfigService;

    @Autowired
    private ConfigValidator configValidator;
//
//    @Autowired
//    @Qualifier("jpkiebpmsManager")
//    IKieFormManager kieFormManager;

    @Autowired
    @Qualifier("jpkiebpmsCaseManager")
    IKieFormManager caseManager;

    @Autowired
    private IKieFormOverrideManager overrideManager;

    @Autowired
    private IKieBpmService kieBpmService;

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

    public IKieBpmService getKieBpmService() {
        return kieBpmService;
    }

    public void setKieBpmService(IKieBpmService kieBpmService) {
        this.kieBpmService = kieBpmService;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getConfigs() throws JsonProcessingException {

        List<KieServerConfigDto> result = this.getKieConfigService().getConfigs(/*requestList*/);

        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs/{configCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getConfig(@PathVariable String configCode) {
        KieServerConfigDto group = this.getKieConfigService().getConfig(configCode);
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
        KieServerConfigDto dto = this.getKieConfigService().addConfig(configRequest, bindingResult);
        return this.getPostPutResponse(dto, bindingResult);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs/{configCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateConfig(@PathVariable String configCode, @Valid @RequestBody KieServerConfigDto configRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getConfigValidator().validateBodyName(configCode, configRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        KieServerConfigDto config = this.getKieConfigService().updateConfig(configRequest, bindingResult);
        return this.getPostPutResponse(config, bindingResult);
    }

    private ResponseEntity<RestResponse> getPostPutResponse(KieServerConfigDto config, BindingResult bindingResult) {
        RestError error = (bindingResult.hasErrors()) ? new RestError("40", "Test connection failed") : null;
        List<RestError> errors = null;
        if (null != error) {
            errors = new ArrayList<>();
            errors.add(error);
        }
        HttpStatus httpStatus = (bindingResult.hasErrors()) ? HttpStatus.ACCEPTED : HttpStatus.OK;
        RestResponse response = new RestResponse(config, errors, null);
        return new ResponseEntity<>(response, httpStatus);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs/{configCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteConfig(@PathVariable String configCode) throws ApsSystemException {
        logger.info("deleting {}", configCode);
        this.getKieConfigService().removeConfig(configCode);
        Map<String, String> result = new HashMap<>();
        result.put("code", configCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/testServerConfigs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> testConfig(@Valid @RequestBody KieServerConfigDto configRequest, BindingResult bindingResult) {
        logger.info("test server {}", configRequest);
        String testResult = this.getKieConfigService().testServerConfigs(configRequest);
        Map<String, String> result = new HashMap<>();
        result.put("result", testResult);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/testAllServerConfigs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> testAllConfig(@Valid @RequestBody KieServerConfigDto configRequest, BindingResult bindingResult) {
        logger.info("test all servers");
        Map<String, String> testResult = this.getKieConfigService().testAllServerConfigs();
        return new ResponseEntity<>(new RestResponse(testResult), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs/{configCode}/deploymentUnits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getDeploymentUnits(@PathVariable String configCode) {

        List<KieContainer> containers = null;
        try {
            KieServerConfigDto serverConfigDto = kieConfigService.getConfig(configCode);
            //TODO This call makes the fetched config the active one.  Need to scope this to the widget at some point
            kieConfigService.setConfig(serverConfigDto);
            containers = this.kieConfigService.getContainerList();
        } catch (Exception e) {
            logger.error("Failed to fetch container ids ", e);
        }

        return new ResponseEntity<>(new RestResponse(containers), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs/{configCode}/processList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getProcessList(@PathVariable String configCode) {

        List<KieProcess> processes = new ArrayList<KieProcess>();
        try {

            KieServerConfigDto serverConfigDto = kieConfigService.getConfig(configCode);
            //TODO This call makes the fetched config the active one.  This should be scoped to the widget at some point
            kieConfigService.setConfig(serverConfigDto);
            processes = kieConfigService.getProcessDefinitionsList();
        } catch (Exception e) {
            logger.error("Failed to fetch container ids ", e);
        }

        return new ResponseEntity<>(new RestResponse(processes), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs/{configCode}/caseDefinitions/{deploymentUnit:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getCaseDefinitions(@PathVariable String configCode, @PathVariable String deploymentUnit) {

        JSONObject cases = null;
        try {

            KieServerConfigDto serverConfigDto = kieConfigService.getConfig(configCode);
            KieBpmConfig bpmConfig = kieConfigService.buildConfig(serverConfigDto);
            //TODO This call makes the fetched config the active one.  This should be scoped to the widget at some point
            caseManager.setConfig(bpmConfig);

            cases = ((CaseManager) caseManager).getCasesDefinitions(deploymentUnit);
        } catch (Exception e) {
            logger.error("Failed to fetch container ids ", e);
        }

        if (cases == null) {
            cases = new JSONObject();
        }
        return new ResponseEntity<>(new RestResponse(cases.toMap()), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/serverConfigs/{configCode}/processesList/{processId:.+}/overrides", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getProcessOverrides(@PathVariable String configCode, @PathVariable String processId) {

        List<KieFormOverride> overrides = new ArrayList<>();
        try {
            overrides = overrideManager.getFormOverrides(configCode, processId);
        } catch (Exception e) {
            logger.error("Failed to fetch container ids ", e);
        }

        return new ResponseEntity<>(new RestResponse(overrides), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/datatableWidget/{configId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getDataTableWIdgetConfig(@PathVariable int configId) {

        DatatableWidgetConfigDto dto = this.getKieBpmService().getDataTableWIdgetConfig(configId);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/datatableWidget", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> createDataTableWIdgetConfig(@RequestBody DatatableWidgetConfigRequest req) {

        DatatableWidgetConfigDto dto = this.getKieBpmService().updateDataTableWIdgetConfig(req);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/datatableWidget/{configId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateDataTableWIdgetConfig(@PathVariable int configId, @RequestBody DatatableWidgetConfigRequest req) {
        if (configId != req.getId()) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(configId, "configId");
            bindingResult.reject("2", new String[]{}, "parameters mismatch");
            throw new ValidationGenericException(bindingResult);
        }
        DatatableWidgetConfigDto dto = this.getKieBpmService().updateDataTableWIdgetConfig(req);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/datatableWidget/{configId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteDataTableWIdgetConfig(@PathVariable int configId) {

        DatatableWidgetConfigDto dto = this.getKieBpmService().deleteDataTableWIdgetConfig(configId);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/datatypeWidget", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> createDataTypeWIdgetConfig(@RequestBody DatatableWidgetConfigRequest req) {

        DatatableWidgetConfigDto dto = this.getKieBpmService().updateDataTypeWIdgetConfig(req);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/datatypeWidget/{configId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateDataTypeWIdgetConfig(@PathVariable int configId, @RequestBody DatatableWidgetConfigRequest req) {
        if (configId != req.getId()) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(configId, "configId");
            bindingResult.reject("2", new String[]{}, "parameters mismatch");
            throw new ValidationGenericException(bindingResult);
        }
        DatatableWidgetConfigDto dto = this.getKieBpmService().updateDataTypeWIdgetConfig(req);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/datatableWidget/form", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> chooseForm() {

        DatatableWidgetConfigDto dto = this.getKieBpmService().chooseForm();
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/kiebpm/datatableWidget/processform", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> chooseProcessForm() {

        DatatableWidgetConfigDto dto = this.getKieBpmService().chooseProcessForm();
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }
}
