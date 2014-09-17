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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.tags;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.helper.IContentWorkFlowActionHelper;
import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

/**
 * @author E.Santoboni
 */
public class ContentStepTag extends StrutsBodyTagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		IContentWorkFlowActionHelper helper = (IContentWorkFlowActionHelper) ApsWebApplicationUtils.getBean("jpcontentworkflowContentActionHelper", pageContext);
		try {
			Content content = this.getContent();
			String stepCode = null;
			if (this.isNextStep()) {
				stepCode = helper.getNextStep(content.getStatus(), content.getTypeCode());
			} else {
				stepCode = helper.getPreviousStep(content.getStatus(), content.getTypeCode());
			}
			if (null != this.getVar()) {
				ValueStack stack = this.getStack();
				stack.getContext().put(this.getVar(), stepCode);
	            stack.setValue("#attr['" + this.getVar() + "']", stepCode, false);
			} else {
				this.pageContext.getOut().print(stepCode);
			}
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
	
	public boolean isNextStep() {
		return _nextStep;
	}
	public void setNextStep(boolean nextStep) {
		this._nextStep = nextStep;
	}
    
	protected String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	private boolean _nextStep;
	
	private String _var;
	
}