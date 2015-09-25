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
package org.entando.entando.plugins.jpwebform.apsadmin.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step.AttributeConfig;
import org.entando.entando.plugins.jpwebform.apsadmin.AbstractConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class StepConfigAction extends AbstractConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(StepConfigAction.class);

	@Override
	public void validate() {
		super.validate();
		this.verifyOgnlExpressions();
	}

    public String entrySteps() {
        this.getRequest().getSession().removeAttribute(STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM);
        return super.entryConfigStep();
    }
	
    public void verifyOgnlExpressions(){
		List<Step> steps = this.getStepsConfig().getSteps();
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			verifyOgnlExpression(step.getCode());
		}
	}
			
	public void verifyOgnlExpression(String stepCode){
		Step step = null;
		try {
			step = this.getStepsConfig().getStep(stepCode);
			Message entityPrototype = (Message) this.getEntityPrototype(this.getEntityTypeCode());
			IFormManager.OGNL_MESSAGES verifyOgnlExpression = this.getFormManager().verifyOgnlExpression(step.getOgnlExpression(), entityPrototype, this.getCurrentUser().getUsername());
			String[] args = {stepCode};
			switch(verifyOgnlExpression){
				case GENERIC_ERROR:
					this.addFieldError(stepCode, this.getText("jpwebform.steps.error.generic", args));
					break;
				case METHOD_ERROR:
					this.addFieldError(stepCode, this.getText("jpwebform.steps.error.nosuchmethod", args));
					break;
				case PROPERTY_ERROR:
					this.addFieldError(stepCode, this.getText("jpwebform.steps.error.nosuchproperty", args));
					break;
			}
        } catch (Throwable t) {
        	_logger.error("error in verifyOgnlExpressionAction", t);
			this.addActionError("error on validation");
        } 
	}
	
    public String addStepAttribute() {
        Boolean readOnly = Boolean.valueOf(this.getRequest().getParameter(ONLY_VIEW_ATTRIBUTE + this.getStepCode()));
        String attributeName = this.getRequest().getParameter(ENTITY_ATTRIBUTE_NAME + this.getStepCode());
        Step step = this.getStepsConfig().getStep(this.getStepCode());
        if(step != null){
            List<String> attributeOrder = (ArrayList<String>) step.getAttributeOrder();
            boolean contains = attributeOrder.contains(this.getAttributeName());
            if(!contains){
                step.addAttributeConfig(attributeName, readOnly);
            } else if (contains){
                this.addFieldError("Add step attribute error", this.getText("error.attributeNameExists"));
            }
        }
        this.getRequest().getSession().setAttribute(STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM, new Boolean(true));
        return SUCCESS;
    }
    
    public String removeStepAttribute() {
        String attributeName = this.getAttributeName();
        Step step = this.getStepsConfig().getStep(this.getStepCode());
        if(step != null){
            List<String> attributeOrder = (ArrayList<String>) step.getAttributeOrder();
            boolean contains = attributeOrder.contains(this.getAttributeName());
            if(contains){
                step.removeAttributeConfig(attributeName);
            } else if (!contains){
                this.addFieldError("Remove step attribute error", this.getText("error.attributeNameExists.not"));
            }
        }
        this.getRequest().getSession().setAttribute(STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM, new Boolean(true));
        return SUCCESS;
    }
    
    public String moveStepAttribute() {
        Step step = this.getStepsConfig().getStep(this.getStepCode());
        int indexStep = this.getAttributeOrder();
        int indexAttribute = this.getAttributeOrder();
        List<String> attributeOrder = step.getAttributeOrder();
        if(step != null && !attributeOrder.isEmpty() && step.getAttributeOrder().contains(this.getAttributeName())){
            moveElement(indexAttribute, attributeOrder);
        }
        this.getRequest().getSession().setAttribute(STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM, new Boolean(true));
        return SUCCESS;
        
    }
    
    public String addStep() throws ApsSystemException {
		List<Step> steps = this.getStepsConfig().getSteps();
		boolean contains = false;
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			contains = this.getNewStepCode().trim().equals(step.getCode());
		}
        if(this.getStepsConfig() != null && !contains){
            this.getStepsConfig().addStep(this.getNewStepCode());
            return SUCCESS;
        } else if (contains){
            this.addFieldError("New step code", this.getText("error.newStepCode"));
        }
        this.getRequest().getSession().setAttribute(STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM, new Boolean(true));
        return SUCCESS;
    }
    
    public String removeStep() {
        List<Step> steps = this.getStepsConfig().getSteps();
        Step currentStep = steps.get(this.getOrder());
        if(currentStep != null && currentStep.getCode().equals(this.getStepCode())){
            steps.remove(this.getOrder());
        }
        this.getRequest().getSession().setAttribute(STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM, new Boolean(true));
        return SUCCESS;
    }
    
    public String moveStep() {
        List<Step> steps = this.getStepsConfig().getSteps();
        int index = this.getOrder();
        Step currentStep = this.getStepsConfig().getSteps().get(index);
        if(currentStep != null && currentStep.getCode().equals(this.getStepCode())) {
            moveElement(index, steps);
        }
        this.getRequest().getSession().setAttribute(STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM, new Boolean(true));
        return SUCCESS;
    }
    
    public String save() {
        try {
			if(isConfirmGui()){
				this.getStepsConfig().setConfirmGui(isConfirmGui());
			} else {
				this.getStepsConfig().setConfirmGui(Boolean.FALSE);
			}
            this.getFormManager().saveStepsConfig(this.getStepsConfig());
			this.getRequest().getSession().setAttribute(JpwebformSystemConstants.STEP_CONFIG_SESSION_PARAM, this.getStepsConfig().clone());
        } catch (Throwable t) {
        	_logger.error("error in save", t);
            return FAILURE;
        } finally {
            this.getRequest().getSession().removeAttribute(JpwebformSystemConstants.STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM);
            this.getRequest().getSession().removeAttribute(JpwebformSystemConstants.STEP_CONFIG_SESSION_PARAM);
        }
        return SUCCESS;
    }
    
    public boolean isStepsConfigModified() {
        Object marker = this.getRequest().getSession().getAttribute(STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM);
        return (null != marker && marker instanceof Boolean && ((Boolean)marker).booleanValue());
    }
    
    private void moveElement(int index, List<?> list) {
        int newIndex = 0;
        switch(this.getMovement()){
            case UP:
                newIndex = index - 1;
                if(newIndex >= 0 ){
                    Collections.swap(list, index, newIndex);
                }
                break;
            case DOWN:
                newIndex = index + 1;
                if(newIndex < list.size()){
                    Collections.swap(list, index, newIndex);
                }
                break;
        }
    }
	
	private boolean checkOtherSteps(String attributeName){
		List<Step> steps = this.getStepsConfig().getSteps();
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			List<String> attributeOrder = step.getAttributeOrder();
			for (int j = 0; j < attributeOrder.size(); j++) {
				String currentAttributeName = attributeOrder.get(j);
				if(currentAttributeName.equals(attributeName)){
					AttributeConfig attributeConfig = step.getAttributeConfigs().get(attributeName);
					if(!attributeConfig.isView())
						return true;
				}
				
			}
		}
		return false;
	}
    
    public MOVEMENT getMovement() {
        return _movement;
    }
    public void setMovement(MOVEMENT movement) {
        this._movement = movement;
    }
    
    public String getAttributeName() {
        return _attributeName;
    }
    public void setAttributeName(String attributeName) {
        this._attributeName = attributeName;
    }
    
    public String getNewStepCode() {
        return _newStepCode;
    }
    
    public void setNewStepCode(String newStepCode) {
        this._newStepCode = newStepCode;
    }

    public int getAttributeOrder() {
        return _attributeOrder;
    }

    public void setAttributeOrder(int attributeOrder) {
        this._attributeOrder = attributeOrder;
    }

	public boolean isConfirmGui() {
		return _confirmGui;
	}

	public void setConfirmGui(boolean confirmGui) {
		this._confirmGui = confirmGui;
	}
	
	
    
    private String _newStepCode;
    private MOVEMENT _movement;
    private String _attributeName;
    private int _attributeOrder;
	
	private boolean _confirmGui;
    
    public enum MOVEMENT {UP, DOWN}
    
    public static final String STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM = "jpwebform_stepConfig_modifyMarker_sessionParam";
    public static final String ONLY_VIEW_ATTRIBUTE = "onlyViewAttribute_";
    public static final String ENTITY_ATTRIBUTE_NAME = "entityAttributeName_";
	

}
