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
package org.entando.entando.plugins.jpwebform.apsadmin.portal.specialwidget.webform;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;

/**
 * @author S.Loru
 */
public class WebFormListWidgetAction extends SimpleWidgetConfigAction  {

	private static final Logger _logger =  LoggerFactory.getLogger(WebFormListWidgetAction.class);

	@Override
	public String init() {
		String result = super.init();
		try {
			Boolean status = null;
			String statusString = this.getWidget().getConfig().getProperty(JpwebformSystemConstants.WIDGET_PARAM_STATUS);
			if(null != statusString || statusString.isEmpty()){
				status = Boolean.valueOf(statusString);
			}
			this.setStatus(status);
			String maxElements = this.getWidget().getConfig().getProperty(JpwebformSystemConstants.WIDGET_PARAM_MAXEELEMENTS);
			this.setMaxElements(maxElements);
			String maxElemForItem = this.getWidget().getConfig().getProperty(JpwebformSystemConstants.WIDGET_PARAM_MAXELEMFORITEM);
			this.setMaxElemForItem(maxElemForItem);
		} catch (Throwable t) {
			_logger.error("error in init", t);
		}
		return result;

	}


	@Override
	public void validate() {
		super.validate();
		try {
			if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
				this.setShowlet(super.createNewShowlet());
				return;
			}
			this.validateTitle();
			this.validateLink();
		} catch (Throwable t) {
			_logger.error("Error validating list viewer", t);
		}
	}

	protected void validateTitle() {
		String titleParamPrefix = IContentListWidgetHelper.WIDGET_PARAM_TITLE + "_";
		if (this.isMultilanguageParamValued(titleParamPrefix)) {
			Lang defaultLang = this.getLangManager().getDefaultLang();
			String defaultTitleParam = titleParamPrefix + defaultLang.getCode();
			String defaultTitle = this.getWidget().getConfig().getProperty(defaultTitleParam);
			if (defaultTitle == null || defaultTitle.length() == 0) {
				String[] args = {defaultLang.getDescr()};
				this.addFieldError(defaultTitleParam, this.getText("error.widget.listViewer.defaultLangTitle.required", args));
			}
		}
	}

	protected void validateLink() {
		String pageLink = this.getWidget().getConfig().getProperty(IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK);
		boolean existsPageLink = pageLink != null && this.getPage(pageLink) != null;
		String linkDescrParamPrefix = IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK_DESCR + "_";
		if (existsPageLink || this.isMultilanguageParamValued(linkDescrParamPrefix)) {
			if (!existsPageLink) {
				this.addFieldError(IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK, this.getText("error.widget.listViewer.pageLink.required"));
			}
			Lang defaultLang = this.getLangManager().getDefaultLang();
			String defaultLinkDescrParam = linkDescrParamPrefix + defaultLang.getCode();
			String defaultLinkDescr = this.getWidget().getConfig().getProperty(defaultLinkDescrParam);
			if (defaultLinkDescr == null || defaultLinkDescr.length() == 0) {
				String[] args = {defaultLang.getDescr()};
				this.addFieldError(defaultLinkDescrParam, this.getText("error.widget.listViewer.defaultLangLink.required", args));
			}
		}
	}

	private boolean isMultilanguageParamValued(String prefix) {
		ApsProperties config = this.getWidget().getConfig();
		if (null == config) {
			return false;
		}
		for (int i = 0; i < this.getLangs().size(); i++) {
			Lang lang = this.getLangs().get(i);
			String paramValue = config.getProperty(prefix+lang.getCode());
			if (null != paramValue && paramValue.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}

	public List<IPage> getPages() {
		if (this._pages == null) {
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

	public IFormManager getFormManager() {
		return _formManager;
	}

	public void setFormManager(IFormManager formManager) {
		this._formManager = formManager;
	}

	public String getMaxElemForItem() {
		return _maxElemForItem;
	}

	public void setMaxElemForItem(String maxElemForItem) {
		this._maxElemForItem = maxElemForItem;
	}

	public String getMaxElements() {
		return _maxElements;
	}

	public void setMaxElements(String maxElements) {
		this._maxElements = maxElements;
	}

	public Boolean getStatus() {
		return _status;
	}

	public void setStatus(Boolean _status) {
		this._status = _status;
	}

	private String _maxElemForItem;
	private String _maxElements;
	private List<IPage> _pages;
	private IFormManager _formManager;
	private Boolean _status;
}
