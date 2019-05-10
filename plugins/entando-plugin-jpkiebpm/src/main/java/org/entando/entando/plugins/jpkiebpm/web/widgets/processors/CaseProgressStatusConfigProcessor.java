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
import org.entando.entando.aps.system.services.widgettype.validators.WidgetConfigurationProcessor;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaseProgressStatusConfigProcessor implements WidgetConfigurationProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(CaseInstanceSelectorConfigProcessor.class);
    
    public static final String WIDGET_CODE = "bpm-case-progress-status";
   
    @Override
    public boolean supports(String widgetCode) {
        return WIDGET_CODE.equals(widgetCode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object buildConfiguration(WidgetConfigurationRequest widget, Map<String, Object> parameters) {
        logger.info("Build configuration for {}",WIDGET_CODE);
        ApsProperties properties = new ApsProperties();
        Map<String, Object> map = widget.getConfig();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            logger.debug("Widget Property found: k {} v {}", entry.getKey() , entry.getValue().toString());
            properties.put(entry.getKey(), entry.getValue().toString());
        }
        logger.debug("Build configuration return properties {}", properties);
        return properties;
    }

    @Override
    public ApsProperties extractConfiguration(ApsProperties widgetProperties) {
        return  widgetProperties;
    }


}
