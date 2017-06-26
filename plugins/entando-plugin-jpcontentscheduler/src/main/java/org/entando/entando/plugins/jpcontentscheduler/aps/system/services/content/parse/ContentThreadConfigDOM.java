/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.parse;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentTypeElem;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Classe DOM delegata alle operazioni di lettura/scrittura della configurazione
 * del thread di pubblicazione/sospensione dei contenuti.
 */
public class ContentThreadConfigDOM {

	/**
	 * Estrae la configurazione
	 *
	 * @param xml
	 * The xml containing the configuration.
	 * @return The contentthread configuration.
	 * @throws ApsSystemException
	 * In case of parsing errors.
	 */
	public ContentThreadConfig extractConfig(String xml) throws ApsSystemException {
		ContentThreadConfig config = new ContentThreadConfig();
		config.setGroupsContentType(new HashMap<String, List<String>>());
		config.setUsersContentType(new HashMap<String, List<String>>());
		Element root = this.getRootElement(xml);
		this.setSitecode(root, config);
		this.extractScheduler(root, config);
		this.extractGloabalCat(root, config);
		this.extractContentReplace(root, config);
		this.extractTypes(root, config);
		// this.extractGroups(root, config);
		this.extractUsers(root, config);
		this.extractMailConfig(root, config);
		return config;
	}

	/**
	 * Se l'elemento contentThreadConfig ha un attributo sitecode, che specifica
	 * il codice del sito abilitato all'invio, lo setta all'interno dell'oggetto
	 * {@link NewsletterConfig}
	 *
	 * @param root
	 * l'elemento contentThreadConfig
	 * @param contentThreadConfig
	 * l'oggetto contenitore della configurazione
	 */
	private void setSitecode(Element root, ContentThreadConfig contentThreadConfig) {
		Attribute sitecodeAttr = root.getAttribute(SITECODE);
		if (null != sitecodeAttr && sitecodeAttr.getValue().trim().length() > 0) {
			String sitecode = sitecodeAttr.getValue();
			contentThreadConfig.setSitecode(sitecode);
		}
	}

	/**
	 * Create an xml containing the newsletter configuration.
	 *
	 * @param config
	 * The contentThread configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException
	 * In case of errors.
	 */
	public String createConfigXml(ContentThreadConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}

	private void extractScheduler(Element root, ContentThreadConfig config) {
		Element schedulerElement = root.getChild(SCHEDULER_ELEM);

		String active = schedulerElement.getAttributeValue(SCHEDULER_ACTIVE_ATTR);
		config.setActive(active == null || active.equalsIgnoreCase("true"));
	}

	private void extractGloabalCat(Element root, ContentThreadConfig config) {
		Element gloabalCatElement = root.getChild(GLOBALCAT_ELEM);
		String globalcat = null;
		if (gloabalCatElement != null) {
			globalcat = gloabalCatElement.getAttributeValue(GLOBALCAT_CODE_ATTR);
		}
		config.setGlobalCat(globalcat);
	}

	private void extractContentReplace(Element root, ContentThreadConfig config) {
		Element contentReplaceElement = root.getChild(CONTENTREPL_ELEM);
		String contentRepModel = null;
		String contentRepId = null;
		if (contentReplaceElement != null) {
			contentRepId = contentReplaceElement.getAttributeValue(CONTENTREPL_CODE_ATTR);
			contentRepModel = contentReplaceElement.getAttributeValue(CONTENTREPL_MODEL_ATTR);
		}
		config.setContentIdRepl(contentRepId);
		config.setContentModelRepl(contentRepModel);
	}

	private List<String> getContentTypes(String s) {
		List<String> ans = new ArrayList<String>();
		String[] ss = s.split(",");
		ans = Arrays.asList(ss);
		return ans;
	}

	@SuppressWarnings("unchecked")
	private void extractUsers(Element root, ContentThreadConfig config) {
		Element usersElem = root.getChild(USERS_ELEM);
		List usersList = usersElem.getChildren();

		Iterator i = usersList.iterator();
		while (i.hasNext()) {
			Element currElem = (Element) i.next();
			config.getUsersContentType().put(currElem.getAttributeValue(USER_USERNAME_ATTR), getContentTypes(currElem.getAttributeValue(USER_CONTENTTYPE_ATTR)));
		}
	}

