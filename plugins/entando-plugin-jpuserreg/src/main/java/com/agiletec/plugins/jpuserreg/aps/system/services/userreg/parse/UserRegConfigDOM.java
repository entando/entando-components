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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg.parse;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.IUserRegConfig;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.Template;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.UserRegConfig;

/*
 * TODO
 * 

<reactivationQuestions>
	<question code="0" >
		<lang code="it" >Qual'è il tuo colore preferito?</lang>
		<lang code="en" >What's your prefered colour?</lang>
	</question>
	<question code="1" >
		<lang code="it" >Qual'è il tuo animale preferito?</lang>
		<lang code="en" >What's your prefered pet?</lang>
	</question>
</reactivationQuestions>

 * 
 * */
/**
 * DOM for plugin config 
 * 
 * 
 * 
 
 <regProfileConfig>
	<profileEntity nameAttr="Nome" surnameAttr="Cognome" eMailAttr="eMail" />
	
	<!-- Token validity in minutes - Activation page name -->
	<tokenValidity minutes="60"/>
	
	<!-- Sender code, as in mailConfig -->
	<sender code="CODE1" />
	
	<!-- Authorities to load on user request profile -->
	<userAuthDefaults>
		<authotization group="coach" role="editor" />
		<authotization group="customers" role="supervisor" />
	</userAuthDefaults>
	
	<!-- Activation page name -->
	<activation pageCode="attivazione">
		<template lang="it">
			<subject>[Entando] : Attivazione utente</subject>
			<body>
Gentile {name} {surname}, 
grazie per esserti registrato.
Per attivare il tuo account è necessario seguire il seguente link: 
{link}
Cordiali Saluti.
			</body>
		</template>
	</activation>
	
	<!-- Activation page name -->
	<reactivation pageCode="riattivazione">
		<template lang="it">
			<subject>[Entando] : Riattivazione utente</subject>
			<body>
Gentile {name} {surname}, 
il tuo userName è {userName}.
Per riattivare il tuo account è necessario seguire il seguente link: 
{link}
Cordiali Saluti.
			</body>
		</template>
	</reactivation>
	<reactivationQuestions>
		<question code="0" >
			<lang code="it" >Qual'è il tuo colore preferito?</lang>
			<lang code="en" >What's your prefered colour?</lang>
		</question>
		<question code="1" >
			<lang code="it" >Qual'è il tuo animale preferito?</lang>
			<lang code="en" >What's your prefered pet?</lang>
		</question>
	</reactivationQuestions>
</regProfileConfig>
 * 
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 */
public class UserRegConfigDOM {
	
