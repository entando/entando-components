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

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsProperties;

/**
 * This class contains all the information of a question as found from the database plus some other 
 * information -called extra info- which add a property with data stored in another table.
 * object. 
 * @author M.E. Minnai
 */
public class Question extends QuestionRecord {
	
	/**
	 * This method is ONLY invoked by the DAO to set the extra properties for the current questions 
	 * @param questionnaire
	 * @param titles
	 */
	public void setExtraInfo(Boolean questionnaire, ApsProperties titles) {
		if (null != questionnaire) this._questionnaire = questionnaire;
		if (null != titles) this._surveyTitles = titles;
	}
	
	public Choice getChoice(int choiceId) {
		if (null== this.getChoices()) return null; 
		for (int i=0; i < this.getChoices().size(); i++) {
			Choice choice = this.getChoices().get(i);
			if (choice.getId() == choiceId) {
				return choice;
			}
		}
		return null;
	}
	
	public List<Choice> getChoices() {
		return _choices;
	}
	public void setChoices(List<Choice> choices) {
		this._choices = choices;
	}
	
	/**
	 * Set the number of the choices for the current question
	 * @param answersNumber
	 * @deprecated This is set automagically when loading the question, and a getChoices().size is sufficient
	 */
	public void setAnswersNumber(int answersNumber) {
		this._answersNumber = answersNumber;
	}
	
	public int getAnswersNumber() {
		if (null != this.getChoices()) {
			return this.getChoices().size();
		}
		ApsSystemUtils.getLogger().info("jpsurvey ***** YOU SHOULD NOT SEE THIS! **** (answers "+_answersNumber+")");
		return _answersNumber;
	}
	
	/**
	 * Extra params: Return the survey type this question belongs to.
	 * @return true if the choice belongs to a survey, false otherwise.
	 */
	public boolean isQuestionnaire() {
		return _questionnaire;
	}
	
	/**
	 * Extra params: return the titles of the survey this question belongs to.
	 * @return the localized map containing the localized titles of the survey.
	 */
	public ApsProperties getSurveyTitles() {
		return _surveyTitles;
	}
	
	private List<Choice> _choices = new ArrayList<Choice>();
	/**
	 * @deprecated DO NOT USE
	 */
	private int _answersNumber;
	private boolean _questionnaire;
	private ApsProperties _surveyTitles = null;
}
