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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.tags;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.IContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.opensymphony.xwork2.util.ValueStack;

public class ContentStepsTag extends StrutsBodyTagSupport {

	@Override
	public int doStartTag() throws JspException {
		IContentWorkflowManager manager = (IContentWorkflowManager) ApsWebApplicationUtils.getBean(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_MANAGER, pageContext);
		try {
			Content content = this.getContent();

			List<Step> steps = manager.getSteps(content.getTypeCode());

			ValueStack stack = this.getStack();
			stack.getContext().put(this.getVar(), steps);
			stack.setValue("#attr['" + this.getVar() + "']", steps, false);

		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error on doStartTag", t);
		}
		return super.doStartTag();
	}

	private Content getContent() {
		ServletRequest request = this.pageContext.getRequest();
		String contentOnSessionMarker = (String) request.getAttribute("contentOnSessionMarker");
		if (null == contentOnSessionMarker || contentOnSessionMarker.trim().length() == 0) {
			contentOnSessionMarker = request.getParameter("contentOnSessionMarker");
		}
		String sessionParamName = ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker;
		return (Content) this.pageContext.getSession().getAttribute(sessionParamName);
	}


	protected String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}


	private String _var;

}