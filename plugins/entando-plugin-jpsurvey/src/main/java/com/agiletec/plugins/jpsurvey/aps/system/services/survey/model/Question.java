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
