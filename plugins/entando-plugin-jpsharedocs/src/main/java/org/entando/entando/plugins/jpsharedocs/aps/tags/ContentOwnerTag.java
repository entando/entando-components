/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.tags;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.entando.entando.plugins.jpsharedocs.aps.system.JpSharedocsSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Return an information of a specified content.
 * The content can will be extracted by id from showlet parameters or from request parameter.
 * The tag extract any specific parameter (by "param" attribute) 
 * or entire {@link ContentAuthorizationInfo} object (setting "var" attribute and anything on "param" attribute).
 * Admitted values for "param" attribute are:<br/>
 * "contentId" returns the code of content id,
 * "mainGroup" returns the code main (owner) group,
 * "authToEdit" returns true if the current user can edit the content (else false).
 * @author E.Santoboni
 */
public class ContentOwnerTag extends OutSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ContentOwnerTag.class);
	
	public ContentOwnerTag() {
		super();
		this.release();
	}
	
	@Override
	public int doStartTag() throws JspException {
		
//		ServletRequest request =  this.pageContext.getRequest();
//		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		HttpSession session = this.pageContext.getSession();
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String creator = null;
		Object lastEditor = null;
		
		try {
			IContentManager contentManager = (ContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.pageContext);
			
			if (null != getContentId()) {
				Content content = contentManager.loadContent(this.getContentId(), true);
				
				if (null != content) {
					lastEditor = content.getLastEditor();
					Object userAttr = content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_USER);
					
					if (null != userAttr
							&& userAttr instanceof MonoTextAttribute) {
						String username = ((MonoTextAttribute)userAttr).getText();
						
						if (null != username 
								&& !"".equals(username)) {
							creator = username;
						}
					}
				}
				if ((null != lastEditor 
						&& currentUser.getUsername().equals(lastEditor))
						|| (null != creator
						&& currentUser.getUsername().equals(creator))) {
					lastEditor = true;
				} else {
					lastEditor = false;
				}
			}
			if (null != lastEditor) {
				String var = this.getVar();
				if (null == var || "".equals(var)) {
					this.pageContext.getOut().print(lastEditor);
				} else {
					this.pageContext.setAttribute(this.getVar(), lastEditor);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error detected while initializing the tag", t);
			throw new JspException("Error detected while initializing the tag", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		this.setContentId(null);
		this.setVar(null);
	}

	/**
	 * Return the content ID.
	 * @return The content id.
	 */
	public String getContentId() {
		return _contentId;
	}
	
	/**
	 * ID of the content to display.
	 * @param id The content id.
	 */
	public void setContentId(String id) {
		this._contentId = id;
	}
	
	/**
	 * Inserts the required parameter (or the entire authorization info object) 
	 * in a variable of the page context with the name provided.
	 * @return The name of the variable.
	 */
	public String getVar() {
		return _var;
	}
	
	/**
	 * Inserts the required parameter (or the entire authorization info object) 
	 * in a variable of the page context with the name provided.
	 * @param var The name of the variable.
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	private String _contentId;
	private String _var;
	
}