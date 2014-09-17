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

import com.agiletec.apsadmin.tags.ApsActionParamSubTag;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;

import org.entando.entando.plugins.jpfrontshortcut.apsadmin.tags.util.ApsAjaxActionParamComponent;

/**
 * @author E.Santoboni
 */
public class ApsAjaxActionParamSubTag extends ApsActionParamSubTag {
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse resp) {
		ApsAjaxActionParamTag parent = (ApsAjaxActionParamTag) findAncestorWithClass(this, ApsAjaxActionParamTag.class);
		return parent.getComponent();
	}
	
	@Override
	protected void populateParams() {
		super.populateParams();
		ApsAjaxActionParamComponent component = (ApsAjaxActionParamComponent) this.getComponent();
		component.setParam(this.getName(), this.getValue());
	}
	
}