	/**
	 * Extract the jpuserreg configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The jpuserreg configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public UserRegConfig extractConfig(String xml) throws ApsSystemException {
		Document doc = this.decodeDOM(xml);
		Element root = doc.getRootElement();
		UserRegConfig config = new UserRegConfig();
		this.extractTokenValidityConfig(root, config);
		this.extractMailSenderConfig(root, config);
		this.extractActivationMailConfig(root, config);
		this.extractReactivationMailConfig(root, config);
		this.extractUserAuthDefaults(root, config);
		return config;
	}
	
	/**
	 * Create an xml containing the jpuserreg configuration.
	 * @param config The jpuserreg configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(IUserRegConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}
	
	protected void extractUserAuthDefaults(Element root, IUserRegConfig config) {
		Element userAuths = root.getChild(USER_DEFAULT_AUTHS);
		if (null != userAuths) {
			List<Element> auths = userAuths.getChildren(USER_DEFAULT_AUTH_ELEM);
			Iterator<Element> it = auths.iterator();
			while (it.hasNext()) {
				Element current = (Element) it.next();
				String groupName = current.getAttributeValue(USER_AUTH_GROUP_ATTR);
				String roleName = current.getAttributeValue(USER_AUTH_ROLE_ATTR);
				if (null == roleName) roleName = "";
				String csv = groupName + "," + roleName;
				config.addDefaultCsvAuthorization(csv);
			}
		}
	}
	
	protected void extractTokenValidityConfig(Element root, IUserRegConfig config) {
		Element tokenElem = root.getChild(TOKEN_VALIDITY);
		config.setTokenValidityMinutes(Long.parseLong(tokenElem.getAttributeValue(TOKEN_VALIDITY_MINUTES_ATTR)));
	}
	
	protected void extractMailSenderConfig(Element root, IUserRegConfig config) {
		Element tokenElem = root.getChild(MAIL_SENDER);
		config.setEMailSenderCode(tokenElem.getAttributeValue(MAIL_SENDER_CODE_ATTR));
	}
	
	protected void extractActivationMailConfig(Element root, IUserRegConfig config) {
		Element activationElem = root.getChild(ACTIVATION);
		config.setActivationPageCode(activationElem.getAttributeValue(ACTION_PAGECODE_ATTR));
		Map<String, Template> templates = this.extractTemplates(activationElem);
		config.setActivationTemplates(templates);
	}
	
	protected void extractReactivationMailConfig(Element root, IUserRegConfig config) {
		Element reactivationElem = root.getChild(REACTIVATION);
		config.setReactivationPageCode(reactivationElem.getAttributeValue(ACTION_PAGECODE_ATTR));
		Map<String, Template> templates = this.extractTemplates(reactivationElem);
		config.setReactivationTemplates(templates);
	}
	
	protected Map<String, Template> extractTemplates(Element parentElem) {
		Map<String, Template> templates = new HashMap<String, Template>();
		List<Element> templateElements = parentElem.getChildren(TEMPLATE);
		Iterator<Element> it = templateElements.iterator();
		while (it.hasNext()) {
			Element templateElem = it.next();
			Template template = new Template();
			String langCode = templateElem.getAttributeValue(TEMPLATE_LANG_ATTR);
			template.setSubject(templateElem.getChildText(TEMPLATE_SUBJECT_CHILD));
			template.setBody(templateElem.getChildText(TEMPLATE_BODY_CHILD));
			templates.put(langCode, template);
		}
		return templates;
	}
	
	protected Document decodeDOM(String xml) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xml);
		Document doc = null;
		try {
			doc = builder.build(reader);
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("Errore nel parsing: " + t.getMessage());
			throw new ApsSystemException("Errore nel parsing della configurazione", t);
		}
		return doc;
	}
	
	protected Element createConfigElement(IUserRegConfig config) {
		Element configElem = new Element(ROOT);
		Element tokenElem = this.createTokenValidityElement(config);
		configElem.addContent(tokenElem);
		Element mailElem = this.createMailSenderElement(config);
		configElem.addContent(mailElem);
		Element userAuthsElem = this.createUserAuthsElement(config);
		configElem.addContent(userAuthsElem);
		Element activationElem = this.createActivationMailElement(config);
		configElem.addContent(activationElem);
		Element reactivationElem = this.createReactivationMailElement(config);
		configElem.addContent(reactivationElem);
		return configElem;
	}
	
	private Element createTokenValidityElement(IUserRegConfig config) {
		Element tokenElement = new Element(TOKEN_VALIDITY);
		tokenElement.setAttribute(TOKEN_VALIDITY_MINUTES_ATTR, String.valueOf(config.getTokenValidityMinutes()));
		return tokenElement;
	}
	
	private Element createMailSenderElement(IUserRegConfig config) {
		Element senderElement = new Element(MAIL_SENDER);
		senderElement.setAttribute(MAIL_SENDER_CODE_ATTR, config.getEMailSenderCode());
		return senderElement;
	}
	
	private Element createUserAuthsElement(IUserRegConfig config) {
		Element userAuthsElement = new Element(USER_DEFAULT_AUTHS);
		Set<String> csvs = config.getDefaultCsvAuthorizations();
		if (null != csvs) {
			Iterator<String> iter = csvs.iterator();
			while (iter.hasNext()) {
				String csv = iter.next();
				String[] params = csv.split(",");
				if (params.length > 0) {
					Element authElem = new Element(USER_DEFAULT_AUTH_ELEM);
					authElem.setAttribute(USER_AUTH_GROUP_ATTR, params[0]);
					if (params.length > 1) {
						authElem.setAttribute(USER_AUTH_ROLE_ATTR, params[1]);
					}
					userAuthsElement.addContent(authElem);
				}
			}
		}
		return userAuthsElement;
	}
	
	private Element createActivationMailElement(IUserRegConfig config) {
		Element activationElement = new Element(ACTIVATION);
		activationElement.setAttribute(ACTION_PAGECODE_ATTR, config.getActivationPageCode());
		this.addTemplateElements(activationElement, config.getActivationTemplates());
		return activationElement;
	}
	
	private Element createReactivationMailElement(IUserRegConfig config) {
		Element reactivationElement = new Element(REACTIVATION);
		reactivationElement.setAttribute(ACTION_PAGECODE_ATTR, config.getReactivationPageCode());
		this.addTemplateElements(reactivationElement, config.getReactivationTemplates());
		return reactivationElement;
	}
	
	private void addTemplateElements(Element parent, Map<String, Template> templates) {
		Iterator<Entry<String, Template>> templatesIter = templates.entrySet().iterator();
		while (templatesIter.hasNext()) {
			Element templateElement = new Element(TEMPLATE);
			Entry<String, Template> current = templatesIter.next();
			Template template = current.getValue();
			
			templateElement.setAttribute(TEMPLATE_LANG_ATTR, current.getKey());
			
			Element subjectElem = new Element(TEMPLATE_SUBJECT_CHILD);
			subjectElem.addContent(new CDATA(template.getSubject()));
			templateElement.addContent(subjectElem);
			
			Element bodyElem = new Element(TEMPLATE_BODY_CHILD);
			bodyElem.addContent(new CDATA(template.getBody()));
			templateElement.addContent(bodyElem);
			
			parent.addContent(templateElement);
		}
	}
	
	private final String ROOT = "userRegConfig";
	
	private final String TOKEN_VALIDITY = "tokenValidity";
	private final String TOKEN_VALIDITY_MINUTES_ATTR = "minutes";
	
	private final String MAIL_SENDER = "sender";
	private final String MAIL_SENDER_CODE_ATTR = "code";
	
	private final String USER_DEFAULT_AUTHS = "userAuthDefaults";
	private final String USER_DEFAULT_AUTH_ELEM = "authotization";
	private final String USER_AUTH_ROLE_ATTR = "role";
	private final String USER_AUTH_GROUP_ATTR = "group";
	
	private final String ACTIVATION = "activation";
	private final String REACTIVATION = "reactivation";
	
	private final String ACTION_PAGECODE_ATTR = "pageCode";
	private final String TEMPLATE = "template";
	private final String TEMPLATE_LANG_ATTR = "lang";
	private final String TEMPLATE_SUBJECT_CHILD = "subject";
	private final String TEMPLATE_BODY_CHILD = "body";
	
}