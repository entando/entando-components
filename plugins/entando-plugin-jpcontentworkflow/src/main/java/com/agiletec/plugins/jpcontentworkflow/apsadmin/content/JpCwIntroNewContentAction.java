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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.helper.IContentWorkFlowActionHelper;

/**
 * @author E.Santoboni
 */
public class JpCwIntroNewContentAction extends com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction {
	
	@Override
	public void validate() {
		super.validate();
		this.checkContentType();
	}
	
	protected void checkContentType() {
		try {
			List<SmallContentType> allowedContentTypes = this.getContentTypes();
			boolean check = false;
			for (int i = 0; i < allowedContentTypes.size(); i++) {
				SmallContentType contentType = allowedContentTypes.get(i);
				if (contentType.getCode().equals(this.getContentTypeCode())) {
					check = true;
					break;
				}
			}
			if (!check) {
				SmallContentType contentType = (SmallContentType) this.getContentManager().getSmallContentTypesMap().get(this.getContentTypeCode());
				String typeDescr = contentType != null ? contentType.getCode() : this.getContentTypeCode();
				this.addFieldError("contentTypeCode", this.getText("error.content.contentType.userNotAllowed", typeDescr));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllowedContentTypes", "Error checking content type authorization");
        	throw new RuntimeException("Error extracting allowed content types", t);
		}
		
	}
	
	@Override
	public String getContentStatus() {
		return Content.STATUS_DRAFT;
	}
	
	@Override
	public List<SmallContentType> getContentTypes() {
		return ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getAllowedContentTypes(this.getCurrentUser());
	}
	
	@Override
	public List<SelectItem> getAvalaibleStatus() {
		List<SelectItem> items = new ArrayList<SelectItem>(1);
		SelectItem item = new SelectItem(Content.STATUS_DRAFT, "name.contentStatus." + Content.STATUS_DRAFT);
		items.add(item);
		return items;
	}
	
}