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
package com.agiletec.plugins.jpnewsletter.apsadmin.config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class NewsletterConfigAction extends AbstractNewsletterConfigAction {
	
	public String edit() {
		try {
			NewsletterConfig config = this.getNewsletterManager().getNewsletterConfig();
			this.setNewsletterConfig(config);
			this.valueForm(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String entryConfig() {
		try {
			NewsletterConfig config = this.getNewsletterConfig();
			if (config==null) {
				config = this.getNewsletterManager().getNewsletterConfig();
				this.setNewsletterConfig(config);
			}
			this.valueForm(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String addCategoryMapping() {
		this.updateConfigOnSession();
		try {
			Collection<Object> mappedCategories = this.getNewsletterConfig().getSubscriptions().keySet();
			Collection<Object> mappedAttributes = this.getNewsletterConfig().getSubscriptions().values();
			String attribute = this.getAttributeName();
			if (null == attribute || attribute.trim().length() == 0) {
				this.addFieldError("attributeName", this.getText("error.attribute.not.valid"));
			} else if (mappedAttributes.contains(attribute)) {
				this.addFieldError("attributeName", this.getText("error.attribute.already.used"));
			}
			String category = this.getCategoryCode();
			if (null == category || category.trim().length() == 0) {
				this.addFieldError("categoryCode", this.getText("error.category.not.valid"));
			} else if (mappedCategories.contains(category)) {
				this.addFieldError("categoryCode", this.getText("error.category.already.used"));
			}
			if (this.hasFieldErrors()) return INPUT;
			NewsletterConfig config = this.getNewsletterConfig();
			config.getSubscriptions().put(category, attribute);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addCategoryMapping");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String removeCategoryMapping() {
		this.updateConfigOnSession();
		try {
			String category = this.getCategoryCode();
			NewsletterConfig config = this.getNewsletterConfig();
			config.getSubscriptions().remove(category);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeCategoryMapping");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String addContentType() {
		this.updateConfigOnSession();
		try {
			//TODO Inserire eventuali parametri (codice e descr), da passare alle etichette,  degli oggetti che si cerca di utilizzare e che sono già in uso (tipo di contenuto e categorie)
			if (null == this.getContentManager().getSmallContentTypesMap().get(this.getContentTypeCode())) {
				this.addFieldError("contentTypeCode", this.getText("error.contenttype.not.valid"));
			}
			if (null != this.getNewsletterConfig().getContentType(this.getContentTypeCode())) {
				this.addFieldError("contentTypeCode", this.getText("error.contenttype.already.used"));
			}
			if (this.hasFieldErrors()) return INPUT;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addContentType");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String removeContentType() {
		this.updateConfigOnSession();
		try {
			NewsletterConfig config = this.getNewsletterConfig();
			config.getContentTypes().remove(this.getContentTypeCode());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeContentType");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		this.updateConfigOnSession();
		try {
			this.getNewsletterManager().updateNewsletterConfig(this.getNewsletterConfig());
			this.getRequest().getSession().removeAttribute(NEWSLETTER_CONFIG_SESSION_PARAM);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	protected void valueForm(NewsletterConfig config) {
		this.setActiveService(config.isActive());
		Calendar startScheduler = Calendar.getInstance();
		startScheduler.setTime(config.getStartScheduler());
		this.setStartSchedulerHour(startScheduler.get(Calendar.HOUR_OF_DAY));
		this.setStartSchedulerMinute(startScheduler.get(Calendar.MINUTE));
		this.setAlsoHtml(config.isAlsoHtml());
	}

	protected void updateConfigOnSession() {
		NewsletterConfig config = this.getNewsletterConfig();
		config.setActive(this.getActiveService());
		Calendar startScheduler = Calendar.getInstance();
		startScheduler.setTime(config.getStartScheduler());
		startScheduler.set(Calendar.HOUR_OF_DAY, this.getStartSchedulerHour());
		startScheduler.set(Calendar.MINUTE, this.getStartSchedulerMinute());
		config.setStartScheduler(startScheduler.getTime());
		config.setAlsoHtml(this.getAlsoHtml());
	}

	public List<SelectItem> getMailSenders() {
		try {
			Map<String, String> senders = this.getMailManager().getMailConfig().getSenders();
			List<SelectItem> items = new ArrayList<SelectItem>(senders.size());
			Iterator<String> iter = senders.keySet().iterator();
			while (iter.hasNext()) {
				String string = (String) iter.next();
				SelectItem item = new SelectItem(string, senders.get(string));
				items.add(item);
			}
			return items;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getMailSenders");
			throw new RuntimeException(t);
		}
	}
	
	public List<Category> getCategories() {
		return this.getCategoryManager().getCategoriesList();
	}
	
	public IUserProfile getDefaultProfile() {
		return this.getProfileManager().getDefaultProfileType();
	}
	
	public List<AttributeInterface> getBooleanProfileAttributes() {
		List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
		List<AttributeInterface> profileAttributes = this.getDefaultProfile().getAttributeList();
		for (int i = 0; i < profileAttributes.size(); i++) {
			AttributeInterface attribute = profileAttributes.get(i);
			if (attribute instanceof BooleanAttribute) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}

	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}

	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}

	public Boolean getActiveService() {
		if (null == this._activeService) return false;
		return _activeService;
	}
	public void setActiveService(Boolean activeService) {
		this._activeService = activeService;
	}

	public Boolean getAlsoHtml() {
		if (null == this._alsoHtml) return false;
		return _alsoHtml;
	}
	public void setAlsoHtml(Boolean alsoHtml) {
		_alsoHtml = alsoHtml;
	}

	public String getCategoryCode() {
		return _categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}

	public String getAttributeName() {
		return _attributeName;
	}
	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}

	public Integer getStartSchedulerHour() {
		if (null == this._startSchedulerHour || this._startSchedulerHour == 0) {
			this._startSchedulerHour = this.getStartSchedulerField(Calendar.HOUR_OF_DAY);
		}
		return _startSchedulerHour;
	}
	public void setStartSchedulerHour(Integer startSchedulerHour) {
		this._startSchedulerHour = startSchedulerHour;
	}

	public Integer getStartSchedulerMinute() {
		if (null == this._startSchedulerMinute || this._startSchedulerMinute == 0) {
			this._startSchedulerMinute = this.getStartSchedulerField(Calendar.MINUTE);
		}
		return _startSchedulerMinute;
	}

	public void setStartSchedulerMinute(Integer startSchedulerMinute) {
		this._startSchedulerMinute = startSchedulerMinute;
	}

	private Integer getStartSchedulerField(int field) {
		NewsletterConfig config = this.getNewsletterConfig();
		Calendar startScheduler = Calendar.getInstance();
		startScheduler.setTime(config.getStartScheduler());
		return startScheduler.get(field);
	}
	
	public List<IPage> getConfirmSubscriptionPages() throws Throwable {
		return this.getPageManager().getWidgetUtilizers(JpnewsletterSystemConstants.SUBSCRIPTION_CONFIRM_WIDGET_CODE);
	}
	
	public List<IPage> getConfirmUnsubscriptionPages() throws Throwable {
		return this.getPageManager().getWidgetUtilizers(JpnewsletterSystemConstants.UNSUBSCRIPTION_CONFIRM_WIDGET_CODE);
	}
	/*
	public List<IPage> getPages() {
		if (this._pages==null) {
			this._pages = new ArrayList<IPage>();
			IPage root = this.getPageManager().getRoot();
			this.addPages(root, this._pages);
		}
		return this._pages;
	}
	
	protected void addPages(IPage page, List<IPage> pages) {
		pages.add(page);
		IPage[] children = page.getChildren();
		for (int i=0; i<children.length; i++) {
			this.addPages(children[i], pages);
		}
	}
	*/
	public String getContentTypeCode() {
		return _contentTypeCode;
	}
	public void setContentTypeCode(String contentTypeCode) {
		this._contentTypeCode = contentTypeCode;
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	protected IUserProfileManager getProfileManager() {
		return _profileManager;
	}
	public void setProfileManager(IUserProfileManager profileManager) {
		this._profileManager = profileManager;
	}
	
	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	private List<IPage> _pages;

	private Boolean _activeService;
	private Boolean _alsoHtml;

	private String _categoryCode;
	private String _attributeName;

	private Integer _startSchedulerHour;
	private Integer _startSchedulerMinute;

	private String _contentTypeCode;

	private ICategoryManager _categoryManager;
	private IUserProfileManager _profileManager;
	private IMailManager _mailManager;
	private IPageManager _pageManager;

	public static String NEWSLETTER_CONFIG_SESSION_PARAM = "newsletterConfig_sessionParam";

}