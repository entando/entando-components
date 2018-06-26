/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.page;

import com.agiletec.aps.system.services.page.PageExtraConfigDOM;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsProperties;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dom class for parse the xml of extra page config
 *
 * @author E.Santoboni
 */
public class SeoPageExtraConfigDOM extends PageExtraConfigDOM {

    private static final Logger _logger = LoggerFactory.getLogger(SeoPageExtraConfigDOM.class);

    private static final String USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME = "useextradescriptions";
    private static final String DESCRIPTIONS_ELEMENT_NAME = "descriptions";
    private static final String DESCRIPTION_ELEMENT_NAME = "property";
    private static final String DESCRIPTION_LANG_ATTRIBUTE_NAME = "key";

    private static final String FRIENDLY_CODE_ELEMENT_NAME = "friendlycode";

    @Deprecated
    private static final String XML_CONFIG_ELEMENT_NAME = "xmlConfig";

    private static final String COMPLEX_PARAMS_ELEMENT_NAME = "complexParameters";

    @Override
    protected void addExtraConfig(PageMetadata pageMetadata, Document doc) {
        super.addExtraConfig(pageMetadata, doc);
        if (!(pageMetadata instanceof SeoPageMetadata)) {
            return;
        }
        Element root = doc.getRootElement();
        SeoPageMetadata seoPage = (SeoPageMetadata) pageMetadata;
        Element useExtraDescriptionsElement = root.getChild(USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME);
        if (null != useExtraDescriptionsElement) {
            Boolean value = Boolean.valueOf(useExtraDescriptionsElement.getText());
            seoPage.setUseExtraDescriptions(value.booleanValue());
        }
        Element descriptionsElement = root.getChild(DESCRIPTIONS_ELEMENT_NAME);
        if (null != descriptionsElement) {
            List<Element> descriptionElements = descriptionsElement.getChildren(DESCRIPTION_ELEMENT_NAME);
            for (int i = 0; i < descriptionElements.size(); i++) {
                Element descriptionElement = descriptionElements.get(i);
                String langCode = descriptionElement.getAttributeValue(DESCRIPTION_LANG_ATTRIBUTE_NAME);
                String description = descriptionElement.getText();
                seoPage.getDescriptions().put(langCode, description);
            }
        }
        Element friendlyCodeElement = root.getChild(FRIENDLY_CODE_ELEMENT_NAME);
        if (null != friendlyCodeElement) {
            seoPage.setFriendlyCode(friendlyCodeElement.getText());
        }
        Element xmlConfigElement = root.getChild(XML_CONFIG_ELEMENT_NAME);
        if (null != xmlConfigElement) {
            String xml = xmlConfigElement.getText();
            seoPage.setComplexParameters(this.extractComplexParameters(xml));
        } else {
            Element complexParamElement = root.getChild(COMPLEX_PARAMS_ELEMENT_NAME);
            if (null != complexParamElement) {
                List<Element> elements = complexParamElement.getChildren("parameter");
                seoPage.setComplexParameters(this.extractComplexParameters(elements));
            }
        }
    }

    /**
     * Extract the complex parameters from string
     *
     * @param xmlConfig the config
     * @return the map of complex parameters
     * @deprecated Used to guarantee porting with previous versions of the
     * plugin
     */
    private Map<String, Object> extractComplexParameters(String xmlConfig) {
        Document doc = this.decodeComplexParameterDOM(xmlConfig);
        List<Element> elements = doc.getRootElement().getChildren("parameter");
        return this.extractComplexParameters(elements);
    }

