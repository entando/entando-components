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