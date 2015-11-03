/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpavatar.aps.system.services.avatar.parse;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.AvatarConfig;

import java.io.StringReader;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Puddu
 */
public class AvatarConfigDOM {
	
	private static final Logger _logger = LoggerFactory.getLogger(AvatarConfigDOM.class);
	
	/**
	 * Extract the service configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The service configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public AvatarConfig extractConfig(String xml) throws ApsSystemException {
		AvatarConfig config = new AvatarConfig();
		Element root = this.getRootElement(xml);
		Element styleElem = root.getChild(STYLE_ELEM);
		String style = null;
		if (styleElem != null) {
			style = styleElem.getText();
		}
		if (StringUtils.isBlank(style) || !ArrayUtils.contains(AvatarConfig.STYLES, style)) {
			style = AvatarConfig.STYLE_LOCAL;
		}
		config.setStyle(style);
		return config;
	}
	
	/**
	 * Extract the configuration from the xml element and save it into the AvatarConfig object.
	 * @param root The xml root element containing the smtp configuration.
	 * @param config The configuration.
	 */
	private Element createConfigElement(AvatarConfig config) {
		Element configElem = new Element(ROOT);
		Element styleElement = new Element(STYLE_ELEM);
		styleElement.setText(config.getStyle());
		configElem.addContent(styleElement);
		return configElem;
	}
	
	/**
	 * Create an xml containing the jpavatar configuration.
	 * @param config The jpavatar configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(AvatarConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
	
	/**
	 * Returns the Xml element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException In case of parsing exceptions.
	 */
	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			_logger.error("Error parsing xml ", t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}
	
	private final String ROOT = "jpavatar";
	private final String STYLE_ELEM = "style";
	
}
