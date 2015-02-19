/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.internalservlet.sharedocs;

import java.util.Date;

import org.entando.entando.plugins.jpsharedocs.aps.internalservlet.sharedocs.helper.DocumentActionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class DocumentCommentAction extends BaseAction {
	
	private static Logger _logger = LoggerFactory.getLogger(DocumentCommentAction.class);
	
	@Override
	public void validate() {
		super.validate();
		Content content = this.getContent();
		if (content==null 
				|| !this.getDocHelper().checkAllowedOnContent(content, this.getCurrentUser(), this.getRequest(), this)) {
			_logger.info("User has no permission granted on contents");
			throw new RuntimeException("User not allowed!");
		}
	}
	
	public String entry() {
		try {
			Content content = this.getContent();
			if (content==null 
					|| !this.getDocHelper().checkAllowedOnContent(content, this.getCurrentUser(), this.getRequest(), this)) {
				return USER_NOT_ALLOWED;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "entry");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String saveContent() {
		try {
			Content content = this.createContent();
			this.getContentManager().insertOnLineContent(content);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveContent");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private Content createContent() {
		Content content = this.getContent();
		String langCode = this.getLangManager().getDefaultLang().getCode();
		Date now = new Date();
		
		MonoListAttribute commentiAttr = (MonoListAttribute) content.getAttribute("Comments");
		
		_logger.debug("Comments for this content: {}", commentiAttr.getAttributes().size());
		
		CompositeAttribute lastCommentoAttr = (CompositeAttribute) commentiAttr.addAttribute();
		
		ITextAttribute commentoAttr = (ITextAttribute) lastCommentoAttr.getAttribute("Comment");
		commentoAttr.setText(this.getCommento(), langCode);
		
		ITextAttribute authorAttr = (ITextAttribute) lastCommentoAttr.getAttribute("Author");
		String addedFrom = this.getDocHelper().getUserCompleteName(this.getCurrentUser());
		authorAttr.setText(addedFrom, langCode);
		
		DateAttribute dataCommentoAttr = (DateAttribute) lastCommentoAttr.getAttribute("CommentDate");
		dataCommentoAttr.setDate(now);
		
		return content;
	}
	
	public Content getContent() {
		if (this._content == null) {
			try {
				this._content = this.getContentManager().loadContent(this.getContentId(), false);
			} catch (ApsSystemException e) {
				throw new RuntimeException("Error! Access to content " + this.getContentId() + " not allowed!");
			}
		}
		return this._content;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public String getCommento() {
		return _commento;
	}
	public void setCommento(String commento) {
		this._commento = commento;
	}
	
	/**
	 * Restituisce il manager gestore delle operazioni sui contenuti.
	 * @return Il manager gestore delle operazioni sui contenuti.
	 */
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	/**
	 * Setta il manager gestore delle operazioni sui contenuti.
	 * @param contentManager Il manager gestore delle operazioni sui contenuti.
	 */
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	public DocumentActionHelper getDocHelper() {
		return _docHelper;
	}
	public void setDocHelper(DocumentActionHelper docHelper) {
		this._docHelper = docHelper;
	}
	
	private Content _content;
	
	private String _contentId;
	private String _commento;
	
	private IContentManager _contentManager;
	
	private DocumentActionHelper _docHelper;
	
}