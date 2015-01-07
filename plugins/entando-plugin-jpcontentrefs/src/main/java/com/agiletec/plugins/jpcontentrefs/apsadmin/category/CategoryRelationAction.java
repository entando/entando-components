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
package com.agiletec.plugins.jpcontentrefs.apsadmin.category;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jpcontentrefs.aps.system.services.category.IContentCategoryRefManager;
import com.agiletec.plugins.jpcontentrefs.apsadmin.contentrelations.AbstractContentRelationAction;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class CategoryRelationAction extends AbstractContentRelationAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(CategoryRelationAction.class);
	
	public String join() {
		try {
			if (!this.isValidContentType()) return "intro";
			if (!this.isValidCategory()) {
				//TODO MESSAGGIO DI ERRORE
				return SUCCESS;
			}
			Category category = this.getCategoryManager().getCategory(this.getSelectedNode());
			if (!category.getCode().equals(category.getParentCode())) {//se non è la Home
				this.getContentCategoryRefManager().addRelation(this.getSelectedNode(), this.getContentTypeCode());
			}
		} catch (Throwable t) {
			_logger.error("error joining category", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String remove() {
		try {
			if (!this.isValidContentType()) return "intro";
			if (!this.isValidCategory()) return "edit";
			List<ContentRecordVO> referencingContent = this.getReferencingContents();
			if (referencingContent.size()>0) {
				this.setReferences(referencingContent);
				return "references";
			}
			this.getContentCategoryRefManager().removeRelation(this.getSelectedNode(), this.getContentTypeCode());
		} catch (Throwable t) {
			_logger.error("error removing category", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private List<ContentRecordVO> getReferencingContents() throws Throwable {
		List<ContentRecordVO> list = new ArrayList<ContentRecordVO>();
		CategoryUtilizer contentManager = (CategoryUtilizer) this.getContentManager();
		List<String> referencingContents = contentManager.getCategoryUtilizers(this.getSelectedNode());
		for (int i = 0; i < referencingContents.size(); i++) {
			String contentId = referencingContents.get(i);
			if (contentId.startsWith(this.getContentTypeCode())) {
				ContentRecordVO contentVo = this.getContentManager().loadContentVO(contentId);
				if (null != contentVo) {
					list.add(contentVo);
				}
			}
		}
		return list;
	}
	
	private boolean isValidCategory() {
		String catCode = this.getSelectedNode();
		return (catCode != null && this.getCategoryManager().getCategory(catCode) != null);
	}
	
	public List<Category> getCategories(String contentType) {
		return this.getContentCategoryRefManager().getCategories(contentType);
	}
	
	public Category getRoot() {
		return this.getCategoryManager().getRoot();
	}
	
	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	protected IContentCategoryRefManager getContentCategoryRefManager() {
		return _contentCategoryRefManager;
	}
	public void setContentCategoryRefManager(IContentCategoryRefManager contentCategoryManager) {
		this._contentCategoryRefManager = contentCategoryManager;
	}
	
	public String getSelectedNode() {
		return _selectedNode;
	}
	public void setSelectedNode(String selectedNode) {
		this._selectedNode = selectedNode;
	}
	
	public List<ContentRecordVO> getReferences() {
		return _references;
	}
	public void setReferences(List<ContentRecordVO> references) {
		this._references = references;
	}
	
	public ICategoryManager _categoryManager;
	public IContentCategoryRefManager _contentCategoryRefManager;
	
	private String _selectedNode;
	private List<ContentRecordVO> _references;
	
}