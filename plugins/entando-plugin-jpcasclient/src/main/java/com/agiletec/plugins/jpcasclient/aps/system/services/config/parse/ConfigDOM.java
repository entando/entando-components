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
package com.agiletec.plugins.jpcasclient.aps.system.services.config.parse;

import java.io.StringReader;
import java.util.List;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;

/*
<?xml version="1.0" encoding="UTF-8"?>
<casclientConfig>
	<active>false</active>
	<casLoginURL>http://japs.intranet:8080/cas/login</casLoginURL>
	<casLogoutURL>http://japs.intranet:8080/cas/logout</casLogoutURL>
	<casValidateURL>http://japs.intranet:8080/cas/validate</casValidateURL>
	<serverBaseURL>http://japs.intranet:8080</serverBaseURL>
	<notAuthPage>notauth</notAuthPage>
	<realm>demo.entando.com</realm>
</casclientConfig>
 */

/**
 * Class that provides read and update operations for the jpmail plugin xml configuration.
 * 
 * @version 1.0
 * @author E.Santoboni, E.Mezzano
 */
public class ConfigDOM {
	
	/**
	 * Extract the jpmail configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The jpmail configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public CasClientConfig extractConfig(String xml) throws ApsSystemException {
		CasClientConfig config = new CasClientConfig();
		Element root = this.getRootElement(xml);
		this.extractParams(root, config);
		return config;
	}
	
	private void extractParams(Element root, CasClientConfig config) {

		Element activeElement = root.getChild(ACTIVE);
		String text = activeElement.getText();
		if ("true".equals(text)) {
			config.setActive(true);
		} else {
			config.setActive(false);
		}

		activeElement = root.getChild(CAS_LOGIN_URL);
		text = activeElement.getText();
		config.setCasLoginURL(text);
		
		activeElement = root.getChild(CAS_LOGOUT_URL);
		text = activeElement.getText();
		config.setCasLogoutURL(text);
		
		activeElement = root.getChild(CAS_VALIDATE_URL);
		text = activeElement.getText();
		config.setCasValidateURL(text);
		
		activeElement = root.getChild(SERVER_BASE_URL);
		text = activeElement.getText();
		config.setServerBaseURL(text);
		
		activeElement = root.getChild(NOT_AUTH_PAGE);
		text = activeElement.getText();
		config.setNotAuthPage(text);
		
		activeElement = root.getChild(REALM);
		text = activeElement.getText();
		config.setRealm(text);
	}

	/**
	 * Create an xml containing the jpmail configuration.
	 * @param config The jpmail configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(CasClientConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}


	
	/**
	 * Extract the smtp configuration from the xml element and save it into the MailConfig object.
	 * @param root The xml root element containing the smtp configuration.
	 * @param config The configuration.
	 */
	private Element createConfigElement(CasClientConfig config) {
		
		Element configElem = new Element(ROOT);
		Element activeElem = new Element(ACTIVE);
		activeElem.setText(String.valueOf(config.isActive()));
		configElem.addContent(activeElem);

		Element casLoginURL = new Element(CAS_LOGIN_URL);
		casLoginURL.setText(config.getCasLoginURL());
		configElem.addContent(casLoginURL);
		
		Element casLogoutURL = new Element(CAS_LOGOUT_URL);
		casLogoutURL.setText(config.getCasLogoutURL());
		configElem.addContent(casLogoutURL);
		
		Element casValidateURL = new Element(CAS_VALIDATE_URL);
		casValidateURL.setText(config.getCasValidateURL());
		configElem.addContent(casValidateURL);
		
		Element serverBaseURL = new Element(SERVER_BASE_URL);
		serverBaseURL.setText(config.getServerBaseURL());
		configElem.addContent(serverBaseURL);
		
		Element notAuthPage = new Element(NOT_AUTH_PAGE);
		notAuthPage.setText(config.getNotAuthPage());
		configElem.addContent(notAuthPage);
		
		Element realm = new Element(REALM);
		realm.setText(config.getRealm());
		configElem.addContent(realm);
		
		return configElem;
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
			ApsSystemUtils.getLogger().error("Error parsing xml: " + t.getMessage());
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}
	
	private final String ROOT = "casclientConfig";
	
	private final String ACTIVE = "active";
	private final String CAS_LOGIN_URL = "casLoginURL";
	private final String CAS_LOGOUT_URL = "casLogoutURL";
	private final String CAS_VALIDATE_URL = "casValidateURL";
	private final String SERVER_BASE_URL = "serverBaseURL";
	private final String NOT_AUTH_PAGE = "notAuthPage";
	private final String REALM = "realm";
	
}