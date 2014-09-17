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

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;

import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.IMyPortalPageModelManager;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * @author E.Santoboni
 */
public class ColumnInfoTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ColumnInfoTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			IPage currPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			String value = null;
			if (null == this.getParamName() || this.getParamName().equals(PARAM_NAME_FIRST_FRAME_ID)) {
				IMyPortalPageModelManager myportalModelConfigManager = 
						(IMyPortalPageModelManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.MYPORTAL_MODEL_CONFIG_MANAGER, this.pageContext);
				PageModel model = currPage.getModel();
				Map<Integer, MyPortalFrameConfig> modelConfig = myportalModelConfigManager.getPageModelConfig(model.getCode());
				Frame[] frames = model.getConfiguration();
				for (int i = 0; i < frames.length; i++) {
					Frame frame = frames[i];
					MyPortalFrameConfig frameConfig = (null != modelConfig) ? modelConfig.get(i) : null;
					if (null != frameConfig && null != frameConfig.getColumn() && (frameConfig.getColumn().equals(this.getColumnId()))) {
						value = String.valueOf(frame.getPos());
					}
					if (null != value) break;
				}
			}
			if (null != value) {
				String var = this.getVar();
				if (null == var || "".equals(var)) {
					this.pageContext.getOut().print(value);
				} else {
					this.pageContext.setAttribute(this.getVar(), value);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in ", t);
			throw new JspException("Error in doStartTag", t);
		}
		return super.doStartTag();
	}

	public Integer getColumnId() {
		return _columnId;
	}
	public void setColumnId(Integer columnId) {
		this._columnId = columnId;
	}

	public String getParamName() {
		return _paramName;
	}
	public void setParamName(String paramName) {
		this._paramName = paramName;
	}

	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}

	private Integer _columnId;
	private String _paramName;
	private String var;

	public static final String PARAM_NAME_FIRST_FRAME_ID = "firstFrameId";

}
