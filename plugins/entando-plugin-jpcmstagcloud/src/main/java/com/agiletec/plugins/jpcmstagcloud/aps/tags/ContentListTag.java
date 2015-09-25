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
package com.agiletec.plugins.jpcmstagcloud.aps.tags;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcmstagcloud.aps.system.JpcmstagcloudSystemConstants;
import com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud.ITagCloudManager;

/**
 * @author E.Santoboni
 */
public class ContentListTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(ContentListTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ITagCloudManager tagCloudManager = (ITagCloudManager) ApsWebApplicationUtils.getBean(JpcmstagcloudSystemConstants.TAG_CLOUD_MANAGER, this.pageContext); 
		ServletRequest request =  this.pageContext.getRequest();
		try {
			UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			String tagCategoryCode = request.getParameter("tagCategoryCode");
			List<String> contentsId = tagCloudManager.loadPublicTaggedContentsId(tagCategoryCode, currentUser);
			this.pageContext.setAttribute(this.getListName(), contentsId);
			request.setAttribute("tagCategoryCode", tagCategoryCode);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("error in doStartTag", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this.setListName(null);
	}
	
	/**
	 * Restituisce il nome con il quale viene inserita nel pageContext
	 * la lista degli identificativi trovati.
	 * @return Returns the listName.
	 */
	public String getListName() {
		return _listName;
	}
	
	/**
	 * Setta il nome con il quale viene inserita nel pageContext
	 * la lista degli identificativi trovati.
	 * @param listName The listName to set.
	 */
	public void setListName(String listName) {
		this._listName = listName;
	}
	
	private String _listName;
	
}
