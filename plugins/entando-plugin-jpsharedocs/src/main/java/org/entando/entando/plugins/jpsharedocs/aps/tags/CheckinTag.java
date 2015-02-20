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
package org.entando.entando.plugins.jpsharedocs.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jpsharedocs.aps.system.JpSharedocsSystemConstants;
import org.entando.entando.plugins.jpsharedocs.aps.system.services.checkin.ContentCheckin;
import org.entando.entando.plugins.jpsharedocs.aps.system.services.checkin.IContentCheckinManager;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class CheckinTag extends TagSupport {
	
	public CheckinTag() {
		super();
		this.release();
	}
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IContentCheckinManager checkinManager = (IContentCheckinManager) ApsWebApplicationUtils.getBean(JpSharedocsSystemConstants.CHECK_IN_MANAGER, this.pageContext);
			ContentCheckin checkin = checkinManager.getContentCheckin(this.extractContentId(this.getContentId(), reqCtx));
			this.pageContext.setAttribute(this.getVar(), checkin);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error detected while initialising the tag", t);
		}
		return EVAL_PAGE;
	}
	
	protected String extractContentId(String contentId, RequestContext reqCtx) {
		if (null == contentId) {
			if (null == contentId) {
				contentId = reqCtx.getRequest().getParameter(SystemConstants.K_CONTENT_ID_PARAM);
			}
		}
		return contentId;
	}
	
	@Override
	public void release() {
		this.setContentId(null);
		this.setVar(null);
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String id) {
		this._contentId = id;
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