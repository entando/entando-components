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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.config.parse;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;

/**
 * This class parses the configuration of jpmyportal
 * @author E.Santoboni - E. Mezzano
 */
public class MyPortalPlusConfigDOM {

	public MyPortalConfig extractConfig(String xml) throws ApsSystemException {
		Element root = this.getRootElement(xml);
		MyPortalConfig config = new MyPortalConfig();
		this.extractWidgetConfig(root, config);
		return config;
	}

	/**
	 * Create an xml containing the MyPortal configuration.
	 * @param config The MyPortal configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(MyPortalConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}

	private void extractWidgetConfig(Element root, MyPortalConfig config) {
		Element element = root.getChild(ALLOWED_WIDGETS_ELEM);
		if (null == element) {
			element = root.getChild(ALLOWED_SHOWLETS_ELEM);
		}
		Set<String> allowedWidgets = new TreeSet<String>();
		if (null != element) {
			List<Element> codeElements = element.getChildren(WIDGET_ELEM);
			if (null == codeElements || codeElements.isEmpty()) {
				codeElements = element.getChildren(SHOWLET_ELEM);
			}
			if (null != codeElements) {
				for (Element codeElem : codeElements) {
					String code = codeElem.getAttributeValue(WIDGET_CODE_ATTR);
					allowedWidgets.add(code);
				}
			}
		}
		config.setAllowedShowlets(allowedWidgets);
	}

	/**
	 * Create the configuration element for the myportal service.
	 * @param config The configuration of myportal service
	 * @return the configuration of this plugin.
	 */
	private Element createConfigElement(MyPortalConfig config) {
		Element configElem = new Element(ROOT);
		Element allowedShowletsElem = this.createShowletsElement(config);
		configElem.addContent(allowedShowletsElem);
		return configElem;
	}

	private Element createShowletsElement(MyPortalConfig config) {
		Element showletsElement = new Element(ALLOWED_WIDGETS_ELEM);
		Set<String> showlets = config.getAllowedShowlets();
		if (null != showlets && showlets.size() > 0) {
			Iterator<String> iter = showlets.iterator();
			while (iter.hasNext()) {
				String showletCode = iter.next();
				Element showletElem = new Element(WIDGET_ELEM);
				showletElem.setAttribute(WIDGET_CODE_ATTR, showletCode);
				showletsElement.addContent(showletElem);
			}
		}
		return showletsElement;
	}

	/**
	 * Returns the XML element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The XML element from a given text.
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
			ApsSystemUtils.getLogger().error("Error parsing xml: " + t.getMessage());
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}

	private static final String ROOT = "myportalConfig";
	
	@Deprecated
	private static final String ALLOWED_SHOWLETS_ELEM = "showlets";
	private static final String ALLOWED_WIDGETS_ELEM = "widgets";
	
	@Deprecated
	private static final String SHOWLET_ELEM = "showlet";
	private static final String WIDGET_ELEM = "widget";
	
	private static final String WIDGET_CODE_ATTR = "code";

}