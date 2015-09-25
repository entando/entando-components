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
package com.agiletec.plugins.jpfacetnav.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpfacetnav.aps.system.JpFacetNavSystemConstants;
import com.agiletec.plugins.jpfacetnav.aps.system.services.content.widget.IFacetNavHelper;

/**
 * 
 * @author E.Santoboni
 */
public class FacetNodeTitleTag extends OutSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(FacetNodeTitleTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IFacetNavHelper facetNavHelper = (IFacetNavHelper) ApsWebApplicationUtils.getBean(JpFacetNavSystemConstants.CONTENT_FACET_NAV_HELPER, this.pageContext);
			ITreeNodeManager facetManager =  facetNavHelper.getTreeNodeManager();
			ITreeNode facetNode = facetManager.getNode(this.getFacetNodeCode());
			String separator = (this.getSeparator() == null) ? " / " : this.getSeparator();
			if (null != facetNode) {
				Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
				String title = facetNode.getTitles().getProperty(currentLang.getCode());
				if (this.isFullTitle()) {
					title = facetNode.getFullTitle(currentLang.getCode(), separator);
				}
				if (null == title || title.trim().length() == 0) {
					ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
					Lang defaultLang = langManager.getDefaultLang();
					title = facetNode.getTitles().getProperty(defaultLang.getCode());
					if (this.isFullTitle()) {
						title = facetNode.getFullTitle(defaultLang.getCode(), separator);
					}
				}
				if (null == title || title.trim().length() == 0) {
					title = this.getFacetNodeCode();
				}
				
				if (this.getEscapeXml()) {
					out(this.pageContext, this.getEscapeXml(), title);
				} else {
					this.pageContext.getOut().print(title);
				}
			} else this.pageContext.getOut().print("UNKNOWN FACET");
			
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("error in doStartTag", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this._facetNodeCode = null;
		this._fullTitle = false;
		this._separator = " / ";
	}
	
	public String getFacetNodeCode() {
		return _facetNodeCode;
	}
	public void setFacetNodeCode(String facetNodeCode) {
		this._facetNodeCode = facetNodeCode;
	}
	
	public boolean isFullTitle() {
		return _fullTitle;
	}
	public void setFullTitle(boolean fullTitle) {
		this._fullTitle = fullTitle;
	}
	
	public String getSeparator() {
		return _separator;
	}
	public void setSeparator(String separator) {
		this._separator = separator;
	}

	/**
	 * Determina se effettuare l'escape dei caratteri speciali nella label ricavata.
	 * @return True nel caso si debba effettuare l'escape, false in caso contrario.
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	/**
	 * Setta se effettuare l'escape dei caratteri speciali nella label ricavata.
	 * @param escapeXml True nel caso si debba effettuare l'escape, false in caso contrario.
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	
	private String _facetNodeCode;
	private boolean _fullTitle = false;
	private String _separator = " / ";
	
}
