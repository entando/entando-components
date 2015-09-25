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
package com.agiletec.plugins.jpmyportalplus.aps.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;

/**
 * @author E.Santoboni
 */
public class CheckFrameStatusTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(CheckFrameStatusTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest req =  (HttpServletRequest) this.pageContext.getRequest();
		Integer[] customShowletStatus = null;
		try {
			CustomPageConfig customPageConfig = 
				(CustomPageConfig) req.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
			if (null != customPageConfig) {
				customShowletStatus = customPageConfig.getStatus();
			}
			RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
			Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			if (customShowletStatus != null) {
				int status = customShowletStatus[currentFrame] == null ? 0 : customShowletStatus[currentFrame].intValue();
				if (IPageUserConfigManager.STATUS_CLOSE == status) {
					return EVAL_BODY_INCLUDE;
				} else if (IPageUserConfigManager.STATUS_OPEN == status) {
					return SKIP_BODY;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error on doStartTag", t);
			throw new JspException("Error on doStartTag", t);
		}
		return SKIP_BODY;
	}
	
}
