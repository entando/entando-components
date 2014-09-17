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
