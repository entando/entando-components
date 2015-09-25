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
package org.entando.entando.plugins.jpwebform.aps.system.services.message.model;

import java.util.ArrayList;
import java.util.List;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;

/**
 * @author E.Santoboni
 */
public class StepsConfig {
	
	@Override
	public StepsConfig clone() {
		StepsConfig clone = new StepsConfig();
		clone.setFormTypeCode(this.getFormTypeCode());
		if (null != this.getSteps()) {
			List<Step> steps = new ArrayList<Step>();
			for (int i = 0; i < this.getSteps().size(); i++) {
				Step step = this.getSteps().get(i);
				steps.add(step.clone());
			}
			clone.setSteps(steps);
		}
		clone.setConfirmGui(this.isConfirmGui());
		clone.setBuiltConfirmGui(this.isBuiltConfirmGui());
		clone.setBuiltEndPointGui(this.isBuiltEndPointGui());
		return clone;
	}
	
	public String getFormTypeCode() {
		return _formTypeCode;
	}
	public void setFormTypeCode(String formTypeCode) {
		this._formTypeCode = formTypeCode;
	}
	
	public boolean isBuiltConfirmGui() {
		return _builtConfirmGui;
	}
	public void setBuiltConfirmGui(boolean builtConfirmGui) {
		this._builtConfirmGui = builtConfirmGui;
	}
	
	public boolean isBuiltEndPointGui() {
		return _builtEndPointGui;
	}
	public void setBuiltEndPointGui(boolean builtEndPointGui) {
		this._builtEndPointGui = builtEndPointGui;
	}
	
	/**
	 * Indicates if the confirm interface is active.
	 */ 
	public boolean isConfirmGui() {
		return _confirmGui;
	}
	public void setConfirmGui(boolean confirmGui) {
		this._confirmGui = confirmGui;
	}
	
	public Step getFirstStep() {
		if (null != this.getSteps() && !this.getSteps().isEmpty()) {
			return this.getSteps().get(0);
		}
		return null;
	}
	
	public Step getLastStep() {
		if (null != this.getSteps() && !this.getSteps().isEmpty()) {
			return this.getSteps().get(this.getSteps().size() - 1);
		}
		return null;
	}
	
	public Step getStep(String code) {
		List<Step> steps = this.getSteps();
		if (null != steps && !steps.isEmpty()) {
			for (int i = 0; i < steps.size(); i++) {
				Step step = steps.get(i);
				if (step.getCode().equals(code)) {
					return step;
				}
			}
		}
		return null;
	}
	
	public Step getNextStep(String code) {
		Step completed = new Step(JpwebformSystemConstants.COMPLETED_STEP_CODE, true);
		Step confirm = new Step(JpwebformSystemConstants.CONFIRM_STEP_CODE, true);
		List<Step> steps = this.getSteps();
		if(JpwebformSystemConstants.CONFIRM_STEP_CODE.equals(code)){
			return completed;
		} else if(this.getLastStep().getCode().equals(code)){
			if(isConfirmGui()){
				return confirm;
			} else {
				return completed;
			}
		}
		if (null != steps && !steps.isEmpty()) {
			for (int i = 0; i < steps.size(); i++) {
				Step step = steps.get(i);
				if (step.getCode().equals(code)) {
					if (i < (steps.size() - 1)) {
						return steps.get(i+1);
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}
	
	public Step getPreviousStep(String code) {
		if(JpwebformSystemConstants.COMPLETED_STEP_CODE.equals(code) && isConfirmGui()){
			return new Step(JpwebformSystemConstants.CONFIRM_STEP_CODE, true);
		} else if((JpwebformSystemConstants.COMPLETED_STEP_CODE.equals(code) && !isConfirmGui()) || JpwebformSystemConstants.CONFIRM_STEP_CODE.equals(code)){
			return this.getLastStep();
		}
		List<Step> steps = this.getSteps();
		if (null != steps && !steps.isEmpty()) {
			for (int i = 0; i < steps.size(); i++) {
				Step step = steps.get(i);
				if (step.getCode().equals(code)) {
					if (i < (steps.size()) && i > 0) {
						return steps.get(i-1);
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}
	
	public String[] getDisabilingCodes(String attributeName) {
		String[] codes = new String[0];
		List<Step> steps = this.getSteps();
		if (null != steps && !steps.isEmpty()) {
			for (int i = 0; i < steps.size(); i++) {
				Step step = steps.get(i);
				List<Step.AttributeConfig> configs = new ArrayList<Step.AttributeConfig>();
				configs.addAll(step.getAttributeConfigs().values());
				boolean editOnStep = false;
				for (int j = 0; j < configs.size(); j++) {
					Step.AttributeConfig attributeConfig = configs.get(j);
					if (attributeConfig.getName().equals(attributeName) && !attributeConfig.isView()) {
						editOnStep = true;
					}
				}
				if (!editOnStep) {
					String disablingCodes = JpwebformSystemConstants.DISABLING_CODE_ONSTEP_PREFIX + step.getCode();
					codes = this.addCode(codes, disablingCodes);
				}
			}
		}
		return codes;
	}
	
	private String[] addCode(String[] codes, String codeToAdd) {
		int len = codes.length;
		String[] newCodes = new String[len + 1];
		for(int i = 0; i < len; i++){
			newCodes[i] = codes[i];
		}
		newCodes[len] = codeToAdd;
		return newCodes;
	}
	
	public void addStep(String code){
		Step newStep = new Step();
		newStep.setCode(code);
		this.getSteps().add(newStep);
	}
	
	
	public List<Step> getSteps() {
		return _steps;
	}
	
	public void setSteps(List<Step> steps) {
		this._steps = steps;
	}
	
	public boolean checkGuiSteps(){
		boolean isBuilt = false;
		List<Step> steps = this.getSteps();
		if(null != steps && !steps.isEmpty()){
			isBuilt = true;
			for (int i = 0; i < steps.size(); i++) {
				Step step = steps.get(i);
				if(!step.isBuiltGui()){
					return false;
				}
			}
			if(!isBuiltConfirmGui() && isConfirmGui()){
				isBuilt = false;
			}
			if(!isBuiltEndPointGui()){
				isBuilt = false;
			}
		}
		return isBuilt;
	}
	
	private String _formTypeCode;
	private boolean _confirmGui;
	private boolean _builtConfirmGui;
	private boolean _builtEndPointGui;
	
	private List<Step> _steps = new ArrayList<Step>();

}
