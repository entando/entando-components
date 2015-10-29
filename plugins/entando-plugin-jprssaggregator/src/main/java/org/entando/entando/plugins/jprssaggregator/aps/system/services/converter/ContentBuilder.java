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
package org.entando.entando.plugins.jprssaggregator.aps.system.services.converter;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.HtmlHandler;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.ApsAggregatorItem;
import com.rometools.rome.feed.synd.SyndEntry;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * This class populates a {@link Content} from a {@link SyndEntryImpl} object
 */
public class ContentBuilder {

	private static final Logger _logger = LoggerFactory.getLogger(ContentBuilder.class);
	
	/**
	 * Populate a content.
	 * @param content The content to polulate 
	 * @param feedItem The feedItem that contains the informations
	 * @param apsAggregatorItem  The ApsAggregatorItem that contains informations about categories 
	 * @param aggregatorConfig The object tha handles the mapping informations
	 * @throws ApsSystemException if an error occurs
	 */
	public void populateContentFromMapping(Content content, SyndEntry feedItem, ApsAggregatorItem apsAggregatorItem, AggregatorConfig aggregatorConfig) throws ApsSystemException {
		//set descrizione contenuto
		this.setContentDescr(content, feedItem, aggregatorConfig);
		//set gruppi;
		this.setContentGroups(content, aggregatorConfig);
		//setta le categorie
		this.setContentCategories(content, apsAggregatorItem);
		List<Element> mappingElements = aggregatorConfig.getAttributes();
		Iterator<Element> mappingElementsIt = mappingElements.iterator();
		while (mappingElementsIt.hasNext()) {
			Element mappingElement =  mappingElementsIt.next();
			this.applyAttributeMapping(mappingElement, feedItem, content);
		}
		this.setAggregatorItemParams(content, apsAggregatorItem, feedItem, aggregatorConfig);
	}

	private void setContentDescr(Content content, SyndEntry feedItem, AggregatorConfig aggregatorConfig) throws ApsSystemException {
		try {
			String description = "";
			Element descr = aggregatorConfig.getDescr();
			if (null != descr) {
				Attribute descrTypeAttr = descr.getAttribute("type");
				if (null != descrTypeAttr && descrTypeAttr.getValue().equalsIgnoreCase("expression")) {
					String filedName = descr.getText();
					description = this.extractValue(filedName, feedItem);
					if (null == description || description.trim().length() == 0) {
						description = this.getMultiLangDafaultValue(descr, this.getDefaultLangCode());
					}
				} else {
					description = descr.getText();
				}
				description = this.trimString(description, descr);
				content.setDescription(description);
			}
		} catch (Throwable t) {
			throw new ApsSystemException("An error occurred setting the content description", t);
		}
	}

	private void setContentGroups(Content content, AggregatorConfig aggregatorConfig) {
		if (null != aggregatorConfig.getMainGroup()) {
			content.setMainGroup(aggregatorConfig.getMainGroup());
		}
		if (null != aggregatorConfig.getGroups()) {
			List<String> groupList = aggregatorConfig.getGroups();
			Iterator<String> groupIt = groupList.iterator();
			while (groupIt.hasNext()) {
				content.addGroup(groupIt.next());
			}
		}
	}

	private void setContentCategories(Content content, ApsAggregatorItem apsAggregatorItem) {
		ApsProperties categories = apsAggregatorItem.getCategories();
		if (null != categories && categories.size() > 0) {
			for (Enumeration e = categories.keys() ; e.hasMoreElements() ;) {
				String categoryCode = (String) e.nextElement();
				Category category = this.getCategoryManager().getCategory(categoryCode);
				if (null != category && !content.getCategories().contains(category)) {
					content.addCategory(category);
				}
			}
		}
	}

	/**
	 * Executes a getter method on the target object to retrieve the property
	 * @param property the property to get
	 * @param target the object 
	 * @return a string value of the property
	 * @throws ApsSystemException
	 */
	private String extractValue(String property, Object target) throws ApsSystemException {
		String value = "";
		try {
			value = ((String)PropertyUtils.getNestedProperty(target, property)).trim();
		} catch (Throwable t) {
			_logger.error("error in extractValue", t);
		}
		return value;
	}

