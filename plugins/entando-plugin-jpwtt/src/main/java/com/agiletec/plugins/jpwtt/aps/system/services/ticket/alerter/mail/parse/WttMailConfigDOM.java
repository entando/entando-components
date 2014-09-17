/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.parse;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.model.MailTemplate;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.model.WttMailConfig;

/*
<wttMailConfig>
	<mail uniqueMail="true" senderCode="CODE1" mailAttrName="email" >
		<subject>Wtt Alert</subject>
		<templates>
			<template operation="0" descr="OPEN">
				<body type="admin"><![CDATA[Op0: Oggetto della mail admin]]></body>
				<body type="user"><![CDATA[Op0: Oggetto della mail user]]></body>
			</template>
			<template operation="1" descr="SETASSIGNABLE">
				<body type="admin"><![CDATA[Op1: Oggetto della mail admin]]></body>
				<body type="operator"><![CDATA[Op1: Oggetto della mail operator]]></body>
			</template>
			<template operation="2" descr="TAKEINCHARGE">
				<body type="admin"><![CDATA[Op2: Oggetto della mail admin]]></body>
				<body type="operator"><![CDATA[Op2: Oggetto della mail operator]]></body>
			</template>
			<template operation="3" descr="ANSWER">
				<body type="admin"><![CDATA[Op3: Oggetto della mail admin]]></body>
				<body type="user"><![CDATA[Op3: Oggetto della mail user]]></body>
				<body type="operator"><![CDATA[Op3: Oggetto della mail operator]]></body>
			</template>
			<template operation="4" descr="DISPATCH">
				<body type="admin"><![CDATA[Op4: Oggetto della mail admin]]></body>
				<body type="operator"><![CDATA[Op4: Oggetto della mail operator]]></body>
			</template>
			<template operation="5" descr="CLOSE">
				<body type="admin"><![CDATA[Op5: Oggetto della mail admin]]></body>
				<body type="user"><![CDATA[Op5: Oggetto della mail user]]></body>
				<body type="operator"><![CDATA[Op5: Oggetto della mail operator]]></body>
			</template>
			<template operation="6" descr="REOPEN">
				<body type="admin"><![CDATA[Op6: Oggetto della mail admin]]></body>
				<body type="user"><![CDATA[Op6: Oggetto della mail user]]></body>
				<body type="operator"><![CDATA[Op6: Oggetto della mail operator]]></body>
			</template>
		</templates>
	</mail>
	<commonAddresses>
		<admin>
			<!-- PUO ESSERE VUOTO -->
			<address>pippo@agiletec.it</address>
			<address>pluto@agiletec.it</address>
			.....
		</admin>
		<operator>
			<!-- PUO ESSERE VUOTO -->
			<address>topolino@agiletec.it</address>
			<address>minnie@agiletec.it</address>
			.....
		</operator>
	</commonAddresses>
	<interventionTypes>
		<interventionType id="1" >
			<admin>
				<!-- PUO ESSERE VUOTO -->
				<address>paperino@agiletec.it</address>
				<address>paperina@agiletec.it</address>
				.....
			</admin>
			<operator>
				<!-- PUO ESSERE VUOTO -->
				<address>qui@agiletec.it</address>
				<address>quo@agiletec.it</address>
				<address>qua@agiletec.it</address>
				.....
			</operator>
		</interventionType>
		<interventionType id="2">
		</interventionType>
	</interventionTypes>
</wttMailConfig>
*/

/**
 * Classe dom delegata alla lettura della configurazione del servizio di web trouble ticketing.
 * @version 1.0
 * @author E.Mezzano
 */
public class WttMailConfigDOM {
	
