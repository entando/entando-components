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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.helper.IContentWorkFlowActionHelper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author E.Santoboni
 */
@Aspect
public class IntroNewContentActionAspect {
	
	@After("execution(* com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction.createNewVoid())")
	public void checkCreateNewContent(JoinPoint joinPoint) {
		try {
			IntroNewContentAction action = (IntroNewContentAction) joinPoint.getTarget();
			List<SmallContentType> allowedContentTypes = this.getAllowedContentTypes();
			boolean check = false;
			for (int i = 0; i < allowedContentTypes.size(); i++) {
				SmallContentType contentType = allowedContentTypes.get(i);
				if (contentType.getCode().equals(action.getContentTypeCode())) {
					check = true;
					break;
				}
			}
			if (!check) {
				SmallContentType contentType = (SmallContentType) this.getContentManager().getSmallContentTypesMap().get(action.getContentTypeCode());
				String typeDescr = contentType != null ? contentType.getCode() : action.getContentTypeCode();
				action.addFieldError("contentTypeCode", action.getText("error.content.contentType.userNotAllowed", typeDescr));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllowedContentTypes", "Error checking content type authorization");
        	throw new RuntimeException("Error extracting allowed content types", t);
		}
	}
	/*
	//@Override
	public String getContentStatus() {
		return Content.STATUS_DRAFT;
	}
	*/
	@AfterReturning(pointcut = "execution(* com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction.getContentTypes())", returning = "contentTypes")
	public void getAllowedContentTypes(Object contentTypes) {
		List<SmallContentType> allowed = this.getAllowedContentTypes();
		System.out.println("getAllowedContentTypes " + this);
		((List<SmallContentType>)contentTypes).clear();
		((List<SmallContentType>)contentTypes).addAll(allowed);
	}
	
	protected List<SmallContentType> getAllowedContentTypes() {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		return this.getContentActionHelper().getAllowedContentTypes(currentUser);
	}
	
	//@Override
	@AfterReturning(pointcut = "execution(* com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction.getAvalaibleStatus())", returning = "items")
	public void getAvalaibleStatus(Object items) {
		System.out.println("getAvalaibleStatus " + this);
		//List<SelectItem> items = new ArrayList<SelectItem>(1);
		SelectItem item = new SelectItem(Content.STATUS_DRAFT, "name.contentStatus." + Content.STATUS_DRAFT);
		((List<SelectItem>)items).clear();
		((List<SelectItem>)items).add(item);
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IContentWorkFlowActionHelper getContentActionHelper() {
		return _contentActionHelper;
	}
	public void setContentActionHelper(IContentWorkFlowActionHelper contentActionHelper) {
		this._contentActionHelper = contentActionHelper;
	}
	
	private IContentManager _contentManager;
	private IContentWorkFlowActionHelper _contentActionHelper;
	
}