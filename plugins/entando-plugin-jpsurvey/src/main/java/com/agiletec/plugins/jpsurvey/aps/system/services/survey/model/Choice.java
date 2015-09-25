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