	@SuppressWarnings("unchecked")
	private void extractTypes(Element root, ContentThreadConfig config) {
		Element ctsElem = root.getChild(CONTENTTYPES_ELEM);
		List<String> listCats = null;
		if (ctsElem != null) {
			List ctsList = ctsElem.getChildren();

			Iterator i = ctsList.iterator();
			while (i.hasNext()) {
				Element currElem = (Element) i.next();
				// obtain categories from typecode
				if (currElem != null) {
					List catList = currElem.getChildren();
					Iterator i2 = catList.iterator();
					listCats = new ArrayList<String>();
					while (i2.hasNext()) {
						Element catElem = (Element) i2.next();
						listCats.add(catElem.getAttributeValue(CODE_ATTR));
					}
				}
				String fieldNameIdContentReplace = null;
				if (currElem.getAttributeValue(ID_CONTENT_REPLACE_ATTR) != null) {
					fieldNameIdContentReplace = currElem.getAttributeValue(ID_CONTENT_REPLACE_ATTR);
				}
				String fieldNameModelIdContentReplace = null;
				if (currElem.getAttributeValue(MODEL_ID_CONTENT_REPLACE_ATTR) != null) {
					fieldNameModelIdContentReplace = currElem.getAttributeValue(MODEL_ID_CONTENT_REPLACE_ATTR);
				}
				String suspend = null;
				if (currElem.getAttributeValue(SUSPEND_ATTR) != null && (currElem.getAttributeValue(SUSPEND_ATTR).equalsIgnoreCase("true") || currElem.getAttributeValue(
						SUSPEND_ATTR).equalsIgnoreCase("false"))) {
					suspend = currElem.getAttributeValue(SUSPEND_ATTR);
				} else {
					suspend = "";
				}
				config.getTypesList().add(new ContentTypeElem(currElem.getAttributeValue(TYPE_ATTR),
						currElem.getAttributeValue(START_DATE_ATTR),
						currElem.getAttributeValue(END_DATE_ATTR),
						fieldNameIdContentReplace,
						suspend,
						listCats,
						fieldNameModelIdContentReplace));
				listCats = null;
			}
		}
	}

	/**
	 * Estrae la parte di configurazione relativa alla mail.
	 * 
	 * @param root
	 * L'elemento radice contenente il sottoelemento relativo alle mail.
	 * @param config
	 * La configurazione del servizio contentThread.
	 */
	private void extractMailConfig(Element root, ContentThreadConfig config) {
		Element mailElem = root.getChild(MAIL_ELEM);

		String alsoHtml = mailElem.getAttributeValue(MAIL_ALSOHTML_ATTR);
		config.setAlsoHtml(alsoHtml != null && "true".equalsIgnoreCase(alsoHtml));

		String senderCode = mailElem.getAttributeValue(MAIL_SENDERCODE_ATTR);
		config.setSenderCode(senderCode);

		String mailAttrName = mailElem.getAttributeValue(MAIL_MAILATTRNAME_ATTR);
		config.setMailAttrName(mailAttrName);

		config.setSubject(mailElem.getChildText(MAIL_SUBJECT_CHILD));

		config.setHtmlHeader(mailElem.getChildText(MAIL_HTML_HEADER_CHILD));
		config.setHtmlFooter(mailElem.getChildText(MAIL_HTML_FOOTER_CHILD));
		config.setHtmlSeparator(mailElem.getChildText(MAIL_HTML_SEPARATOR_CHILD));

		config.setTextHeader(mailElem.getChildText(MAIL_TEXT_HEADER_CHILD));
		config.setTextFooter(mailElem.getChildText(MAIL_TEXT_FOOTER_CHILD));
		config.setTextSeparator(mailElem.getChildText(MAIL_TEXT_SEPARATOR_CHILD));

		config.setHtmlHeaderMove(mailElem.getChildText(MAIL_HTML_HEADER_CHILD_MOVE));
		config.setHtmlFooterMove(mailElem.getChildText(MAIL_HTML_FOOTER_CHILD_MOVE));
		config.setHtmlSeparatorMove(mailElem.getChildText(MAIL_HTML_SEPARATOR_CHILD_MOVE));

		config.setTextHeaderMove(mailElem.getChildText(MAIL_TEXT_HEADER_CHILD_MOVE));
		config.setTextFooterMove(mailElem.getChildText(MAIL_TEXT_FOOTER_CHILD_MOVE));
		config.setTextSeparatorMove(mailElem.getChildText(MAIL_TEXT_SEPARATOR_CHILD_MOVE));
	}

