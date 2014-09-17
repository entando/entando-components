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
package com.agiletec.plugins.jpcontentrefs.apsadmin.content;

import java.util.List;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jpcontentrefs.aps.system.services.category.IContentCategoryRefManager;

/**
 * @author E.Santoboni
 */
public class ContentCategoryAction extends com.agiletec.plugins.jacms.apsadmin.content.ContentCategoryAction {
	
	public List<Category> getCategories(String contentType) {
		return this.getContentCategoryRefManager().getCategories(contentType);
	}
	
	protected IContentCategoryRefManager getContentCategoryRefManager() {
		return _contentCategoryRefManager;
	}
	public void setContentCategoryRefManager(IContentCategoryRefManager contentCategoryRefManager) {
		this._contentCategoryRefManager = contentCategoryRefManager;
	}
	
	private IContentCategoryRefManager _contentCategoryRefManager;
	
}