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
package com.agiletec.plugins.jpgeoref.aps.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jpgeoref.aps.system.GeoRefSystemConstants;

/**
 * @author E.Santoboni
 */
public class GeoRouteListTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(GeoRouteListTag.class);
	
	/**
	 * Start tag analysis.
	 */
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			Widget showlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			String contentsId = showlet.getConfig().getProperty(GeoRefSystemConstants.ROUTE_CONTENTS_ID_SHOWLET_PARAM);
			this.pageContext.setAttribute(this.getListName(), this.extractContentIdList(contentsId));
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error initialization tag", t);
		}
		return super.doStartTag();
	}

	/**
	 * Returns content id list
	 * @param contentsId contents Id
	 * @return content id list
	 */
	private List<String> extractContentIdList(String contentsId) {
		if (null != contentsId && contentsId.trim().length()>0) {
			String[] contents = contentsId.split(",");
			if (null != contents) {
				List<String> contentList = new ArrayList<String>(contents.length);
				for (int i=0; i<contents.length; i++) {
					contentList.add(contents[i]);
				}
				return contentList;
			}
		}
		return new ArrayList<String>();
	}

	/**
	 * Release the list of identifiers found in pageContext.
	 */
	public void release() {
		this._listName = null;
	}

	/**
	 * Returns the name that is inserted into the list of identifiers found in pageContext.
	 * @return Returns the listName.
	 */
	public String getListName() {
		return _listName;
	}

	/**
	 * Sets the name that is inserted into the list of identifiers found in pageContext.
	 * @param listName The listName to set.
	 */
	public void setListName(String listName) {
		this._listName = listName;
	}

	private String _listName;

}