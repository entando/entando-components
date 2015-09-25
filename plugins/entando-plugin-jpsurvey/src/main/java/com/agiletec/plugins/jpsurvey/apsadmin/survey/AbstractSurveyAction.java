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
package com.agiletec.plugins.jpsurvey.apsadmin.survey;

import java.util.List;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.apsadmin.resource.AbstractResourceAction;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.ISurveyManager;

/**
 * This class is the base for all the administration actions on surveys in all the back-end views.
 * We include the survey, question and choice managers and some other common methods.
 * @author M.E. Minnai
 */
public class AbstractSurveyAction extends AbstractResourceAction {

	/**
	 * Get, from the given properties indexed by the language code, the one in the most suitable
	 * language: if the localization in the current back-end language is not available then use the
	 * default language. 
	 * @param labels the label properties
	 * @return the localized label
	 */
	public String getLabel(ApsProperties labels) {
		String label = labels.getProperty(this.getCurrentLang().getCode());
		if (label == null || label.trim().length() == 0) {
			Lang defaultLang = this.getLangManager().getDefaultLang();
			label = labels.getProperty(defaultLang.getCode());
		}
		return label;
	}
	
	/**
	 * Return the list of the languages defined in the system
	 * @return the list of the languages defined in the system
	 */
	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}
	
	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	
	public void setSurveyManager(ISurveyManager surveyManager) {
		this._surveyManager = surveyManager;
	}
	protected ISurveyManager getSurveyManager() {
		return this._surveyManager;
	}
	
	private IResourceManager _resourceManager;
	private ISurveyManager _surveyManager;
	
}