	/**
	 * Returns a system label.
	 * Check if the mapping element has an default attribute. If true, 
	 * return the string.
	 * @param element the mapping element
	 * @param langCode the lang code.
	 * @return return the string stored in the system labels that corresponds to the value of the mapping element
	 * provided
	 * @throws ApsSystemException if an error occurs
	 */
	private String getMultiLangDafaultValue(Element element, String langCode) throws ApsSystemException {
		String defaultValue = "";
		Attribute defaultValueAttr = element.getAttribute("default");
		if (null != defaultValueAttr) {
			defaultValue = this.getI18nManager().getLabel(defaultValueAttr.getValue(), langCode);
		}
		return defaultValue;
	}

	/**
	 * If exitst the attribute maxLangth in the descr attribute
	 * if necessary truncates the string
	 * @param description the string value of the description
	 * @param descr The descr attribute in the confifuration
	 * @return the desription truncate with the value specified in the attribute maxLenght
	 */
	private String trimString(String description, Element descr) {
		if(null == description) return "";
		if (null != descr && null != descr.getAttributeValue("maxLength")) {
			int maxLength = new Integer(descr.getAttributeValue("maxLength")).intValue();
			if (description.length() > maxLength) {
				description = description.substring(0, maxLength-1);
			}
		}
		return description;
	}

	private void applyAttributeMapping(Element element, Object target, Content content) throws ApsSystemException {
		String source = element.getAttributeValue("source");
		String dest = element.getAttributeValue("dest");
		String rawFeedvalue = this.extractValue(source, target);
		HtmlHandler htmlHandler = new HtmlHandler();
		String feedvalue = htmlHandler.getParsedText(rawFeedvalue);
		AttributeInterface attr =  (AttributeInterface) content.getAttribute(dest);
		if (attr instanceof LinkAttribute) {
			((LinkAttribute)attr).setText(feedvalue, this.getDefaultLangCode());
			SymbolicLink link = new SymbolicLink();
			link.setDestinationToUrl(feedvalue);
			((LinkAttribute)attr).setSymbolicLink(link);
		} else if (attr instanceof HypertextAttribute) {
			if (feedvalue.trim().length() > 0) {
				((HypertextAttribute)attr).setText(feedvalue, this.getDefaultLangCode());
			} else {
				Iterator<Lang> langIt = this.getLangManager().getLangs().iterator();
				while (langIt.hasNext()) {
					Lang current = langIt.next();
					String code = current.getCode();
					String defaultValue = this.getMultiLangDafaultValue(element, code);
					((HypertextAttribute)attr).setText(defaultValue, code);
				}
			}
		} else if(attr instanceof TextAttribute) {
			if (feedvalue.trim().length() > 0) {
				((TextAttribute)attr).setText(feedvalue, this.getDefaultLangCode());
			} else {
				Iterator<Lang> langIt = this.getLangManager().getLangs().iterator();
				while (langIt.hasNext()) {
					Lang current = langIt.next();
					String code = current.getCode();
					String defaultValue = this.getMultiLangDafaultValue(element, code);
					((TextAttribute)attr).setText(defaultValue, code);
				}
			}
		} else if(attr instanceof MonoTextAttribute) {
			((MonoTextAttribute)attr).setText(feedvalue);
		}
	}
	
	protected void setAggregatorItemParams(Content content, ApsAggregatorItem item, SyndEntry feedItem, AggregatorConfig aggregatorConfig) throws ApsSystemException {
		List<Element> elements = aggregatorConfig.getApsAggregatorItems();
		if (null != elements && elements.size() > 0) {
			Iterator<Element> it = elements.iterator();
			while (it.hasNext()) {
				Element element = it.next();
				this.applyAttributeMapping(element, item, content);
			}
		}
	}
	
	private String getDefaultLangCode() {
		return this.getLangManager().getDefaultLang().getCode();
	}
	
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	protected ILangManager getLangManager() {
		return _langManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	protected IContentManager getContentManager() {
		return _contentManager;
	}

	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}
	protected II18nManager getI18nManager() {
		return _i18nManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	private ILangManager _langManager;
	private IContentManager _contentManager;
	private II18nManager _i18nManager;
	private ICategoryManager _categoryManager;
}