	/**
	 * Crea l'elemento della configurazione del servizio di contentThread.
	 * 
	 * @param config
	 * La configurazione del servizio contentThread.
	 * @return L'elemento della configurazione del servizio di contentThread.
	 */
	private Element createConfigElement(ContentThreadConfig config) {
		Element configElem = new Element(ROOT);
		configElem.setAttribute(SITECODE, config.getSitecode());

		Element schedulerElem = this.createSchedulerElement(config);
		configElem.addContent(schedulerElem);

		Element globalCatElem = this.createGlobalCatElement(config);
		configElem.addContent(globalCatElem);

		Element contentReplaceElem = this.createContentReplaceElement(config);
		configElem.addContent(contentReplaceElem);

		// Element groupsElem = this.createGroupsElement(config);
		// configElem.addContent(groupsElem);

		Element usersElem = this.createUsersElement(config);
		configElem.addContent(usersElem);

		Element contentTypesElem = this.createContentTypesElement(config);
		configElem.addContent(contentTypesElem);

		Element mailElem = this.createMailElement(config);
		configElem.addContent(mailElem);

		return configElem;
	}

	private Element createSchedulerElement(ContentThreadConfig config) {
		Element schedulerElement = new Element(SCHEDULER_ELEM);
		schedulerElement.setAttribute(SCHEDULER_ACTIVE_ATTR, String.valueOf(config.isActive()));
		return schedulerElement;
	}

	private Element createGlobalCatElement(ContentThreadConfig config) {
		Element gloabalCatElement = new Element(GLOBALCAT_ELEM);
		gloabalCatElement.setAttribute(GLOBALCAT_CODE_ATTR, config.getGlobalCat());
		return gloabalCatElement;
	}

	//	private String setContentTypes(List<String> list) {
	//		StringBuilder ans = new StringBuilder("");
	//		for (Iterator<String> i = list.iterator(); i.hasNext();) {
	//			String curr = i.next();
	//			ans.append(curr);
	//			if (i.hasNext()) {
	//				ans.append(",");
	//			}
	//		}
	//		return ans.toString();
	//	}

	private Element createContentReplaceElement(ContentThreadConfig config) {
		Element contentReplaceElement = new Element(CONTENTREPL_ELEM);
		String contentId = config.getContentIdRepl();
		if (StringUtils.isNotBlank(contentId)) {
			contentReplaceElement.setAttribute(CONTENTREPL_CODE_ATTR, contentId);
		}
		String modelId = config.getContentModelRepl();
		if (StringUtils.isNotBlank(modelId)) {
			contentReplaceElement.setAttribute(CONTENTREPL_MODEL_ATTR, modelId);
		}
		return contentReplaceElement;
	}

