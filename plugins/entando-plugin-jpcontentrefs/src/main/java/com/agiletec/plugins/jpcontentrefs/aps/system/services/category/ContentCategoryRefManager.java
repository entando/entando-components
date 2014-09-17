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
package com.agiletec.plugins.jpcontentrefs.aps.system.services.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentrefs.aps.system.services.JpcontentrefsSystemConstants;
import com.agiletec.plugins.jpcontentrefs.aps.system.services.contentrelations.AbstractContentRefManager;
import com.agiletec.plugins.jpcontentrefs.aps.system.services.contentrelations.AbstractContentRelactionDOM;

/**
 * @author E.Santoboni
 */
public class ContentCategoryRefManager extends AbstractContentRefManager implements IContentCategoryRefManager, CategoryUtilizer {
	
	@Override
	public List<Category> getCategories(String contentType) {
		List<Category> categories = new ArrayList<Category>();
		List<String> categoryCodes = this.getContentTypeElements().get(contentType);
		if (null != categoryCodes) {
			for (int i=0; i<categoryCodes.size(); i++) {
				Category category = this.getCategoryManager().getCategory(categoryCodes.get(i));
				categories.add(category);
			}
			Collections.sort(categories);
		}
		return categories;
	}
	
	@Override
	public List getCategoryUtilizers(String categoryCode) throws ApsSystemException {
		List<SmallContentType> contentTypes = new ArrayList<SmallContentType>();
		try {
			List<String> codes = new ArrayList<String>(this.getContentTypeElements().keySet());
			for (int i=0; i<codes.size(); i++) {
				String contentTypeCode = codes.get(i);
				List<String> categories = this.getContentTypeElements().get(contentTypeCode);
				if (categories.contains(categoryCode)) {
					SmallContentType contenType = (SmallContentType) this.getContentManager().getSmallContentTypesMap().get(contentTypeCode);
					contentTypes.add(contenType);
				}
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Errore", t);
		}
		return contentTypes;
	}
	
	@Override
	protected void checkReference(String elementCode) {
		Category category = this.getCategoryManager().getCategory(elementCode);
		if (null == category) {
			throw new RuntimeException("Codice Categoria '" + elementCode + "' non riconosciuto");
		}
	}
	
	@Override
	public AbstractContentRelactionDOM getConfigDom() throws ApsSystemException {
		return new ContentCategoryRefDOM();
	}
	@Override
	public AbstractContentRelactionDOM getConfigDom(String xml) throws ApsSystemException  {
		return new ContentCategoryRefDOM(xml);
	}
	@Override
	public String getConfigItemName() {
		return JpcontentrefsSystemConstants.CONTENTREFS_CATEGORIES_CONFIG_ITEM;
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	private ICategoryManager _categoryManager;
	
}