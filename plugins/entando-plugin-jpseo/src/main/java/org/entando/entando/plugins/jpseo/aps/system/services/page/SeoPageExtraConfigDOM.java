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
                List<Element> elements = complexParamElement.getChildren();
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
    private Map<String, Map<String, PageMetatag>> extractComplexParameters(String xmlConfig) {
        Document doc = this.decodeComplexParameterDOM(xmlConfig);
        List<Element> elements = doc.getRootElement().getChildren();
        return this.extractComplexParameters(elements);
    }
    
    /*
.....
    OLD STRUCTURE
  <complexParameters>
    <parameter key="key1">VALUE_1</parameter>
    <parameter key="key2">VALUE_2</parameter>
    <parameter key="key5">
      <property key="fr">VALUE_5 FR</property>
      <property key="en">VALUE_5 EN</property>
      <property key="it">VALUE_5 IT</property>
    </parameter>
    <parameter key="key6">VALUE_6</parameter>
    <parameter key="key3">
      <property key="en">VALUE_3 EN</property>
      <property key="it">VALUE_3 IT</property>
    </parameter>
    <parameter key="key4">VALUE_4</parameter>
  </complexParameters>
.....
    */
    
    /*
    New Structure
.....
  <complexParameters>
    <lang code="it">
      <meta key="key5">VALUE_5_IT</meta>
      <meta key="key3" attributeName="name" useDefaultLang="false" >VALUE_3_IT</meta>
      <meta key="key2" attributeName="property" useDefaultLang="true" />
    </lang>
    <lang code="en">
      <meta key="key5">VALUE_5_IT</meta>
      <meta key="key3" attributeName="name" useDefaultLang="false" >VALUE_3_EN</meta>
      <meta key="key2" attributeName="property" useDefaultLang="true" />
    </lang>
    ...
    ...
  </complexParameters>
.....
    */
    
    private Map<String, PageMetatag> extractLangMap(String code, 
            Map<String, Map<String, PageMetatag>> complexParameters) {
        Map<String, PageMetatag> langMap = complexParameters.get(code);
        if (null == langMap) {
            langMap = new HashMap<>();
            complexParameters.put(code, langMap);
        }
        return langMap;
    }
    
    protected Map<String, Map<String, PageMetatag>> extractComplexParameters(List<Element> elements) {
        Map<String, Map<String, PageMetatag>> complexParameters = new HashMap<>();
        if (null == elements) {
            return complexParameters;
        }
        for (Element paramElement : elements) {
            String elementName = paramElement.getName();
            if (elementName.equals("parameter")) {
                //Used to guarantee porting with previous versions of the plugin
                String key = paramElement.getAttributeValue("key");
                List<Element> langElements = paramElement.getChildren("property");
                if (null != langElements && langElements.size() > 0) {
                    for (Element langElement : langElements) {
                        String langCode = langElement.getAttributeValue("key");
                        Map<String, PageMetatag> langMap = this.extractLangMap(langCode, complexParameters);
                        PageMetatag metatag = new PageMetatag(langCode, key, langElement.getText());
                        langMap.put(key, metatag);
                    }
                } else {
                    Map<String, PageMetatag> defaultLang = this.extractLangMap("default", complexParameters);
                    PageMetatag metatag = new PageMetatag("default", key, paramElement.getText());
                    defaultLang.put(key, metatag);
                }
            } else if (elementName.equals("lang")) {
                String langCode = paramElement.getAttributeValue("code");
                Map<String, PageMetatag> langMap = this.extractLangMap(langCode, complexParameters);
                List<Element> langElements = paramElement.getChildren("meta");
                for (Element langElement : langElements) {
                    String key = langElement.getAttributeValue("key");
                    PageMetatag metatag = new PageMetatag(langCode, key, langElement.getText());
                    metatag.setKeyAttribute(langElement.getAttributeValue("attributeName"));
                    String useDefaultLang = langElement.getAttributeValue("useDefaultLang");
                    metatag.setUseDefaultLangValue(Boolean.parseBoolean(useDefaultLang));
                    langMap.put(key, metatag);
                }
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

    protected void addComplexParameters(Element elementRoot, Map<String, Map<String, PageMetatag>> parameters) {
        if (null == parameters) {
            return;
        }
        Iterator<String> iter1 = parameters.keySet().iterator();
        while (iter1.hasNext()) {
            String langCode = iter1.next();
            Map<String, PageMetatag> metas = parameters.get(langCode);
            if (langCode.equals("default")) {
                Iterator<String> iter2 = metas.keySet().iterator();
                while (iter2.hasNext()) {
                    String key2 = iter2.next();
                    Element parameterElement = new Element("parameter");
                    PageMetatag metatag = metas.get(key2);
                    parameterElement.setAttribute("key", metatag.getKey());
                    parameterElement.setText(metatag.getValue());
                    elementRoot.addContent(parameterElement);
                }
            } else {
                Element langElement = new Element("lang");
                langElement.setAttribute("code", langCode);
                Iterator<String> iter2 = metas.keySet().iterator();
                while (iter2.hasNext()) {
                    String key2 = iter2.next();
                    Element metaElement = new Element("meta");
                    PageMetatag metatag = metas.get(key2);
                    metaElement.setAttribute("key", metatag.getKey());
                    metaElement.setAttribute("attributeName", metatag.getKeyAttribute());
                    metaElement.setAttribute("useDefaultLang", String.valueOf(metatag.isUseDefaultLangValue()));
                    metaElement.setText(metatag.getValue());
                    langElement.addContent(metaElement);
                }
                elementRoot.addContent(langElement);
            }
        }
    }

}
