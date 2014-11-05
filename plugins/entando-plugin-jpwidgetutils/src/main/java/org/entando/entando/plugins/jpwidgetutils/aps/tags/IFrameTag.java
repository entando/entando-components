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
package org.entando.entando.plugins.jpwidgetutils.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.plugins.jpwidgetutils.aps.system.JpWidgetUtilSystemConstants;

/**
 * @author E.Santoboni
 */
public class IFrameTag  extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		try {
			Widget showlet = this.extractShowlet();
			ApsProperties properties = showlet.getConfig();
			StringBuilder buffer = new StringBuilder();
			if (null != properties) {
				String url = properties.getProperty(JpWidgetUtilSystemConstants.URL_SHOWLET_PARAM);
				if (null != url) {
					buffer.append(url);
					String usernameParam = properties.getProperty(JpWidgetUtilSystemConstants.USERNAME_PARAM_SHOWLET_PARAM);
					String username = properties.getProperty(JpWidgetUtilSystemConstants.USERNAME_SHOWLET_PARAM);
					String passwordParam = properties.getProperty(JpWidgetUtilSystemConstants.PASSWORD_PARAM_SHOWLET_PARAM);
					String password = properties.getProperty(JpWidgetUtilSystemConstants.PASSWORD_SHOWLET_PARAM);
					if (null != usernameParam && null != username && null != passwordParam && null != password) {
						if (buffer.indexOf("?") > 0) {
							buffer.append("&");
						} else {
							buffer.append("?");
						}
						buffer.append(usernameParam).append("=").append(username)
								.append("&").append(passwordParam).append("=").append(password);
					}
				}
			}
			this.pageContext.getOut().print(buffer.toString());
		} catch (Throwable t) {
			String msg = "Error detected during showlet preprocessing";
			ApsSystemUtils.logThrowable(t, this, "doEndTag", msg);
			throw new JspException(msg, t);
		}
		return super.doStartTag();
	}
	
	private Widget extractShowlet() {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		Widget showlet = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_SHOWLET));
		return showlet;
	}
	
}
