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
import java.util.Iterator;
import java.util.List;

/**
 * This class describes a survey object, composed by the fields describing it in the tables plus some
 * additional data or methods needed for the sake of functionality.
 * @author M.E. Minnai
 */
public class Survey extends SurveyRecord {
	
	/**
	 * This method checks if the survey is eligible for publication and, if the case, it can be published for voting operations.
	 * @return true if the survey is eligible for voting, false otherwise.
	 * TODO correct the method name!
	 */
	public boolean isElegibleForVoting(Boolean ignoreDate) {
		if (null == ignoreDate) {
			ignoreDate = false;
		}
		if (!this.isPublishable()) return false;
		if (!this.isActive()) return false;
		return true;
	}
	
	/**
	 * Evaluate the publishability of the 'survey'. A poll is publishable if it has at least one question and if at least 
	 * two choices are present for every question. A questionnaire must have at least one question and a choice (or freetext).
	 * @return true if the survey is publishable, false otherwise.
	 */
	public boolean isPublishable() {
		if (null == this.getQuestions() || this.getQuestions().isEmpty()) return false;
		// each question must have at least two choices to choose from
		Iterator<Question> questionItr = this.getQuestions().iterator();
		while (questionItr.hasNext()) {
			Question currentQuestion = questionItr.next();
			if (null == currentQuestion.getChoices() || 
					currentQuestion.getChoices().isEmpty() || 
					(currentQuestion.getChoices().size() < 2 && !this.isQuestionnaire()) ||
					(currentQuestion.getChoices().size() == 1 && !currentQuestion.getChoices().get(0).isFreeText())) {
				return false;
			}
		}
		return true;
	}
	
	public Question getQuestion(int questionId) {
		if (null== this.getQuestions()) return null; 
		for (int i=0; i<this.getQuestions().size(); i++) {
			Question question = this.getQuestions().get(i);
			if (question.getId() == questionId) {
				return question;
			}
		}
		return null;
	}
	
	public void setQuestionsNumber(int questionsNumber) {
		this._questionsNumber = questionsNumber;
	}
	public int getQuestionsNumber() {
		return _questionsNumber;
	}
	
	public List<Question> getQuestions() {
		return _questions;
	}
	public void setQuestions(List<Question> questions) {
		this._questions = questions;
	}
	
	private List<Question> _questions = new ArrayList<Question>();
	
	private int _questionsNumber;
	
}

