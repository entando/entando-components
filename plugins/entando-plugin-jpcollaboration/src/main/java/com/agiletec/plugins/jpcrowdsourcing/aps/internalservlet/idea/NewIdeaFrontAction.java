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
package com.agiletec.plugins.jpcrowdsourcing.aps.internalservlet.idea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.category.helper.ICategoryActionHelper;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;
import com.agiletec.plugins.jpcrowdsourcing.apsadmin.portal.specialwidget.IdeaInstanceWidgetAction;
import com.agiletec.plugins.jpcrowdsourcing.apsadmin.util.SmallCategory;


public class NewIdeaFrontAction extends BaseAction implements INewIdeaFrontAction, ServletResponseAware {

	private static final Logger _logger =  LoggerFactory.getLogger(NewIdeaFrontAction.class);

	@Override
	public String entryIdea() {
		try {
			this.resetFileds();
			String descr = super.getParameter("jpcrowdsourcing_fastDescr");
			if (null != descr && descr.trim().length() > 0) {
				this.getIdea().setDescr(descr);
			}
			String instanceCode = super.getParameter("jpcrowdsourcing_fastInstanceCode");
			if (StringUtils.isNotBlank(instanceCode)) {
				this.getIdea().setInstanceCode(instanceCode);
			}
		} catch (Throwable t) {
			_logger.error("error in entryIdea", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected void resetFileds() {
		this.setIdea(new Idea());
		this.setTags(new HashSet<String>());
	}

	@Override
	public String saveIdea() {
		String returnValue = null;
		try {
			Idea idea = this.getIdea();
			idea.setTags(new ArrayList<String>(this.getTags()));
			this.getIdeaManager().addIdea(idea);

			//la pagina sulla quale effettuare il redirect è calcolata in base all'istanza. In caso di problemi, rimane comunque valido il parametro che arriva della jsp tramite il tag PageWithShowlet
			String destPage = this.getRefirectPageCode(idea.getInstanceCode());
			if (StringUtils.isBlank(destPage)) {
				destPage = super.getRequest().getParameter("saveidea_destpage");
			}
			IPage page = (null != destPage) ? this.getPageManager().getPage(destPage) : null;
			if (null != page) {
				IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this.getRequest());
				RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
				Lang lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
				Map<String, String> params = new HashMap<String, String>();
				params.put("newIdea", "true");
				String url = urlManager.createUrl(page, lang, params);
				String encodedUrl = this.getResponse().encodeURL(url.toString());
				this.setRedirectUrl(encodedUrl);
				returnValue = "redirectPage";
			} else {
				if (this.getIdeaManager().getConfig().isModerateEntries()) {
					this.addActionMessage(this.getText("jpcrowdsourcing.message.idea.inserted.moderated"));
				} else {
					this.addActionMessage(this.getText("jpcrowdsourcing.message.idea.inserted.ok"));
				}
				returnValue = SUCCESS;
			}
		} catch (Throwable t) {
			_logger.error("error in saveIdea", t);
			return FAILURE;
		}
		return returnValue;
	}

	protected String getRefirectPageCode(String instanceValue) {
		String code = null;
		try {
			List<IPage> pages = this.getPageManager().getWidgetUtilizers(IdeaInstanceWidgetAction.WIDGET_CODE);
			List<IPage> filteredPages = new ArrayList<IPage>();
			Iterator<IPage> it = pages.iterator();
			while (it.hasNext()) {
				IPage currentPage = it.next();
				Widget[] showlets = currentPage.getWidgets();
				for (int i = 0; i < showlets.length; i++) {
					Widget currentWidget = showlets[i];
					if (null != currentWidget && currentWidget.getType().getCode().equals(IdeaInstanceWidgetAction.WIDGET_CODE)) {
						ApsProperties config = currentWidget.getConfig();
						if (null != config) {
							String value = config.getProperty(IdeaInstanceWidgetAction.WIDGET_PARAM_IDEA_INSTANCE);
							if (StringUtils.isNotBlank(value) && value.equals(instanceValue)) {
								filteredPages.add(currentPage);
							}
						}
					}
				}
			}
			if (null != filteredPages && !filteredPages.isEmpty()) {
				code = filteredPages.get(0).getCode();
			}
		} catch (Throwable t) {
			_logger.error("error in getRefirectPageCode", t);
		}
		return code;
	}

	@Override
	public String joinCategory() {
		try {
			String code = "";
			List<SmallCategory> ideaTags = this.getIdeaTags(false);
			for (int i = 0; i < ideaTags.size(); i++) {
				SmallCategory smallCategory = ideaTags.get(i);
				if(this.getTag().equals(smallCategory.getTitle())){
					code = smallCategory.getCode();
					break;
				}
			}
			Category category = this.getCategoryManager().getCategory(code);
			if (null == category) {
				String categoryCode = this.getHelper().buildCode(this.getTag(), "tag", 30);
				category = new Category();
				category.setCode(categoryCode);
				List<Lang> langs = this.getLangManager().getLangs();
				for (int i = 0; i < langs.size(); i++) {
					Lang lang = langs.get(i);
					category.setTitle(lang.getCode(), categoryCode);
				}
				category.setParentCode(this.getIdeaManager().getCategoryRoot());
				this.getCategoryManager().addCategory(category);
			}
			this.getTags().add(category.getCode());
		} catch (Throwable t) {
			_logger.error("error in joinCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String removeCategory() {
		try {
			this.getTags().remove(this.getTag());
		} catch (Throwable t) {
			_logger.error("error in removeCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public Category getCategory(String code) {
		Category cat = null;
		try {
			cat = this.getCategoryManager().getCategory(code);
		} catch (Throwable t) {
			_logger.error("error in getCategory", t);
			throw new RuntimeException("Errore in getCategory " + code);
		}
		return cat;
	}

	public List<SmallCategory> getIdeaTags(boolean completeTitle) {
		List<SmallCategory> categories = new ArrayList<SmallCategory>();
		try {
			String langCode = this.getCurrentLang().getCode();
			String nodeRootCode = this.getIdeaManager().getCategoryRoot();
			categories = this.getCategoryLeaf(nodeRootCode, langCode, completeTitle);
		} catch (Throwable t) {
			_logger.error("error in getIdeaTags", t);
			throw new RuntimeException("Errore in estrazione categorie");
		}
		return categories;
	}

	private List<SmallCategory> getCategoryLeaf(String nodeRootCode, String langCode, boolean completeTitle) {
		List<SmallCategory> categories = new ArrayList<SmallCategory>();
		try {
			Category root = (Category) this.getCategoryManager().getCategory(nodeRootCode);
			this.addSmallCategory(categories, root, langCode, completeTitle, true);
		} catch (Throwable t) {
			_logger.error("error in methodName", t);
			throw new RuntimeException("Errore in estrazione categorie");
		}
		return categories;
	}

	private void addSmallCategory(List<SmallCategory> categories, Category parentCat, String langCode,  boolean completeTitle, boolean isFirst) {
		for (Category cat : parentCat.getChildren()) {
			if (null == cat.getChildren() || cat.getChildren().length == 0) {
				SmallCategory catSmall = new SmallCategory();
				catSmall.setCode(cat.getCode());
				if (!completeTitle) {
					catSmall.setTitle(cat.getTitles().getProperty(langCode));
				} else {
					catSmall.setTitle(cat.getFullTitle(langCode));
				}
				categories.add(catSmall);
			}
			this.addSmallCategory(categories, cat, langCode, completeTitle, false);
		}
	}

	public List<IdeaInstance> getIdeaInstances() {
		List<IdeaInstance> ideaInstances = new ArrayList<IdeaInstance>();
		try {
			IPage page = this.getCurrentPage();
			Set<String> pageGroups = new HashSet<String>();
			pageGroups.add(page.getGroup());
			if (null != page.getExtraGroups() && !page.getExtraGroups().isEmpty()) {
				pageGroups.addAll(page.getExtraGroups());
			}
			List<Group> groups = this.getAuthorizationManager().getUserGroups(this.getCurrentUser());
			Set<String> userGroups = new HashSet<String>();
			if (null != groups) {
				for (int i = 0; i< groups.size(); i++) {
					userGroups.add(groups.get(i).getName());
				}
			}
			if (!userGroups.contains(Group.FREE_GROUP_NAME)) userGroups.add(Group.FREE_GROUP_NAME);
			Set<String> allgroups = new HashSet<String>(userGroups);
			allgroups.addAll(pageGroups);
			List<String> codes = this.getIdeaInstanceManager().getIdeaInstances(allgroups, null);
			if (null != codes && !codes.isEmpty()) {
				Iterator<String> it = codes.iterator();
				while (it.hasNext()) {
					String code = it.next();
					IdeaInstance ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(code);
					if (null != ideaInstance) {
						ideaInstances.add(ideaInstance);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in getIdeaInstances", t);
			throw new RuntimeException("error in getIdeaInstances  ");
		}
		return ideaInstances;
	}

	protected IPage getCurrentPage() {
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		return (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
	}

	@Override
	public UserDetails getCurrentUser() {
		return super.getCurrentUser();
	}
	
	public String[] getTagsArray(){
		Set<String> tagsSet = new HashSet<String>();
		List<SmallCategory> ideaTags = getIdeaTags(false);
		for (int i = 0; i < ideaTags.size(); i++) {
			SmallCategory smallCategory = ideaTags.get(i);
			tagsSet.add(smallCategory.getTitle());
		}
		return tagsSet.toArray(new String[tagsSet.size()]);
	}

	public void setIdea(Idea idea) {
		this._idea = idea;
	}
	public Idea getIdea() {
		return _idea;
	}

	public void setTags(Set<String> tags) {
		this._tags = tags;
	}
	public Set<String> getTags() {
		return _tags;
	}

	public void setTag(String tag) {
		this._tag = tag;
	}
	public String getTag() {
		return _tag;
	}

	public String getRedirectUrl() {
		return _redirectUrl;
	}
	protected void setRedirectUrl(String redirectUrl) {
		this._redirectUrl = redirectUrl;
	}

	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}
	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}

	protected HttpServletResponse getResponse() {
		return _response;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public ICategoryActionHelper getHelper() {
		return helper;
	}

	public void setHelper(ICategoryActionHelper helper) {
		this.helper = helper;
	}

	private String _tag;
	private String _redirectUrl;
	private Set<String> _tags = new HashSet<String>();
	private Idea _idea;

	private IIdeaManager _ideaManager;
	private ICategoryManager _categoryManager;
	private HttpServletResponse _response;
	private ConfigInterface _configManager;
	private IIdeaInstanceManager _ideaInstanceManager;
	private IPageManager _pageManager;
	private ICategoryActionHelper helper;

}
