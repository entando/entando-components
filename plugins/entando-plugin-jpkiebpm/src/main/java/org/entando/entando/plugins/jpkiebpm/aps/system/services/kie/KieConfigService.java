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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieServerConfigDto;
import org.entando.entando.plugins.jpkiebpm.web.config.validator.ConfigValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class KieConfigService implements IKieConfigService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IKieFormManager kieFormManager;

    protected IDtoBuilder<KieBpmConfig, KieServerConfigDto> getKieServerConfigDtoBuilder() {
        return new DtoBuilder<KieBpmConfig, KieServerConfigDto>() {
            @Override
            protected KieServerConfigDto toDto(KieBpmConfig src) {
                return new KieServerConfigDto(src);
            }
        };
    }

    @Override
    public List<KieServerConfigDto> getConfigs(/*RestListRequest requestList*/) {
        List<KieServerConfigDto> list = null;
        try {
            Map<String, KieBpmConfig> map = this.getKieFormManager().getKieServerConfigurations();
            List<KieBpmConfig> configs = new ArrayList<>(map.values());
            BeanComparator c = new BeanComparator("id");
            Collections.sort(configs, c);
            list = this.getKieServerConfigDtoBuilder().convert(configs);
        } catch (Exception t) {
            logger.error("error in get configuration", t);
            throw new RestServerError("error in get configuration", t);
        }
        return list;
    }

    @Override
    public KieServerConfigDto getConfig(String configCode) {
        KieServerConfigDto configDto = null;
        try {
            Map<String, KieBpmConfig> map = this.getKieFormManager().getKieServerConfigurations();
            KieBpmConfig config = map.get(configCode);
            if (null == config) {
                logger.warn("no config found with code {}", configCode);
                throw new RestRourceNotFoundException(ConfigValidator.ERRCODE_CONFIG_NOT_FOUND, "kie server config", configCode);
            }
            configDto = this.getKieServerConfigDtoBuilder().convert(config);
        } catch (RestRourceNotFoundException t) {
            throw t;
        } catch (Exception t) {
            logger.error("error in get configuration", t);
            throw new RestServerError("error in get configuration", t);
        }
        return configDto;
    }

    @Override
    public synchronized KieServerConfigDto addConfig(KieServerConfigDto configRequest, BindingResult bindingResult) {
        KieServerConfigDto configDto = null;
        try {
            KieBpmConfig newConfig = this.buildConfig(configRequest);
            this.getKieFormManager().addConfig(newConfig);
            try {
                this.getKieFormManager().getContainersList();
            } catch (Exception e) {
                logger.warn("Added configuration with invalid server connection - {}", configRequest);
                bindingResult.reject("schema", "Invalid configuration");
            }
            configDto = this.getKieServerConfigDtoBuilder().convert(newConfig);
        } catch (Exception t) {
            logger.error("error in post configuration", t);
            throw new RestServerError("error in post configuration", t);
        }
        return configDto;
    }

    protected KieBpmConfig buildConfig(KieServerConfigDto configDto) {
        KieBpmConfig config = new KieBpmConfig();
        config.setActive(configDto.getActive());
        config.setDebug(configDto.getDebug());
        config.setHostname(configDto.getHostName());
        config.setId(configDto.getId());
        config.setName(configDto.getName());
        config.setPassword(configDto.getPassword());
        config.setPort(configDto.getPort());
        config.setSchema(configDto.getSchema());
        config.setTimeoutMsec(configDto.getTimeout());
        config.setUsername(configDto.getUsername());
        config.setWebapp(configDto.getWebappName());
        return config;
    }

    @Override
    public KieServerConfigDto updateConfig(KieServerConfigDto configRequest, BindingResult bindingResult) {
        KieServerConfigDto configDto = null;
        try {
            Map<String, KieBpmConfig> map = this.getKieFormManager().getKieServerConfigurations();
            KieBpmConfig config = map.get(configRequest.getId());
            if (null == config) {
                throw new RestRourceNotFoundException(ConfigValidator.ERRCODE_CONFIG_NOT_FOUND, "kie bpm config", configRequest.getId());
            }
            KieBpmConfig newConfig = this.buildConfig(configRequest);
            this.getKieFormManager().setKieServerConfiguration(configRequest.getId());
            this.getKieFormManager().addConfig(newConfig);
            try {
                this.getKieFormManager().getContainersList();
            } catch (Exception e) {
                logger.warn("Modified configuration with invalid server connection - {}", configRequest);
                bindingResult.reject("schema", "Invalid configuration");
            }
            configDto = this.getKieServerConfigDtoBuilder().convert(newConfig);
        } catch (RestRourceNotFoundException t) {
            throw t;
        } catch (Exception t) {
            logger.error("error in put configuration", t);
            throw new RestServerError("error in put configuration", t);
        }
        return configDto;
    }

    @Override
    public void removeConfig(String configCode) {
        try {
            this.getKieFormManager().deleteConfig(configCode);
        } catch (Exception t) {
            logger.error("error in delete configuration", t);
            throw new RestServerError("error in delete configuration", t);
        }
    }

    @Override
    public String testServerConfigs(KieServerConfigDto configDto) {
        try {
            KieBpmConfig config = this.buildConfig(configDto);
            config.setActive(Boolean.TRUE);
            this.getKieFormManager().setConfig(config);
            this.getKieFormManager().getContainersList();
        } catch (Throwable t) {
            logger.error("error checking configuration", t);
            return "FAILURE";
        }
        return "SUCCESS";
    }

    @Override
    public Map<String, String> testAllServerConfigs() {
        Map<String, String> results = new HashMap<>();
        try {
            //Save the current Config
            KieBpmConfig setKieBpmConfig = this.getKieFormManager().getConfig();
            HashMap<String, KieBpmConfig> serverConfigurations = this.getKieFormManager().getKieServerConfigurations();
            for (KieBpmConfig config : serverConfigurations.values()) {
                this.getKieFormManager().setConfig(config);
                try {
                    this.getKieFormManager().getContainersList();
                    results.put(config.getId(), "SUCCESS");
                } catch (ApsSystemException e) {
                    logger.error("Configuration test {} failed!", config.getId(), e);
                    results.put(config.getId(), "FAILURE");
                }
            }
            //load the current config
            this.getKieFormManager().setConfig(setKieBpmConfig);
        } catch (Throwable t) {
            logger.error("error checking configuration", t);
            throw new RestServerError("error checking configuration", t);
        }
        return results;
    }

    public IKieFormManager getKieFormManager() {
        return kieFormManager;
    }

    public void setKieFormManager(IKieFormManager kieFormManager) {
        this.kieFormManager = kieFormManager;
    }

}
