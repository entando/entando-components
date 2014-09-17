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
package org.entando.entando.plugins.jpfrontshortcut.apsadmin.tags.util;

import org.entando.entando.plugins.jpfrontshortcut.apsadmin.util.ApsAjaxActionParamsUtil;
import com.agiletec.apsadmin.tags.util.ApsActionParamComponent;

import com.opensymphony.xwork2.util.ValueStack;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsException;

/**
 * @author E.Santoboni
 */
public class ApsAjaxActionParamComponent extends ApsActionParamComponent {
	
	public ApsAjaxActionParamComponent(ValueStack stack, HttpServletRequest req) {
		super(stack, req);
		this._req = req;
	}
	
	@Override
	public boolean end(Writer writer, String body) {
		String actionParam = ApsAjaxActionParamsUtil.createApsActionParam(this.getAction(), this.getParams());
		if (this.getVar() != null) {
            this.getStack().getContext().put(this.getVar(), actionParam);
            // add to the request and page scopes as well
            this._req.setAttribute(this.getVar(), actionParam);
        } else {
            try {
            	writer.write(actionParam);
            } catch (IOException e) {
                throw new StrutsException("IOError: " + e.getMessage(), e);
            }
        }
		return true;
	}
	
	private HttpServletRequest _req;
	
}
