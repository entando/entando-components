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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content.helper;

import java.util.List;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public interface IContentWorkFlowActionHelper {
	
	public List<WorkflowSearchFilter> getWorkflowSearchFilters(UserDetails user);
	
	public List<SmallContentType> getAllowedContentTypes(UserDetails currentUser);
	
	public List<SelectItem> getAvalaibleStatus(UserDetails user, String contentTypeCode);
	
	public String getPreviousStep(String currentStep, String typeCode);
	
	public String getNextStep(String currentStep, String typeCode);
	
}