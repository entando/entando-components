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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class WorkflowSearchFilter {
	
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	public List<String> getAllowedSteps() {
		return _allowedSteps;
	}
	public void setAllowedSteps(List<String> allowedSteps) {
		this._allowedSteps = allowedSteps;
	}
	public void addAllowedStep(String allowedStep) {
		this._allowedSteps.add(allowedStep);
	}
	
	private String _typeCode;
	private List<String> _allowedSteps = new ArrayList<String>();
	
}