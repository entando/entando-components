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
package com.agiletec.plugins.jpstats.apsadmin.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import de.laures.cewolf.taglib.html.AbstractHTMLBaseTag;

public abstract class CewolfAbstractHTMLBaseTag extends AbstractHTMLBaseTag {
	
	public int doEndTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		try{
			if(hasBody()){
				writer.write("</" + getTagName() + ">");
			} else {
				if(wellFormed()){
					writer.write(" /");
				}
				writer.write(">");
			}
			return doAfterEndTag(EVAL_PAGE);
		} catch(IOException ioe){
			super.pageContext.getServletContext().log("",ioe);
			throw new JspException(ioe.getMessage());
		}
	}
    
	protected boolean wellFormed() {
		return true;
	}
	
}