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
package com.agiletec.plugins.jpcrowdsourcing.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Returns informations about the showlet where the tag resides.
 * The "param" attribute acceptes the following values:
 * - "code" returns the code of the associated showlet type (empty if none associated)<br/>
 * - "title" returns the name of the associated showlet type (empty if none associated)<br/>
 * - "config" returns the value of the configuration parameter declared in the "configParam" attribute<br/>
 * To obtain information about a showlet placed in a frame other than the current, use the "frame" attribute.
 * @author E.Santoboni - E.Mezzano
 */
@SuppressWarnings("serial")
public class CurrentPageWidgetTag extends OutSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(CurrentPageWidgetTag.class);

	@Override
	public int doStartTag() throws JspException {
		try {
			Widget showlet = this.extractShowlet();
			if (null == showlet) return super.doStartTag();
			String value = null;
			if ("code".equals(this.getParam())) {
				value = showlet.getType().getCode();
			} else if ("title".equals(this.getParam())) {
				value = this.extractTitle(showlet);
			} else if ("config".equals(this.getParam())) {
				ApsProperties config = showlet.getConfig();
				if (null != config) {
					value = config.getProperty(this.getConfigParam());
				}
			}
			if (null != value) {
				String var = this.getVar();
				if (null == var || "".equals(var)) {
					if (this.getEscapeXml()) {
						out(this.pageContext, this.getEscapeXml(), value);
					} else {
						this.pageContext.getOut().print(value);
					}
				} else {
					this.pageContext.setAttribute(this.getVar(), value);
				}
			}
		} catch (Throwable t) {
			String msg = "Error detected during showlet preprocessing";
			_logger.error("error in doEndTag", t);
			throw new JspException(msg, t);
		}
		return super.doStartTag();
	}

	private String extractTitle(Widget showlet) {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		WidgetType type = showlet.getType();
		String value = type.getTitles().getProperty(currentLang.getCode());
		if (null == value || value.trim().length() == 0) {
			ILangManager langManager =
				(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			Lang defaultLang = langManager.getDefaultLang();
			value = type.getTitles().getProperty(defaultLang.getCode());
		}
		return value;
	}

	private Widget extractShowlet() {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		Widget showlet = null;
		if (this.getFrame() < 0 && StringUtils.isBlank(this.getWidget())) {
			showlet = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		} else {
			IPage currentPage = (IPage) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
			Widget[] showlets = currentPage.getWidgets();
			if (this.getFrame() >= 0) {
				if (showlets.length > this.getFrame()) {
					showlet = showlets[this.getFrame()];
				}
			} else {
				for (int i = 0; i < showlets.length; i++) {
					Widget currentWidget = showlets[i];
					if (null != currentWidget && currentWidget.getType().getCode().equals(this.getWidget())) {
						showlet = currentWidget;
						break;
					}
				}
			}
		}
		return showlet;
	}

	@Override
	public void release() {
		super.release();
		this._param = null;
		this._configParam = null;
		this._var = null;
		this._frame = -1;
		this._widget = null;
		super.escapeXml = true;

	}

	public String getParam() {
		return _param;
	}
	public void setParam(String param) {
		this._param = param;
	}

	public String getConfigParam() {
		return _configParam;
	}
	public void setConfigParam(String configParam) {
		this._configParam = configParam;
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	public int getFrame() {
		return _frame;
	}
	public void setFrame(int frame) {
		this._frame = frame;
	}

	/**
	 * Checks if the special characters must be escaped
	 * @return True if the special characters must be escaped
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}

	/**
	 * Toggles the escape of the special characters of the result.
	 * @param escapeXml True to perform the escaping, false otherwise.
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}


	public String getWidget() {
		return _widget;
	}
	public void setWidget(String widget) {
		this._widget = widget;
	}



	private String _param;
	private String _configParam;
	private String _var;

	private int _frame = -1;
	private String _widget;

}
