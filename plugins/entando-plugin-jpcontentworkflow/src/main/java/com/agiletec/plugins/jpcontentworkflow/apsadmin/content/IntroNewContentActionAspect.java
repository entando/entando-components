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