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
