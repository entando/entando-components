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
package com.agiletec.plugins.jpcontentrefs.apsadmin.category;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jpcontentrefs.aps.system.services.category.IContentCategoryRefManager;
import com.agiletec.plugins.jpcontentrefs.apsadmin.contentrelations.AbstractContentRelationAction;

/**
 * @author E.Santoboni
 */
public class CategoryRelationAction extends AbstractContentRelationAction {
	
	@Override
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
			ApsSystemUtils.logThrowable(t, this, "join");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
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
			ApsSystemUtils.logThrowable(t, this, "remove");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private List<ContentRecordVO> getReferencingContents() throws Throwable {
		List<ContentRecordVO> list = new ArrayList<ContentRecordVO>();
		CategoryUtilizer contentManager = (CategoryUtilizer) this.getContentManager();
		List<ContentRecordVO> referencingContents = contentManager.getCategoryUtilizers(this.getSelectedNode());
		for (ContentRecordVO contentVo : referencingContents) {
			if (contentVo.getTypeCode().equals(this.getContentTypeCode())) {
				list.add(contentVo);
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