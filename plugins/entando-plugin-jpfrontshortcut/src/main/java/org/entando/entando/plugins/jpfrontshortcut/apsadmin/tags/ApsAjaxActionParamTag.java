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
package org.entando.entando.plugins.jpfrontshortcut.apsadmin.tags;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import org.entando.entando.plugins.jpfrontshortcut.apsadmin.tags.util.ApsAjaxActionParamComponent;

/**
 * @author E.Santoboni
 */
public class ApsAjaxActionParamTag extends ComponentTagSupport {
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse resp) {
		return new ApsAjaxActionParamComponent(stack, req);
	}
	
	@Override
	protected void populateParams() {
		super.populateParams();
		ApsAjaxActionParamComponent actionParam = (ApsAjaxActionParamComponent) component;
		actionParam.setAction(this.getAction());
		actionParam.setVar(this.getVar());
	}
	
	/**
	 * Return the name of the variable in the value stack where to store the result.
	 * @return The variable name.
	 */
	public String getVar() {
		return _var;
	}
	
	/**
	 * Set the name of the variable in the value stack where to store the result.
	 * @param var The variable name.
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	/**
	 * Get the action name.
	 * @return
	 */
	public String getAction() {
		return _action;
	}
	
	/**
	 * Set the action name.
	 * @param action
	 */
	public void setAction(String action) {
		this._action = action;
	}
	
	protected String _var;
	protected String _action;
	
}
