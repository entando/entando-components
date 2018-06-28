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

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 * @deprecated Used to guarantee porting with previous versions of the plugin
 */
public class SeoComplexParameterDOM {

    private static final Logger logger = LoggerFactory.getLogger(SeoComplexParameterDOM.class);

    public String extractXml(Map<String, Map<String, PageMetatag>> parameters) {
        Document doc = new Document();
        Element elementRoot = new Element("seoparameters");
        doc.setRootElement(elementRoot);
        SeoPageExtraConfigDOM configDom = new SeoPageExtraConfigDOM();
        configDom.addComplexParameters(elementRoot, parameters);
        return this.getXMLDocument(doc);
    }

    private String getXMLDocument(Document doc) {
        XMLOutputter out = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        out.setFormat(format);
        return out.outputString(doc);
    }

    public Map<String, Map<String, PageMetatag>> extractComplexParameters(String xmlConfig) {
        if (StringUtils.isBlank(xmlConfig)) {
            return new HashMap<>();
        }
        Document doc = this.decodeComplexParameterDOM(xmlConfig);
        List<Element> elements = doc.getRootElement().getChildren();
        return new SeoPageExtraConfigDOM().extractComplexParameters(elements);
    }

    private Document decodeComplexParameterDOM(String xml) {
        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        StringReader reader = new StringReader(xml);
        try {
            doc = builder.build(reader);
        } catch (Throwable t) {
            logger.error("Error while parsing xml: {}", xml, t);
        }
        return doc;
    }

}
