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
