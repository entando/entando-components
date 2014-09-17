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