    private Map<String, Object> extractComplexParameters(List<Element> elements) {
        Map<String, Object> complexParameters = new HashMap<>();
        if (null == elements) {
            return complexParameters;
        }
        for (int i = 0; i < elements.size(); i++) {
            Element paramElement = elements.get(i);
            String paramKey = paramElement.getAttributeValue("key");
            List<Element> langElements = paramElement.getChildren("property");
            if (null != langElements && langElements.size() > 0) {
                ApsProperties properties = new ApsProperties();
                for (int j = 0; j < langElements.size(); j++) {
                    Element langElement = langElements.get(j);
                    String propertyKey = langElement.getAttributeValue("key");
                    String propertyValue = langElement.getText();
                    properties.setProperty(propertyKey, propertyValue);
                }
                complexParameters.put(paramKey, properties);
            } else {
                String paramValue = paramElement.getText();
                complexParameters.put(paramKey, paramValue);
            }
        }
        return complexParameters;
    }

    private Document decodeComplexParameterDOM(String xml) {
        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        StringReader reader = new StringReader(xml);
        try {
            doc = builder.build(reader);
        } catch (Throwable t) {
            _logger.error("Error while parsing xml: {}", xml, t);
        }
        return doc;
    }

    @Override
    protected void fillDocument(Document doc, PageMetadata pageMetadata) {
        super.fillDocument(doc, pageMetadata);
        if (!(pageMetadata instanceof SeoPageMetadata)) {
            return;
        }
        SeoPageMetadata seoPageMetadata = (SeoPageMetadata) pageMetadata;
        Element useExtraDescriptionsElement = new Element(USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME);
        useExtraDescriptionsElement.setText(String.valueOf(seoPageMetadata.isUseExtraDescriptions()));
        doc.getRootElement().addContent(useExtraDescriptionsElement);
        ApsProperties descriptions = seoPageMetadata.getDescriptions();
        if (null != descriptions && descriptions.size() > 0) {
            Element descriptionsElement = new Element(DESCRIPTIONS_ELEMENT_NAME);
            doc.getRootElement().addContent(descriptionsElement);
            Iterator<Object> iterator = descriptions.keySet().iterator();
            while (iterator.hasNext()) {
                String langCode = (String) iterator.next();
                Element extraDescriptionElement = new Element(DESCRIPTION_ELEMENT_NAME);
                extraDescriptionElement.setAttribute(DESCRIPTION_LANG_ATTRIBUTE_NAME, langCode);
                extraDescriptionElement.setText(descriptions.getProperty(langCode));
                descriptionsElement.addContent(extraDescriptionElement);
            }
        }
        if (null != seoPageMetadata.getFriendlyCode() && seoPageMetadata.getFriendlyCode().trim().length() > 0) {
            Element friendlyCodeElement = new Element(FRIENDLY_CODE_ELEMENT_NAME);
            friendlyCodeElement.setText(seoPageMetadata.getFriendlyCode().trim());
            doc.getRootElement().addContent(friendlyCodeElement);
        }
        if (null != seoPageMetadata.getComplexParameters()) {
            Element complexConfigElement = new Element(COMPLEX_PARAMS_ELEMENT_NAME);
            this.addComplexParameters(complexConfigElement, seoPageMetadata.getComplexParameters());
            doc.getRootElement().addContent(complexConfigElement);
        }
    }

    protected void addComplexParameters(Element elementRoot, Map<String, Object> parameters) {
        if (null == parameters) {
            return;
        }
        Iterator<String> iter1 = parameters.keySet().iterator();
        while (iter1.hasNext()) {
            String key = iter1.next();
            Element parameterElement = new Element("parameter");
            parameterElement.setAttribute("key", key);
            Object value = parameters.get(key);
            if (value instanceof Properties) {
                Properties properties = (Properties) value;
                Iterator<Object> iter2 = properties.keySet().iterator();
                while (iter2.hasNext()) {
                    Object key2 = iter2.next();
                    Element propertyElement = new Element("property");
                    propertyElement.setAttribute("key", key2.toString());
                    propertyElement.setText(properties.get(key2).toString());
                    parameterElement.addContent(propertyElement);
                }
            } else {
                parameterElement.setText(value.toString());
            }
            elementRoot.addContent(parameterElement);
        }
    }

}
