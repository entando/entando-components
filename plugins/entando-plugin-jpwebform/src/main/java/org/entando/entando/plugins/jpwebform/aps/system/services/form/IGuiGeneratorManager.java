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
package org.entando.entando.plugins.jpwebform.aps.system.services.form;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepGuiConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.TypeVersionGuiConfig;

/**
 * @author E.Santoboni
 */
public interface IGuiGeneratorManager {
	
	//public String generateUserGui(String typeCode, Integer stepIndex) throws ApsSystemException;
	
	//public void saveUserGui(String typeCode, Integer stepIndex, String gui) throws ApsSystemException;
	
	public void checkUserGuis(TypeVersionGuiConfig config) throws ApsSystemException;
	
	public void generatePreview(StepGuiConfig config) throws ApsSystemException;
	
	public void generatePreview(String formTypeCode) throws ApsSystemException;
	
}
