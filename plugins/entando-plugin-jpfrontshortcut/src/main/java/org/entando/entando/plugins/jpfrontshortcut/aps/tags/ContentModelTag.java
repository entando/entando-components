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
package org.entando.entando.plugins.jpfrontshortcut.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

/**
 * @author E.Santoboni
 */
public class ContentModelTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ContentModelTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		try {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
            ApsProperties showletConfig = widget.getConfig();
            String modelId = this.extractModelId(showletConfig, reqCtx);
			if (null != modelId) {
				String var = this.getVar();
				if (null == var || "".equals(var)) {
					this.pageContext.getOut().print(modelId);
				} else {
					this.pageContext.setAttribute(this.getVar(), modelId);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error in doStartTag", t);
		}
		this.release();
		return super.doStartTag();
	}

	protected String extractModelId(ApsProperties showletConfig, RequestContext reqCtx) {
		String modelId = (null != showletConfig) ? (String) showletConfig.get("modelId") : null;
		if (null == modelId) {
			modelId = reqCtx.getRequest().getParameter("modelId");
		}
		if (null == modelId && null != this.getContentId()) {
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.pageContext);
			modelId = contentManager.getDefaultModel(this.getContentId());
		}
		return modelId;
	}

	@Override
	public void release() {
		super.release();
		this._contentId = null;
		this._var = null;
	}

	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	private String _contentId;
	private String _var;

}
