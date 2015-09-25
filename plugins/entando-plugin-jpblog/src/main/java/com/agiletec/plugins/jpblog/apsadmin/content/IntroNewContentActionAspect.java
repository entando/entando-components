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
package com.agiletec.plugins.jpblog.apsadmin.content;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TimestampAttribute;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction;
import com.agiletec.plugins.jpblog.aps.system.JpblogSystemConstants;

/**
 * @author E.Santoboni
 */
@Aspect
public class IntroNewContentActionAspect {

	private static final Logger _logger =  LoggerFactory.getLogger(IntroNewContentActionAspect.class);
	
	@After("execution(* com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction.createNew())")
	public void presetBlogAttributes(JoinPoint joinPoint) {
		try {
			IntroNewContentAction action = (IntroNewContentAction) joinPoint.getTarget();
			Content content = action.getContent();
			HttpServletRequest request = ServletActionContext.getRequest();
			UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			AttributeInterface authorAttribute = content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_AUTHOR);
			if (null != authorAttribute) {
				((MonoTextAttribute) authorAttribute).setText(currentUser.getUsername()); 
			}
			TimestampAttribute timeAttribute = (TimestampAttribute) content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE);
			if (null != timeAttribute) {
				Date now = new Date();
				((TimestampAttribute) timeAttribute).setDate(now);
			}
		} catch (Throwable t) {
			_logger.error("Error presetting Blog Attributes", t);
			throw new RuntimeException("Error presetting Blog Attributes", t);
		}
	}
	
}