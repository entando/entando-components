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
package com.agiletec.plugins.jpblog.apsadmin.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpblog.aps.system.services.blog.BlogConfig;
import com.agiletec.plugins.jpblog.aps.system.services.blog.IBlogConfig;
import com.agiletec.plugins.jpblog.aps.system.services.blog.IBlogManager;

public class BlogConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogConfigAction.class);
	
	private String checkCategory() {
		String check = null;
		if (null == this.getCatCode() || this.getCatCode().trim().length() == 0) {
			this.addActionError(this.getText("error.jpblob.category.required"));
			return INPUT;
		}
		if (null != this.getCategories() && this.getCategories().size() >0) {
			this.addActionError(this.getText("error.jpblob.category.alreadyPresent"));
			return INPUT;
		}
		return check;
	}

	public String addCategory() {
		try {
			String check = this.checkCategory();
			if (null != check) {
				return check;
			}
			this.getCategories().add(this.getCatCode());
		} catch (Throwable t) {
			_logger.error("error in addCategory", t);
			return FAILURE;
		}
		this.setCatCode(null);
		return SUCCESS;
	}

	public String removeCategory() {
		try {
			this.getCategories().remove(this.getCatCode());
		} catch (Throwable t) {
			_logger.error("error in addCategory", t);
			return FAILURE;
		}
		this.setCatCode(null);
		return SUCCESS;
	}

	public String edit() {
		try {
			IBlogConfig config = this.getBlogManager().getConfig();
//			if (null == config) config = new BlogConfig();
//			this.setConfig((BlogConfig) config);
			this.setCategories(config.getCategories());
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String update() {
		try {
			IBlogConfig config = this.getBlogManager().getConfig();
			if (null != config) {
				config.setCategories(this.getCategories());
				this.getBlogManager().updateConfig(config);
				this.addActionMessage(this.getText("jpblog.message.config.updated"));
			}
		} catch (Throwable t) {
			_logger.error("error in update", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}

	/**
	 * Restituisce la lista di categorie definite nel sistema.
	 * @return La lista di categorie definite nel sistema.
	 */
	public List<Category> getSystemCategories() {
		return this.getCategoryManager().getCategoriesList();
	}
	
	/**
	 * Restutuisce la root delle categorie.
	 * @return La root delle categorie.
	 */
	public Category getCategoryRoot() {
		return this.getCategoryManager().getRoot();
	}

	public BlogConfig getConfig() {
		return _config;
	}
	public void setConfig(BlogConfig config) {
		this._config = config;
	}

	public IBlogManager getBlogManager() {
		return _blogManager;
	}
	public void setBlogManager(IBlogManager blogManager) {
		this._blogManager = blogManager;
	}

	public String getCatCode() {
		return _catCode;
	}
	public void setCatCode(String catCode) {
		this._catCode = catCode;
	}

	public List<String> getCategories() {
		return _categories;
	}
	public void setCategories(List<String> categories) {
		this._categories = categories;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	private BlogConfig _config;
	private IBlogManager _blogManager;
	private String _catCode;
	private List<String> _categories= new ArrayList<String>();
	private ICategoryManager _categoryManager;
}
