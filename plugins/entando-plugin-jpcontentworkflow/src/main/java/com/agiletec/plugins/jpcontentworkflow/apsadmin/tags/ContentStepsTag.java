/*
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando Enterprise Edition software.
 * You can redistribute it and/or modify it
 * under the terms of the Entando's EULA
 * 
 * See the file License for the specific language governing permissions   
 * and limitations under the License
 * 
 * 
 * 
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
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