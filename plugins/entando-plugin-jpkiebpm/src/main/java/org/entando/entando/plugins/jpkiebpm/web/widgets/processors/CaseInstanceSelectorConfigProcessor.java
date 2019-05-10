/*
 * The MIT License
 *
 * Copyright 2019 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.web.widgets.processors;

import java.util.Map;

import com.agiletec.aps.util.ApsProperties;
import java.util.HashMap;
import java.util.List;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetConfigurationProcessor;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaseInstanceSelectorConfigProcessor implements WidgetConfigurationProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(CaseInstanceSelectorConfigProcessor.class);
    
    public static final String WIDGET_CODE = "bpm-case-instance-selector";

    private static final String WIDGET_CONFIG_CHANNEL_KEY = "channel";
    private static final String WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY = "knowledge-source-id";
    private static final String WIDGET_CONFIG_FRONTEND_CASE_DATA_KEY = "frontEndCaseData";
    private static final String WIDGET_CONFIG_DEPLOYMENT_UNIT_KEY = "container-id";
       
    private static final String APP_BUILDER_WIDGET_CONFIG_CHANNEL_KEY = "channel";    
    private static final String APP_BUILDER_WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY = "knowledgeSourcePath";
    private static final String APP_BUILDER_WIDGET_CONFIG_DEPLOYMENT_UNIT_KEY = "processPath";

    @Override
    public boolean supports(String widgetCode) {
        return WIDGET_CODE.equals(widgetCode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object buildConfiguration(WidgetConfigurationRequest widget,
                                     Map<String, Object> parameters) {
        logger.info("Build configuration for {}",WIDGET_CODE);

        ApsProperties properties = new ApsProperties();

        Map<String,String> widgetProperties  = new HashMap<>();
        
        Map<String, Object> map = widget.getConfig();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            logger.debug("Widget Property found: k {} v {}", entry.getKey() , entry.getValue().toString());
            widgetProperties.put(entry.getKey(), entry.getValue().toString());
        }
        
        JSONObject frontEndCaseData = new JSONObject();
        
        if (widgetProperties.containsKey(APP_BUILDER_WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY)) {
            frontEndCaseData.put(WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY, widgetProperties.get(APP_BUILDER_WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY));
        }
 
        if (widgetProperties.containsKey(APP_BUILDER_WIDGET_CONFIG_DEPLOYMENT_UNIT_KEY)){
            frontEndCaseData.put(WIDGET_CONFIG_DEPLOYMENT_UNIT_KEY, widgetProperties.get(APP_BUILDER_WIDGET_CONFIG_DEPLOYMENT_UNIT_KEY));
        }
     
        properties.put(WIDGET_CONFIG_FRONTEND_CASE_DATA_KEY, frontEndCaseData.toString());
        if (widgetProperties.containsKey(APP_BUILDER_WIDGET_CONFIG_CHANNEL_KEY)) {
            properties.put(WIDGET_CONFIG_CHANNEL_KEY, widgetProperties.get(APP_BUILDER_WIDGET_CONFIG_CHANNEL_KEY));
        }
        else {       
            properties.put(WIDGET_CONFIG_CHANNEL_KEY, "1");
        }
        
        logger.debug("Build configuration return properties {}", properties);

        return properties;
    }

    @Override
    public ApsProperties extractConfiguration(ApsProperties widgetProperties) {
        logger.info("Extract configuration for {}", WIDGET_CODE);
        ApsProperties properties = new ApsProperties();       
        String frontendCaseData="";
        if (widgetProperties.containsKey(WIDGET_CONFIG_FRONTEND_CASE_DATA_KEY)) {
            frontendCaseData=(String) widgetProperties.get(WIDGET_CONFIG_FRONTEND_CASE_DATA_KEY);
        }   
        JSONObject frontendCaseDataJson= new JSONObject(frontendCaseData);      
        properties.put(APP_BUILDER_WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY, frontendCaseDataJson.getString(WIDGET_CONFIG_KNOWLEDGE_SOURCE_KEY));       
        properties.put(APP_BUILDER_WIDGET_CONFIG_DEPLOYMENT_UNIT_KEY, frontendCaseDataJson.getString(WIDGET_CONFIG_DEPLOYMENT_UNIT_KEY));
        if (widgetProperties.containsKey(WIDGET_CONFIG_CHANNEL_KEY)){
            properties.put(APP_BUILDER_WIDGET_CONFIG_CHANNEL_KEY, widgetProperties.get(WIDGET_CONFIG_CHANNEL_KEY));
        }
        logger.debug("Extract configuration return properties {}", properties);
        return  properties;
    }

}
