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
