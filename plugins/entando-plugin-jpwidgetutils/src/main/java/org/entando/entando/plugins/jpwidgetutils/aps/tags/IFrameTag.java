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
