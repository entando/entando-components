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