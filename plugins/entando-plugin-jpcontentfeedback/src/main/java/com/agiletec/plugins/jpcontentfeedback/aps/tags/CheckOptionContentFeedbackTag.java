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
package com.agiletec.plugins.jpcontentfeedback.aps.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.portal.specialwidget.ContentFeedbackWidgetAction;

/**
 * Tag di utilità per la verifica dei parametri di configurazione della showlet
 * Visualizza il corpo del tag se la showlet corrente è configurata per la visualizzazione del corrispettivo componente
 * specificato come attributo del tag.
 * @version 1.0
 * @author D.Cherchi
 */
public class CheckOptionContentFeedbackTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(CheckOptionContentFeedbackTag.class);
	
	public int doStartTag() throws JspException {
		try {
			HttpServletRequest request =  (HttpServletRequest) this.pageContext.getRequest();
			boolean hasParam = false;

			if(!hasParam) {
				//showlet
				hasParam = this.extractShowletParam(request);
			}

			if (null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), new Boolean(hasParam));
			}
			if (hasParam) {
				return EVAL_BODY_INCLUDE;
			} else {
				return SKIP_BODY;
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error during tag initialization ", t);
		}
	}

	private Boolean extractShowletParam(HttpServletRequest request) {
		Boolean hasParam = false;
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		Widget showlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
		ApsProperties config = showlet.getConfig();

		String param = this.getParam();
		if (param.equalsIgnoreCase("allowComment")) param = ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ACTIVE;
		if (param.equalsIgnoreCase("allowAnonymousComment")) param = ContentFeedbackWidgetAction.WIDGET_PARAM_COMMENT_ANONYMOUS;
		if (param.equalsIgnoreCase("allowRateContent")) param = ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_CONTENT;
		if (param.equalsIgnoreCase("allowRateComment")) param = ContentFeedbackWidgetAction.WIDGET_PARAM_RATE_COMMENT;

		if (null != config && this.getParam()!=null && this.getParam().length()>0) {
			String value = config.getProperty(this.getParam());
			if (value!= null && value.equalsIgnoreCase("true")) {
				hasParam = true;
			}
		}
		return hasParam;
	}

	@Override
	public void release() {
		this.setVar(null);
		this.setParam(null);
	}

	/**
	 * Setta il nome del parametro tramite il quale settare nella request
	 * il buleano rappresentativo del risultato del controllo di autorizzazione.
	 * @param resultParamName Il nome del parametro.
	 */
	public void setVar(String var) {
		this._var = var;
	}
	/**
	 * Restituisce il nome del parametro tramite il quale settare nella request
	 * il buleano rappresentativo del risultato del controllo di autorizzazione.
	 * @return Il nome del parametro.
	 */
	public String getVar() {
		return _var;
	}

	public void setParam(String param) {
		this._param = param;
	}
	public String getParam() {
		return _param;
	}

	private String _param;
	private String _var;

}
