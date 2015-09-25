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

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import static org.entando.entando.plugins.jpwebform.aps.system.services.form.GuiGeneratorManager.TEMPLATE_FOLDER;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.springframework.web.context.ServletContextAware;

/**
 * @author E.Santoboni
 */
public class UserGuiGenerator implements ServletContextAware {

	public String generateUserGui(String stepCode, StepsConfig stepsConfig, IApsEntity prototype) throws ApsSystemException, Throwable {
		if (stepCode != null && stepCode.equals(JpwebformSystemConstants.COMPLETED_STEP_CODE)) {
			String userGui = this.generateEndPointUserGui();
			stepsConfig.setBuiltEndPointGui(true);
			return userGui;
		} else if (stepCode != null && stepCode.equals(JpwebformSystemConstants.CONFIRM_STEP_CODE)) {
			String userGui = this.generateConfirmUserGui();
			stepsConfig.setBuiltConfirmGui(true);
			return userGui;
		}
		if (null == stepsConfig) {
			throw new ApsSystemException("Null config");
		}
		Step step = stepsConfig.getStep(stepCode);
		if (null != step && null != prototype) {
			boolean isFirstStep = false;
			if (step.equals(stepsConfig.getFirstStep())) {
				isFirstStep = true;
			}
			String userGui = this.generateUserGui(step, prototype, isFirstStep);
			step.setBuiltGui(true);
			return userGui;
		}
		throw new ApsSystemException("Invalid Step Code + " + stepCode);
	}

	public String generateUserGui(Step step, IApsEntity prototype, boolean isFirstStep) throws ApsSystemException, Throwable {
		StringBuilder gui = new StringBuilder();
		String attributeType = "";
		gui.append(GuiGeneratorManager.getText(WFT_TITLE, _servletContext));
		gui.append(GuiGeneratorManager.getText(WFT_FORMSTART, _servletContext));
		List<String> attributeNames = (null != step) ? step.getAttributeOrder() : null;
		if (null != attributeNames && !attributeNames.isEmpty()) {
			for (int i = 0; i < attributeNames.size(); i++) {
				String attributeName = attributeNames.get(i);
				Step.AttributeConfig config = step.getAttributeConfigs().get(attributeName);
				AttributeInterface attribute = (AttributeInterface) prototype.getAttribute(attributeName);
				if (attribute != null) {
					attributeType = attribute.getType();
				}
				if ("Composite".equalsIgnoreCase(attributeType) || "Monolist".equalsIgnoreCase(attributeType)) {
					gui.append(GuiGeneratorManager.getText(WFT_COMPLEX, _servletContext));
				} else {
					gui.append(GuiGeneratorManager.getText(WFT_SIMPLE, _servletContext));
				}
				gui = new StringBuilder(gui.toString().replaceAll(WFT_NAME_MARKER, attributeName));
				if (config.isView()) {
					gui = new StringBuilder(gui.toString().replaceAll(WFT_INPUT, WFT_INPUT_EDITFALSE));
				}
			}
		}
		if (isFirstStep) {
			gui.append(GuiGeneratorManager.getText(WFT_FORMACTIONS, _servletContext));
		} else {
			gui.append(GuiGeneratorManager.getText(WFT_FORMACTIONSFULL, _servletContext));
		}
		gui.append(GuiGeneratorManager.getText(WFT_FORMEND, _servletContext));
		return gui.toString();
	}

	public String generateConfirmUserGui() throws ApsSystemException, Throwable {
		return this.generateUserGui(null, null, false);
	}

	public String generateEndPointUserGui() throws ApsSystemException, Throwable {
		return GuiGeneratorManager.getText(WFT_ENDMESSAGE, _servletContext);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this._servletContext = servletContext;
	}
	private static final String DEFAULT_GUI_FOLDER = TEMPLATE_FOLDER + "defaultgui/";
	public static final String WFT_SIMPLE = DEFAULT_GUI_FOLDER + "simple.wft";
	public static final String WFT_COMPLEX = DEFAULT_GUI_FOLDER + "complex.wft";
	/*
	 * This contains only submit button
	 */
	public static final String WFT_FORMACTIONS = DEFAULT_GUI_FOLDER + "formactions.wft";
	/*
	 * This contains back and submit button
	 */
	public static final String WFT_FORMACTIONSFULL = DEFAULT_GUI_FOLDER + "formactionsfull.wft";
	public static final String WFT_FORMEND = DEFAULT_GUI_FOLDER + "formend.wft";
	public static final String WFT_FORMSTART = DEFAULT_GUI_FOLDER + "formstart.wft";
	public static final String WFT_TITLE = DEFAULT_GUI_FOLDER + "title.wft";
	public static final String WFT_ENDMESSAGE = DEFAULT_GUI_FOLDER + "endmessage.wft";
	public static final String WFT_NAME_MARKER = "##name##";
	public static final String WFT_INPUT = "type=input#";
	public static final String WFT_INPUT_EDITFALSE = "type=input;edit=false#";
	
	private ServletContext _servletContext;
}
