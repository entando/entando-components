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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class Workflow {
	
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	public String getRole() {
		return _role;
	}
	public void setRole(String role) {
		this._role = role;
	}
	
	public Step getStep(String stepCode) {
		return this._stepsMap.get(stepCode);
	}
	public void addStep(Step step) {
		this._steps.add(step);
		this._stepsMap.put(step.getCode(), step);
	}
	public List<Step> getSteps() {
		return _steps;
	}
	public void setSteps(List<Step> steps) {
		this._steps = steps;
		this._stepsMap = new HashMap<String, Step>();
		Iterator<Step> stepsIter = steps.iterator();
		while (stepsIter.hasNext()) {
			Step step = stepsIter.next();
			this._stepsMap.put(step.getCode(), step);
		}
	}
	
	private String _typeCode;
	private String _role;
	private List<Step> _steps = new ArrayList<Step>();
	private Map<String, Step> _stepsMap = new HashMap<String, Step>();
	
}