	private Element createGroupsElement(ContentThreadConfig config) {
		Element groupsElement = new Element(GROUPS_ELEM);
		Map<String, List<String>> groupsMap = config.getGroupsContentType();
		Set<String> keys = groupsMap.keySet();
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			String key = i.next();
			//String contentTypes = StringUtils.join(groupsMap.get(key), ",");
			Element groupElem = new Element(GROUP_ELEM);
			groupElem.setAttribute(GROUP_ID_ATTR, key);

			groupsElement.addContent(groupElem);
		}
		return groupsElement;
	}

	private Element createUsersElement(ContentThreadConfig config) {
		Element usersElement = new Element(USERS_ELEM);
		Map<String, List<String>> usersMap = config.getUsersContentType();
		Set<String> keys = usersMap.keySet();
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			String key = i.next();
			String contentTypes = StringUtils.join(usersMap.get(key), ",");

			Element userElem = new Element(USER_ELEM);
			userElem.setAttribute(USER_USERNAME_ATTR, key);
			userElem.setAttribute(USER_CONTENTTYPE_ATTR, contentTypes);
			usersElement.addContent(userElem);
		}
		return usersElement;
	}

	private Element createContentTypesElement(ContentThreadConfig config) {
		Element ctsElement = new Element(CONTENTTYPES_ELEM);
		List<ContentTypeElem> ctsList = config.getTypesList();
		for (Iterator<ContentTypeElem> i = ctsList.iterator(); i.hasNext();) {
			ContentTypeElem elem = i.next();
			Element ctElem = new Element(CONTENTTYPE_ELEM);
			this.safeSetAttr(ctElem, TYPE_ATTR, elem.getContentType());
			this.safeSetAttr(ctElem, START_DATE_ATTR, elem.getStartDateAttr());
			this.safeSetAttr(ctElem, END_DATE_ATTR, elem.getEndDateAttro());
			this.safeSetAttr(ctElem, ID_CONTENT_REPLACE_ATTR, elem.getIdContentReplace());
			this.safeSetAttr(ctElem, MODEL_ID_CONTENT_REPLACE_ATTR, elem.getModelIdContentReplace());
			this.safeSetAttr(ctElem, SUSPEND_ATTR, elem.getSuspend());

			List<String> catList = elem.getIdsCategories();
			for (Iterator<String> i2 = catList.iterator(); i2.hasNext();) {
				String cat = i2.next();
				Element catElem = new Element(CATEGORY_ELEM);
				this.safeSetAttr(ctElem, CODE_ATTR, cat);
				ctElem.addContent(catElem);
			}
			ctsElement.addContent(ctElem);
		}
		return ctsElement;
	}

	private void safeSetAttr(Element element, String attrName, String attrValue) {
		if (null != attrValue) {
			element.setAttribute(attrName, attrValue);
		}
	}

	/**
	 * Crea l'elemento della configurazione relativa alle mail.
	 * 
	 * @param config
	 * La configurazione del servizio contentThread.
	 * @return L'elemento di configurazione relativo alle mail.
	 */
	private Element createMailElement(ContentThreadConfig config) {
		Element mailElem = new Element(MAIL_ELEM);

		mailElem.setAttribute(MAIL_ALSOHTML_ATTR, String.valueOf(config.isAlsoHtml()));
		mailElem.setAttribute(MAIL_SENDERCODE_ATTR, config.getSenderCode());
		mailElem.setAttribute(MAIL_MAILATTRNAME_ATTR, config.getMailAttrName());

		Element subject = new Element(MAIL_SUBJECT_CHILD);
		subject.addContent(new CDATA(config.getSubject()));
		mailElem.addContent(subject);

		//

		Element htmlHeader = new Element(MAIL_HTML_HEADER_CHILD);
		htmlHeader.addContent(new CDATA(config.getHtmlHeader()));
		mailElem.addContent(htmlHeader);

		Element htmlFooter = new Element(MAIL_HTML_FOOTER_CHILD);
		htmlFooter.addContent(new CDATA(config.getHtmlFooter()));
		mailElem.addContent(htmlFooter);

		Element htmlSeparator = new Element(MAIL_HTML_SEPARATOR_CHILD);
		htmlSeparator.addContent(new CDATA(config.getHtmlSeparator()));
		mailElem.addContent(htmlSeparator);

		//

		Element textHeader = new Element(MAIL_TEXT_HEADER_CHILD);
		textHeader.addContent(new CDATA(config.getTextHeader()));
		mailElem.addContent(textHeader);

		Element textFooter = new Element(MAIL_TEXT_FOOTER_CHILD);
		textFooter.addContent(new CDATA(config.getTextFooter()));
		mailElem.addContent(textFooter);

		Element textSeparator = new Element(MAIL_TEXT_SEPARATOR_CHILD);
		textSeparator.addContent(new CDATA(config.getTextSeparator()));
		mailElem.addContent(textSeparator);

		//

		Element htmlHeaderM = new Element(MAIL_HTML_HEADER_CHILD_MOVE);
		htmlHeaderM.addContent(new CDATA(config.getHtmlHeaderMove()));
		mailElem.addContent(htmlHeaderM);

		Element htmlFooterM = new Element(MAIL_HTML_FOOTER_CHILD_MOVE);
		htmlFooterM.addContent(new CDATA(config.getHtmlFooterMove()));
		mailElem.addContent(htmlFooterM);

		Element htmlSeparatorM = new Element(MAIL_HTML_SEPARATOR_CHILD_MOVE);
		htmlSeparatorM.addContent(new CDATA(config.getHtmlSeparatorMove()));
		mailElem.addContent(htmlSeparatorM);

		//

		Element textHeaderM = new Element(MAIL_TEXT_HEADER_CHILD_MOVE);
		textHeaderM.addContent(new CDATA(config.getTextHeaderMove()));
		mailElem.addContent(textHeaderM);

		Element textFooterM = new Element(MAIL_TEXT_FOOTER_CHILD_MOVE);
		textFooterM.addContent(new CDATA(config.getTextFooterMove()));
		mailElem.addContent(textFooterM);

		Element textSeparatorM = new Element(MAIL_TEXT_SEPARATOR_CHILD_MOVE);
		textSeparatorM.addContent(new CDATA(config.getTextSeparatorMove()));
		mailElem.addContent(textSeparatorM);

		return mailElem;
	}

	/**
	 * Returns the Xml element from a given text.
	 * 
	 * @param xmlText
	 * The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException
	 * In case of parsing exceptions.
	 */
	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (JDOMException t) {
			ApsSystemUtils.getLogger().error("Error parsing xml: " + t.getMessage());
			throw new ApsSystemException("Error parsing xml", t);
		} catch (IOException t) {
			ApsSystemUtils.getLogger().error("Error parsing xml: " + t.getMessage());
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}

	private static final String ROOT = "contentThreadconfig";
	private static final String SITECODE = "sitecode";

	private static final String SCHEDULER_ELEM = "scheduler";
	private static final String SCHEDULER_ACTIVE_ATTR = "active";

	private static final String GLOBALCAT_ELEM = "globalcat";
	private static final String GLOBALCAT_CODE_ATTR = "code";

	private static final String CONTENTREPL_ELEM = "contentReplace";
	private static final String CONTENTREPL_CODE_ATTR = "contentId";
	private static final String CONTENTREPL_MODEL_ATTR = "modelId";

	private static final String GROUPS_ELEM = "groups";
	private static final String GROUP_ELEM = "group";
	private static final String GROUP_ID_ATTR = "id";
	// private static final String GROUP_CONTENTTYPE_ATTR = "contentType";

	private static final String CONTENTTYPES_ELEM = "contentTypes";
	private static final String CONTENTTYPE_ELEM = "contentType";
	private static final String CATEGORY_ELEM = "category";
	private static final String TYPE_ATTR = "type";
	private static final String CODE_ATTR = "code";
	private static final String START_DATE_ATTR = "startAttr";
	private static final String END_DATE_ATTR = "endAttr";
	private static final String ID_CONTENT_REPLACE_ATTR = "idContentReplace";
	private static final String MODEL_ID_CONTENT_REPLACE_ATTR = "modelIdContentReplace";
	private static final String SUSPEND_ATTR = "suspend";

	private static final String USERS_ELEM = "users";
	private static final String USER_ELEM = "user";
	private static final String USER_USERNAME_ATTR = "username";
	private static final String USER_CONTENTTYPE_ATTR = "contentType";

	private static final String MAIL_ELEM = "mail";
	private static final String MAIL_ALSOHTML_ATTR = "alsoHtml";
	private static final String MAIL_SENDERCODE_ATTR = "senderCode";
	private static final String MAIL_MAILATTRNAME_ATTR = "mailAttrName";
	private static final String MAIL_SUBJECT_CHILD = "subject";
	private static final String MAIL_HTML_HEADER_CHILD = "htmlHeader";
	private static final String MAIL_HTML_FOOTER_CHILD = "htmlFooter";
	private static final String MAIL_HTML_SEPARATOR_CHILD = "htmlSeparator";
	private static final String MAIL_TEXT_HEADER_CHILD = "textHeader";
	private static final String MAIL_TEXT_FOOTER_CHILD = "textFooter";
	private static final String MAIL_TEXT_SEPARATOR_CHILD = "textSeparator";

	private static final String MAIL_HTML_HEADER_CHILD_MOVE = "htmlHeaderMove";
	private static final String MAIL_HTML_FOOTER_CHILD_MOVE = "htmlFooterMove";
	private static final String MAIL_HTML_SEPARATOR_CHILD_MOVE = "htmlSeparatorMove";
	private static final String MAIL_TEXT_HEADER_CHILD_MOVE = "textHeaderMove";
	private static final String MAIL_TEXT_FOOTER_CHILD_MOVE = "textFooterMove";
	private static final String MAIL_TEXT_SEPARATOR_CHILD_MOVE = "textSeparatorMove";

}
