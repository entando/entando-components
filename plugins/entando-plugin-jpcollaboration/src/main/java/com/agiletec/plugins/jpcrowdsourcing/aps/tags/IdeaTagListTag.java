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
package com.agiletec.plugins.jpcrowdsourcing.aps.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.JpCrowdSourcingSystemConstants;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.CategoryInfoBean;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;

public class IdeaTagListTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaTagListTag.class);

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			List<CategoryInfoBean> list = this.loadTags(reqCtx);
			this.pageContext.setAttribute(this.getVar(), list);
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Errore tag", t);
		}
		this.release();
		return super.doEndTag();
	}

	public List<CategoryInfoBean> loadTags(RequestContext reqCtx) {
		List<CategoryInfoBean> beans = new ArrayList<CategoryInfoBean>();
		try {
			IIdeaManager ideaManager = (IIdeaManager) ApsWebApplicationUtils.getBean(JpCrowdSourcingSystemConstants.IDEA_MANAGER, this.pageContext);
			ICategoryManager categoryManager = (ICategoryManager) ApsWebApplicationUtils.getBean(SystemConstants.CATEGORY_MANAGER, this.pageContext);
			Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			Map<String, Integer> fullList = ideaManager.getIdeaTags(IIdea.STATUS_APPROVED);

			//boolean filterByRootNode = null != this.getOnlyCrawdSourcingNode() && this.getOnlyCrawdSourcingNode().equalsIgnoreCase("true");
			boolean filterByRootNode = null != this.getCategoryFilterType() && (this.getCategoryFilterType().equals("tag") || this.getCategoryFilterType().equals("cloudTag"));
			
			
			
			boolean onlyLeaf = null != this.getOnlyLeaf() && this.getOnlyLeaf().equalsIgnoreCase("true");
			String serviceCatagoryRoot = null;
			if (filterByRootNode) {
				if (this.getCategoryFilterType().equals("tag")) {
					serviceCatagoryRoot = ideaManager.getCategoryRoot();
				} else {
					_logger.warn("Invalid parameter for 'CategoryFilterType' with value {}. Allowed values are 'tag' or 'tagCloud'", this.getCategoryFilterType());
					filterByRootNode = false;
				}
				
			}
			Iterator<String> it = fullList.keySet().iterator();
			while (it.hasNext()) {
				Category cat = categoryManager.getCategory(it.next());
				if (null != cat) {
					if (filterByRootNode || onlyLeaf) {
						boolean okForRoot = false;
						boolean okForLeaf = false;
						if (filterByRootNode) {
							if (cat.isChildOf(serviceCatagoryRoot)) {
								okForRoot = true;
							}
						} else {
							okForRoot = true;
						}
						if (onlyLeaf) {
							if (null == cat.getChildren() || cat.getChildren().length == 0) {
								okForLeaf = true;
							} else {
								okForLeaf = false;
							}
						} else {
							okForLeaf = true;
						}
						if (okForLeaf && okForRoot) {
							beans.add(new CategoryInfoBean(cat, currentLang.getCode(), fullList.get(cat.getCode())));
						}

					} else {
						beans.add(new CategoryInfoBean(cat, currentLang.getCode(), fullList.get(cat.getCode())));
					}
				}
			}
			BeanComparator comparator = new BeanComparator("title");
			Collections.sort(beans, comparator);
		} catch (Throwable t) {
			_logger.error("Error loading Tags per ideas", t);
			throw new RuntimeException("Errore in caricamento Tags per idee");
		}
		return beans;
	}

	@Override
	public void release() {
		this.setCategoryFilterType(null);
		this.setOnlyLeaf(null);
	}

	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public void setOnlyLeaf(String onlyLeaf) {
		this._onlyLeaf = onlyLeaf;
	}
	public String getOnlyLeaf() {
		return _onlyLeaf;
	}

	public void setCategoryFilterType(String categoryFilterType) {
		this._categoryFilterType = categoryFilterType;
	}
	public String getCategoryFilterType() {
		return _categoryFilterType;
	}
	
	private String _var;
	private String _onlyLeaf;
	private String _categoryFilterType;

}
