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
package org.entando.entando.plugins.jpwebform.aps.system.services.form.parse;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;

/**
 * @author S.Loru
 */
public class TestStepConfigsDOM extends BaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	private void init() throws Exception {
		try {
			_formManager = (IFormManager) this.getService(JpwebformSystemConstants.FORM_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	public void testOgnlExpression() throws ApsSystemException {
		StepsConfig perStepsConfigBackup = this._formManager.getStepsConfig("PER");
		StepsConfig stepsConfigTest = perStepsConfigBackup.clone();
		stepsConfigTest.addStep("TEST");
		List<Step> steps = stepsConfigTest.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			if("TEST".equals(step.getCode())){
				step = createTestStep("TEST");
				steps.set(i, step);
			}
		}
		steps = stepsConfigTest.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			if("TEST".equals(step.getCode())){
				assertEquals("test", step.getOgnlExpression());
			}
		}
		this._formManager.saveStepsConfig(stepsConfigTest);
		StepsConfig stepsConfigTestFromDB = this._formManager.getStepsConfig("PER");
		assertEquals(stepsConfigTest, stepsConfigTestFromDB);
		
		this._formManager.saveStepsConfig(perStepsConfigBackup);
		StepsConfig perStepsConfig = this._formManager.getStepsConfig("PER");
		assertNull(perStepsConfig.getStep("TEST"));
		
		assertEquals(perStepsConfigBackup, perStepsConfig);
	}
	
	private Step createTestStep(String code){
		Step step = new Step(code, true);
		step.setOgnlExpression("test");
		return step;
	}

	private IFormManager _formManager = null;

}
