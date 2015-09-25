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
package com.agiletec.plugins.jpcrowdsourcing.apsadmin.idea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;
import com.agiletec.plugins.jpcrowdsourcing.apsadmin.util.SmallCategory;

public class IdeaAction extends BaseAction implements IIdeaAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaAction.class);

	public Category getCategoryRoot() {
		return (Category) this.getCategoryManager().getRoot();
	}

	public Category getCategory(String code) {
		return (Category) this.getCategoryManager().getCategory(code);
	}

	@Override
	public String edit() {
		try {
			Idea idea = (Idea) this.getIdeaManager().getIdea(this.getIdeaId());
			if (null == idea) {
				this.addActionError(this.getText("jpcrowdsourcing.idea.null"));
				return INPUT;
			}
			this.setIdea(idea);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String save() {
		try {
			IIdea idea = this.getIdea();
			this.getIdeaManager().updateIdea(idea);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String joinCategory() {
		try {
			Idea idea =  this.getIdea();
			String categoryCode = this.getTag();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category && !category.getCode().equals(category.getParentCode()) && !idea.getTags().contains(category.getCode())) {
				idea.getTags().add(category.getCode());
			}
		} catch (Throwable t) {
			_logger.error("error in joinCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String removeCategory() {
		try {
			Idea idea =  this.getIdea();
			String categoryCode = this.getTag();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) {
				idea.getTags().remove(category.getCode());
			}
		} catch (Throwable t) {
			_logger.error("error in removeCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public List<SmallCategory> getIdeaTags(boolean completeTitle) {
		List<SmallCategory> categories = new ArrayList<SmallCategory>();
		try {
			String langCode = this.getCurrentLang().getCode();
			String nodeRootCode = this.getIdeaManager().getCategoryRoot();
			categories = this.getCategoryLeaf(nodeRootCode, langCode, completeTitle);
		} catch (Throwable t) {
			_logger.error("Errore loading categories", t);
			throw new RuntimeException("Error loading categories");
		}
		return categories;
	}

	private List<SmallCategory> getCategoryLeaf(String nodeRootCode, String langCode, boolean completeTitle) {
		List<SmallCategory> categories = new ArrayList<SmallCategory>();
		try {
			Category root = (Category) this.getCategoryManager().getCategory(nodeRootCode);
			this.addSmallCategory(categories, root, langCode, completeTitle, true);
		} catch (Throwable t) {
			_logger.error("Errore loading categories leafs", t);
			throw new RuntimeException("Errore loading categories leafs");
		}
		return categories;
	}

	private void addSmallCategory(List<SmallCategory> categories, Category parentCat, String langCode,  boolean completeTitle, boolean isFirst) {
		for (Category cat : parentCat.getChildren()) {
			if (null == cat.getChildren() || cat.getChildren().length == 0) {
				SmallCategory catSmall = new SmallCategory();
				catSmall.setCode(cat.getCode());
				if (!completeTitle) {
					catSmall.setTitle(cat.getTitles().getProperty(langCode));
				} else {
					catSmall.setTitle(cat.getFullTitle(langCode));
				}
				categories.add(catSmall);
			}
			this.addSmallCategory(categories, cat, langCode, completeTitle, false);
		}
	}

	@Override
	public IIdea getIdea(String code) {
		IIdea idea = null;
		try {
			idea = this.getIdeaManager().getIdea(code);
		} catch (Throwable t) {
			_logger.error("Errore in caricamento idea {}", code, t);
			throw new RuntimeException("Errore in caricamento idea " + code);
		}
		return idea;
	}

	public List<IdeaInstance> getIdeaInstances() {
		List<IdeaInstance> ideaInstances = new ArrayList<IdeaInstance>();
		try {
			List<String> codes = this.getIdeaInstanceManager().getIdeaInstances(null, null);
			if (null != codes && !codes.isEmpty()) {
				Iterator<String> it = codes.iterator();
				while (it.hasNext()) {
					String code = it.next();
					IdeaInstance ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(code);
					if (null != ideaInstance) {
						ideaInstances.add(ideaInstance);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in getIdeaInstances", t);
			throw new RuntimeException("error in getIdeaInstances");
		}
		return ideaInstances;
	}

	public void setIdea(Idea idea) {
		this._idea = idea;
	}
	public Idea getIdea() {
		return _idea;
	}

	public void setIdeaId(String ideaId) {
		this._ideaId = ideaId;
	}
	public String getIdeaId() {
		return _ideaId;
	}

	public void setTag(String tag) {
		this._tag = tag;
	}
	public String getTag() {
		return _tag;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}
	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}

	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}

	private IIdeaManager _ideaManager;
	private IIdeaInstanceManager _ideaInstanceManager;
	private String _ideaId;
	private Idea _idea;
	private String _tag;
	private ICategoryManager _categoryManager;


}
