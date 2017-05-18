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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
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
	@Deprecated
	public List<SmallContentType> getContentTypes() {
		List<SmallContentType> cts = new ArrayList<SmallContentType>();
		List<SmallEntityType> types = ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getAllowedContentTypes(this.getCurrentUser());
		for (int i = 0; i < types.size(); i++) {
			SmallEntityType set = types.get(i);
			SmallContentType ct = new SmallContentType();
			ct.setCode(set.getCode());
			ct.setDescr(set.getDescription());
			cts.add(ct);
		}
		return cts;
	}
	
	@Override
	public List<SelectItem> getAvalaibleStatus() {
		List<SelectItem> items = new ArrayList<SelectItem>(1);
		SelectItem item = new SelectItem(Content.STATUS_DRAFT, "name.contentStatus." + Content.STATUS_DRAFT);
		items.add(item);
		return items;
	}
	
}