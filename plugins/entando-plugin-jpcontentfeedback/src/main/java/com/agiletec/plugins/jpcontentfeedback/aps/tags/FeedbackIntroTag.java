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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.InternalServletTag;

/**
 * Tag che consente la visualisualizzazione del blocco jpcontentFeedback per la publicazione del rating del contenuto,
 * dei commenti e del rating dei commenti
 * @author D.Cherchi
 */
public class FeedbackIntroTag extends InternalServletTag {

	private static final Logger _logger = LoggerFactory.getLogger(FeedbackIntroTag.class);
	
	@Override
	protected void includeWidget(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget widget) throws ServletException, IOException {
		HttpServletRequest request = reqCtx.getRequest();
		try {
			String actionPath = this.extractIntroActionPath(reqCtx, widget);
			if (!this.isStaticAction()) {
				String requestActionPath = request.getParameter(REQUEST_PARAM_ACTIONPATH);
				String currentFrameActionPath = request.getParameter(REQUEST_PARAM_FRAMEDEST);
				Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
				if (requestActionPath != null && currentFrameActionPath != null && currentFrame.toString().equals(currentFrameActionPath)) {
					if (this.isAllowedRequestPath(requestActionPath)) {
						actionPath = requestActionPath;
					}
				}
			}
			reqCtx.addExtraParam(EXTRAPAR_STATIC_ACTION, this.isStaticAction());
			StringBuilder params = new StringBuilder();
			// 1) widget 2) tag 3)request
			String contentId = null;
			if (null != this.getContentId()) {
				contentId = this.getContentId();
			}
			if (null != contentId) {
				reqCtx.getRequest().setAttribute("currentContentId", contentId);
			}
			if (null != this.getReverseVotes() && this.getReverseVotes().equalsIgnoreCase("true")) {
				if (params.length() > 0) {
					params.append("&");
				}
				params.append("reverseVotes=true");
			}
			if (StringUtils.isNotBlank(this.getExtraParamsRedirect())) {
				String[] redirectParams = this.getExtraParamsRedirect().split(",");
				reqCtx.getRequest().setAttribute("extraParamNames", redirectParams);
			}
			actionPath = actionPath + "?" + params.toString();
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(actionPath);
			requestDispatcher.include(request, responseWrapper);
		} catch (Throwable t) {
			_logger.error("Error including widget ", t);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/aps/jsp/system/internalServlet_error.jsp");
			requestDispatcher.include(request, responseWrapper);
		}
	}
	
	@Override
	public String getActionPath() {
		return "/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/intro.action";
	}
	
	@Override
	public void release() {
		super.release();
		this.setContentId(null);
		this.setReverseVotes(null);
		this.setExtraParamsRedirect(null);
	}

	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	public String getReverseVotes() {
		return _reverseVotes;
	}
	public void setReverseVotes(String reverseVotes) {
		this._reverseVotes = reverseVotes;
	}

	public String getExtraParamsRedirect() {
		return _extraParamsRedirect;
	}
	public void setExtraParamsRedirect(String extraParamsRedirect) {
		this._extraParamsRedirect = extraParamsRedirect;
	}

	private String _contentId;
	private String _reverseVotes;
	private String _extraParamsRedirect;
}
