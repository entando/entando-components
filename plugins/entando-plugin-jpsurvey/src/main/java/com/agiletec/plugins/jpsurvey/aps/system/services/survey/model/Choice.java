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
package com.agiletec.plugins.jpsurvey.aps.system.services.survey.model;

import com.agiletec.aps.util.ApsProperties;

/**
 * This class describes a survey object, composed by the fields describing it in the tables plus some
 * additional data -called extra info- which add properties to the choice object, but which are stored 
 * in different tables. Please note the the extraInfo fields are NOT persistent and should be read only
 * values.
 * @author M.E. Minnai
 */
public class Choice extends ChoiceRecord {
	
	/**
	 * Set the extra parameters for the current choice object. These value are not persistent and therefore
	 * this method cannot be used to change survey properties. This method is only invoked by the proper DAO
	 * when loading the choice from the database so do not use it (unless you know WHAT you are doing).
	 * NOTE: a null value in any of the input parameters won't update the previous one.
	 * @param surveyId the ID of the survey this choices belongs to
	 * @param questionnaire The survey type, if true then the survey is a questionnaire
	 * @param titles The localized titles of the survey this choice belongs to
	 * @param questions the localized questions this choices belongs to
	 */
	public void setExtraInfo(Integer surveyId, Boolean questionnaire, ApsProperties titles, ApsProperties questions) {
		if (null != surveyId) this._surveyId = surveyId;
		if (null != questionnaire) this._questionnaire = questionnaire;
		if (null != titles) this._surveyTitles = titles;
		if (null != questions) this._questions = questions;
	}
	
	public void setAnswer(String langCode, String answer) {
		this.getChoices().setProperty(langCode, answer);
	}
	public String getAnswer(String langCode) {
		return this.getChoices().getProperty(langCode);
	}
	
	/**
	 * Extra info field: return the ID of the survey this choice belongs to
	 * @return the ID of the survey this choice belongs to
	 */
	public int getSurveyId() {
		return _surveyId;
	}
	
	/**
	 * Extra info field: return the survey type this choice belongs to
	 * @return true if the choice belongs to a survey, false otherwise.
	 */
	public boolean isQuestionnaire() {
		return _questionnaire;
	}
	
	/**
	 * Extra info field: get the title of the survey this choice belongs to
	 * @return the map with the localized titles of the survey this choices belongs to.
	 */
	public ApsProperties getSurveyTitles() {
		return _surveyTitles;
	}

	/**
	 * Extra info field: get the question titles this choice belongs to.
	 * @return the map with the localized title of the question this choice belongs to.
	 */
	public ApsProperties getQuestions() {
		return _questions;
	}

	private int _surveyId;
	private boolean _questionnaire;
	private ApsProperties _surveyTitles;
	private ApsProperties _questions;
}