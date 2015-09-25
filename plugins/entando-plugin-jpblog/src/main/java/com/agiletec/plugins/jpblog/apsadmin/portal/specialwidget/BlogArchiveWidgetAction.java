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
package com.agiletec.plugins.jpblog.apsadmin.portal.specialwidget;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jpblog.aps.system.JpblogSystemConstants;

public class BlogArchiveWidgetAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogArchiveWidgetAction.class);

	@Override
	public String save() {
		return super.save();
	}

	public String configContentType() {
		try {
			Widget showlet = super.createNewShowlet();
			showlet.getConfig().setProperty(IContentListWidgetHelper.WIDGET_PARAM_CONTENT_TYPE, this.getContentType());
			this.setShowlet(showlet);
		} catch (Throwable t) {
			_logger.error("error in init", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	

	public String changeContentType() {
		try {
			Widget showlet = super.createNewShowlet();
			this.setShowlet(showlet);
		} catch (Throwable t) {
			_logger.error("error in changeContentType", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	

	/**
	 * Restituisce la lista di contenuti (in forma small) definiti nel sistema.
	 * Il metodo è a servizio delle jsp che richiedono questo dato per fornire
	 * una corretta visualizzazione della pagina.
	 * @return La lista di tipi di contenuto (in forma small) definiti nel sistema.
	 */
	public List<SmallContentType> getContentTypes() {
		List<SmallContentType> types = new ArrayList<SmallContentType>();
		List<SmallContentType> systemTypes = this.getContentManager().getSmallContentTypes();
		
		if (null != systemTypes) {
			for (int i = 0; i < systemTypes.size(); i++) {
				SmallContentType current = systemTypes.get(i);
				Content content = this.getContentManager().createContentType(current.getCode());
				boolean hasAuthor = null != content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_AUTHOR);
				boolean hasBody = null != content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_BODY);
				boolean hasDate= null != content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE);
				boolean hasTitle = null != content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_TITLE);
				if (hasAuthor && hasBody && hasDate && hasTitle) {
					types.add(current);
				}
			}
		}
		return types;
	}
	
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	private String _contentType;
	private IContentManager _contentManager;
}
