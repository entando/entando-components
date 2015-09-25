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

import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.apsadmin.system.entity.type.EntityTypeConfigAction;

/**
 *
 * @author S.Loru
 */
public class FormEntityTypeConfigAction extends EntityTypeConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(FormEntityTypeConfigAction.class);

	@Override
	public String saveEntityType() {
		Message entityType = (Message) this.getEntityType();
		entityType.setRepeatable(this.isRepeatable());
//		IFormManager formManager = (IFormManager) this.getEntityManager();
//		StepsConfig stepsConfig = formManager.getStepsConfig(this.getEntityTypeCode());
//		if(null != stepsConfig){
//			List<Step> steps = stepsConfig.getSteps();
//			for (int i = 0; i < steps.size(); i++) {
//				Step step = steps.get(i);
//				List<String> attributeOrder = step.getAttributeOrder();
//				for (int j = 0; j < attributeOrder.size(); j++) {
//					String attributeName = attributeOrder.get(j);
//					List<AttributeInterface> attributeList = entityType.getAttributeList();
//					if(null != attributeList && !attributeList.isEmpty()){
//						if(!contains(attributeList, attributeName)){
//							step.removeAttributeConfig(attributeName);
//						}
//					}
//				}
//			}
//		}
//		try {
//			formManager.saveStepsConfig(stepsConfig);
//		} catch (Throwable t) {
//			 ApsSystemUtils.logThrowable(t, this, "save");
//		}
		return super.saveEntityType();
	}

	@Override
	protected IApsEntity updateEntityOnSession() {
		Message entityType = (Message) this.getEntityType();
		entityType.setRepeatable(this.isRepeatable());
		entityType.setIgnoreVersionEdit(this.isIgnoreVersionEdit());
		entityType.setIgnoreVersionDisplay(this.isIgnoreVersionDisplay());
		return super.updateEntityOnSession();
	}
	
	public boolean isRepeatable() {
		return repeatable;
	}

	public void setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
	}
	
	public boolean isFormRepeatable(){
		return ((Message) this.getEntityType()).isRepeatable();
	}
	
	public boolean isIgnoreVersionEdit() {
		return ignoreVersionEdit;
	}

	public void setIgnoreVersionEdit(boolean ignoreVersionEdit) {
		this.ignoreVersionEdit = ignoreVersionEdit;
	}

	public boolean isFormIgnoreEdit(){
		return ((Message) this.getEntityType()).isIgnoreVersionEdit();
	}
	
	public boolean isIgnoreVersionDisplay() {
		return ignoreVersionDisplay;
	}

	public void setIgnoreVersionDisplay(boolean ignoreVersionDisplay) {
		this.ignoreVersionDisplay = ignoreVersionDisplay;
	}
	
	public boolean isFormIgnoreDisplay(){
		return ((Message) this.getEntityType()).isIgnoreVersionDisplay();
	}
	private boolean repeatable;
	private boolean ignoreVersionEdit;
	private boolean ignoreVersionDisplay;

	private boolean contains(List<AttributeInterface> attributeList, String attributeName) {
		boolean contains = false;
		for (int k = 0; k < attributeList.size(); k++) {
			AttributeInterface attributeInterface = attributeList.get(k);
			if(attributeInterface.getName().equals(attributeName)){
				contains = true;
			}
		}
		return contains;
	}
	
	
	

}
