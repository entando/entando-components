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
