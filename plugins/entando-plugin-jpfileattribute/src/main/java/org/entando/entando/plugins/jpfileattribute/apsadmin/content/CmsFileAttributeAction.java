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
package org.entando.entando.plugins.jpfileattribute.apsadmin.content;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.lang.Lang;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.helper.IContentActionHelper;

import javax.servlet.http.HttpSession;

import org.entando.entando.plugins.jpfileattribute.apsadmin.entity.attribute.FileAttributeAction;
import org.entando.entando.plugins.jpfileattribute.apsadmin.entity.attribute.FileAttributeActionHelper;

/**
 * @author E.Santoboni
 */
public class CmsFileAttributeAction extends FileAttributeAction {
	
	public String backToEntryContent() {
		String anchorDest = buildEntryContentAnchorDest();
		this.setEntryContentAnchorDest(anchorDest);
		FileAttributeActionHelper.removeSessionParams(this.getRequest().getSession());
		return SUCCESS;
	}
	
	@Override
	protected void buildEntryEntityAnchorDest() {
		String anchorDest = buildEntryContentAnchorDest();
		this.setEntryContentAnchorDest(anchorDest);
	}
	
	private String buildEntryContentAnchorDest() {
		HttpSession session = this.getRequest().getSession();
		StringBuilder buffer = new StringBuilder("contentedit_");
		//StringBuilder buffer = new StringBuilder(anchorPrefix);
		buffer.append(this.getLangManager().getDefaultLang().getCode());
		buffer.append("_").append(session.getAttribute(FileAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM));
		return buffer.toString();
	}
	
	public String getEntryContentAnchorDestFromRemove() {
		StringBuilder buffer = new StringBuilder("contentedit_");
		Lang defaultLang = this.getLangManager().getDefaultLang();
		buffer.append(defaultLang.getCode());
		buffer.append("_");
		if (null != this.getParentAttributeName()) {
			buffer.append(this.getParentAttributeName());
		} else {
			buffer.append(this.getAttributeName());
		}
		return buffer.toString();
	}
	
	@Override
	protected void updateEntity() {
		this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
	}
	
	@Override
	protected IApsEntity getEntity() {
		return this.getContent();
	}
	
	/**
	 * Restituisce il contenuto in sesione.
	 * @return Il contenuto in sesione.
	 */
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
	}
	
	public String getContentOnSessionMarker() {
		return _contentOnSessionMarker;
	}
	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this._contentOnSessionMarker = contentOnSessionMarker;
	}
	
	public String getEntryContentAnchorDest() {
		if (null == this._entryContentAnchorDest) {
			String anchorDest = buildEntryContentAnchorDest();
			this.setEntryContentAnchorDest(anchorDest);
		}
		return _entryContentAnchorDest;
	}
	protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
		this._entryContentAnchorDest = entryContentAnchorDest;
	}
	
	protected IContentActionHelper getContentActionHelper() {
		return _contentActionHelper;
	}
	public void setContentActionHelper(IContentActionHelper contentActionHelper) {
		this._contentActionHelper = contentActionHelper;
	}
	
	private String _contentOnSessionMarker;
	
	private String _entryContentAnchorDest;
	
	private IContentActionHelper _contentActionHelper;
	
}
