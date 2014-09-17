/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content.helper.IContentActionHelper;
import com.agiletec.plugins.jpfastcontentedit.aps.system.JpFastContentEditSystemConstants;

/**
 * @author E.Santoboni
 */
public class IntroNewContentAction extends com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction {
	
	public String entryAction() {
		HttpServletRequest request = this.getRequest();
		request.getSession().setAttribute(JpFastContentEditSystemConstants.FINAL_DEST_PAGE_PARAM_NAME, this.getFinalPageDest());
		String result = "newContent";
		String contentId = this.getContentId();
		if (contentId != null && contentId.length() > 0) {
			Boolean delete = this.getDelete();
			if (delete != null && delete.booleanValue()) {
				result = "deleteContent";
			} else {
				result = "editContent";
			}
		}
		return result;
	}
	
	@Override
	public String createNewVoid() {
		String result = super.createNewVoid();
		Content content = this.getContent();
		IContentActionHelper helper = (IContentActionHelper) this.getContentActionHelper();
		helper.checkTypeLabels(content);
		this.disableAttributes(content);
		String mainGroup = this.getMainGroup();
		if (null != mainGroup 
				&& null != this.getGroupManager().getGroup(mainGroup) 
				&& super.isCurrentUserMemberOf(mainGroup)) {
			content.setMainGroup(mainGroup);
		}
		return result;
	}
	
	protected void disableAttributes(Content content) {
		content.disableAttributes(JpFastContentEditSystemConstants.CONTENT_DISABLING_CODE);
	}
	
	/**
	 * Restituisce la descrizione del contenuto.
	 * @return La descrizione del contenuto.
	 */
	@Override
	public String getContentDescription() {
		return "New Content of type " + this.getContentTypeCode();
	}
	
	@Override
	public String getContentTypeCode() {
		String contentTypeCode = super.getContentTypeCode();
		if (null == contentTypeCode) {
			IContentActionHelper helper = (IContentActionHelper) this.getContentActionHelper();
			contentTypeCode = helper.extractShowletParam(this.getRequest(), JpFastContentEditSystemConstants.TYPE_CODE_SHOWLET_PARAM_NAME);
			super.setContentTypeCode(contentTypeCode);
		}
		return contentTypeCode;
	}
	
	@Override
	public void setContentTypeCode(String contentTypeCode) {
		IContentActionHelper helper = (IContentActionHelper) this.getContentActionHelper();
		String showletTypeCode = helper.extractShowletParam(this.getRequest(), JpFastContentEditSystemConstants.TYPE_CODE_SHOWLET_PARAM_NAME);
		contentTypeCode = (null==showletTypeCode) ? contentTypeCode : showletTypeCode;
		super.setContentTypeCode(contentTypeCode);
	}
	
	/**
	 * Restituisce lo stato del contenuto.
	 * @return Lo stato del contenuto.
	 */
	@Override
	public String getContentStatus() {
		return Content.STATUS_READY;
	}
	
	public String getFinalPageDest() {
		return _finalPageDest;
	}
	public void setFinalPageDest(String finalPageDest) {
		this._finalPageDest = finalPageDest;
	}
	
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	public String getContentId() {
		return _contentId;
	}
	
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	public void setDelete(Boolean delete) {
		this._delete = delete;
	}
	public Boolean getDelete() {
		return _delete;
	}
	
	private String _finalPageDest;
	
	/**
	 * Se presente, verrà effettuato l'edit del contenuto relativo
	 */
	private String _contentId;
	private String _mainGroup;
	private Boolean _delete;
	
}