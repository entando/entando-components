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

import com.agiletec.apsadmin.system.entity.type.EntityTypesAction;
import java.util.List;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;

/**
 *
 * @author S.Loru
 */
public class FormEntityTypeAction extends EntityTypesAction {
	
	
	public StepsConfig getStepsConfigExtended(String entityTypeCode) {
		StepsConfig stepsConfig = this.getFormManager().getStepsConfig(entityTypeCode);
		if(null == stepsConfig){
			stepsConfig = new StepsConfig();
		} else {
			stepsConfig = stepsConfig.clone();
		}
		List<Step> steps = stepsConfig.getSteps();
		steps.add(new Step(JpwebformSystemConstants.CONFIRM_STEP_CODE, stepsConfig.isBuiltConfirmGui()));
		steps.add(new Step(JpwebformSystemConstants.COMPLETED_STEP_CODE, stepsConfig.isBuiltEndPointGui()));
		return stepsConfig;
	}

	
	public boolean checkEntityGui(String entityTypeCode){
		return this.getFormManager().checkStepGui(entityTypeCode);
	}
	
	public IFormManager getFormManager() {
		return formManager;
	}
	
	public void setFormManager(IFormManager formManager) {
		this.formManager = formManager;
	}
	
	private IFormManager formManager;

}
