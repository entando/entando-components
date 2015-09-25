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
package com.agiletec.plugins.jpfacetnav.aps.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.Tag;

import com.agiletec.aps.tags.URLTag;

/**
 * Sub tag of URLTag used for adding parameters to query string.
 * Is expected to attribute "name", while the value of the parameter must be specified in the body of the tag.
 * @author E.Santoboni
 */
public class URLParTag extends com.agiletec.aps.tags.ParameterTag {
	
	@Override
	public int doEndTag() throws JspException {
		BodyContent body = this.getBodyContent();
		String value = body.getString();
		URLTag parentTag = null;
		try {
			Tag tag = this;
			do {
				tag = tag.getParent();
			} while (!(tag instanceof URLTag));
			parentTag = (URLTag) tag;
		} catch (RuntimeException e) {
			throw new JspException("Error nesting parameter in url tag.", e);
		}
		parentTag.addParameter(this.getName(), value);
		return EVAL_PAGE;
	}
	
}