	/**
	 * Extract the wtt configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The wtt configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public WttMailConfig extractConfig(String xml) throws ApsSystemException {
		WttMailConfig config = new WttMailConfig();
		Element root = this.getRootElement(xml);
		this.extractMailConfig(root, config);
		return config;
	}
	
	private WttMailConfig extractMailConfig(Element root, WttMailConfig config) {
		Element mailElem = root.getChild(MAIL_ELEM);
		this.parseMailElement(mailElem, config);
		
		Element addressesElem = root.getChild(COMMON_ADDRESSES_ELEM);
		this.parseCommonAddressesElement(addressesElem, config);
		
		Element interventionTypesElement = root.getChild(INTERVENTION_TYPES_ELEM);
		this.parseInterventionTypesElement(interventionTypesElement, config);
		
		return config;
	}
	
	private void parseMailElement(Element mailElem, WttMailConfig config) {
		String uniqueMail = mailElem.getAttributeValue(MAIL_UNIQUEMAIL_ATTR);
		config.setUniqueMail("true".equalsIgnoreCase(uniqueMail));
		
		String senderCode = mailElem.getAttributeValue(MAIL_SENDERCODE_ATTR);
		config.setSenderCode(senderCode);
		
		String mailAttrName = mailElem.getAttributeValue(MAIL_MAILATTRNAME_ATTR);
		config.setMailAttrName(mailAttrName);
		
		config.setSubject(mailElem.getChildText(MAIL_SUBJECT_CHILD));
		
		Element templatesElem = mailElem.getChild(MAIL_TEMPLATES_CHILD);
		List<Element> templates = templatesElem.getChildren(MAIL_TEMPLATE_CHILD);
		for (Element templateElem : templates) {
			MailTemplate template = new MailTemplate();
			String operationString = templateElem.getAttributeValue(MAIL_TEMPLATE_OPERATION_ATTR);
			Integer operation = Integer.valueOf(operationString);
			template.setOperation(operationString);
			List<Element> bodyElements = templateElem.getChildren(MAIL_TEMPLATE_BODY_CHILD);
			for (Element bodyElement : bodyElements) {
				String type = bodyElement.getAttributeValue(MAIL_TEMPLATE_BODY_TYPE_ATTR);
				String body = bodyElement.getText();
				template.addTemplateBody(type, body);
			}
			config.addTemplate(operation, template);
		}
	}
	
	private void parseCommonAddressesElement(Element addressesElem, WttMailConfig config) {
		Element adminElement = addressesElem.getChild(ADMIN_ADDRESSES_CHILD);
		if (adminElement!=null) {
			config.setCommonAdminAddresses(this.extractMailAddresses(adminElement));
		}
		Element operatorElement = addressesElem.getChild(OPERATOR_ADDRESSES_CHILD);
		if (operatorElement!=null) {
			config.setCommonOperatorAddresses(this.extractMailAddresses(operatorElement));
		}
	}
	
	private void parseInterventionTypesElement(Element interventionTypesElem, WttMailConfig config) {
		List<Element> interventionTypeElements = interventionTypesElem.getChildren(INTERVENTION_TYPE_CHILD);
		for (Element interventionTypeElem : interventionTypeElements) {
			Integer id = new Integer(interventionTypeElem.getAttributeValue(INTERVENTION_TYPE_ID_ATTR));
			Element adminElement = interventionTypeElem.getChild(ADMIN_ADDRESSES_CHILD);
			if (adminElement!=null) {
				config.addIntervTypeAdminAddresses(id, this.extractMailAddresses(adminElement));
			}
			Element operatorElement = interventionTypeElem.getChild(OPERATOR_ADDRESSES_CHILD);
			if (operatorElement!=null) {
				config.addIntervTypeOperatorAddresses(id, this.extractMailAddresses(operatorElement));
			}
		}
	}
	
	private List<String> extractMailAddresses(Element addressesElem) {
		List<String> destAddresses = new ArrayList<String>();
		if (addressesElem!=null) {
			List<Element> addressesElements = addressesElem.getChildren(ADDRESSES_ADDRESS_CHILD);
			if (addressesElements!=null) {
				for (Element addressElem : addressesElements) {
					String mailAddress = addressElem.getText();
					destAddresses.add(mailAddress);
				}
			}
		}
		return destAddresses;
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
	
	private static final String ROOT = "wttMailConfig";
	
	private static final String MAIL_ELEM = "mail";
	private static final String MAIL_UNIQUEMAIL_ATTR = "uniqueMail";
	private static final String MAIL_SENDERCODE_ATTR = "senderCode";
	private static final String MAIL_MAILATTRNAME_ATTR = "mailAttrName";
	private static final String MAIL_SUBJECT_CHILD = "subject";
	private static final String MAIL_TEMPLATES_CHILD = "templates";
	private static final String MAIL_TEMPLATE_CHILD = "template";
	private static final String MAIL_TEMPLATE_OPERATION_ATTR = "operation";
	private static final String MAIL_TEMPLATE_BODY_CHILD = "body";
	private static final String MAIL_TEMPLATE_BODY_TYPE_ATTR = "type";
	
	private static final String COMMON_ADDRESSES_ELEM = "commonAddresses";
	private static final String ADMIN_ADDRESSES_CHILD = "admin";
	private static final String OPERATOR_ADDRESSES_CHILD = "operator";
	
	private static final String INTERVENTION_TYPES_ELEM = "interventionTypes";
	private static final String INTERVENTION_TYPE_CHILD = "interventionType";
	private static final String INTERVENTION_TYPE_ID_ATTR = "id";
	
	private static final String ADDRESSES_ADDRESS_CHILD = "address";
	
}