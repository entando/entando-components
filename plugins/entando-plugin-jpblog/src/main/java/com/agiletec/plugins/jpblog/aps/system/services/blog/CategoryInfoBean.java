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
package com.agiletec.plugins.jpblog.aps.system.services.blog;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jpblog.aps.tags.BlogCategoryTag;

/**
 * Utility bean for the tag {@link BlogCategoryTag}
 */
public class CategoryInfoBean {
	
	public CategoryInfoBean(Category category, String langCode, int occurrences) {
		this._category = category;
		this._langCode = langCode;
		this._occurrences = occurrences;
	}
	
	public String getTitle() {
		String title = this.getCategory().getTitle(this.getLangCode());
		if (null == title) {
			title = this.getCategory().getCode();
		}
		return title;
	}
	
	public Category getCategory() {
		return _category;
	}
	public void setCategory(Category category) {
		this._category = category;
	}
	
	public String getLangCode() {
		return _langCode;
	}
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	public void setOccurrences(int occurrences) {
		this._occurrences = occurrences;
	}
	public int getOccurrences() {
		return _occurrences;
	}
	
	private Category _category;
	private String _langCode;
	private int _occurrences;
	
}
