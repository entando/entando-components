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
