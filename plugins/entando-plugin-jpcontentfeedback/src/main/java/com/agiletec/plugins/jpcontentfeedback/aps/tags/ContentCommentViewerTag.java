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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;

/**
 * Estrae dal sistema un commento, dato il suo identificativo specificato come parametro
 * @author D.Cherchi
 *
 */
public class ContentCommentViewerTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ContentCommentViewerTag.class);
	
	 @Override
		public int doStartTag() throws JspException {
			try {
				ICommentManager commentManager = (ICommentManager) ApsWebApplicationUtils.getBean(JpcontentfeedbackSystemConstants.COMMENTS_MANAGER, this.pageContext);
				IComment comment = commentManager.getComment(this.getCommentId());
				this.pageContext.setAttribute(this.getCommentName(), comment);
			} catch (Throwable t) {
				_logger.error("error in doStartTag", t);
				throw new JspException("Error in doStartTag", t);
			}
			return EVAL_PAGE;
		}

	public void setCommentId(int commentId) {
		this._commentId = commentId;
	}

	public int getCommentId() {
		return _commentId;
	}

	public void setCommentName(String commentName) {
		this._commentName = commentName;
	}

	public String getCommentName() {
		return _commentName;
	}

	private int _commentId;
	private String _commentName;

}
