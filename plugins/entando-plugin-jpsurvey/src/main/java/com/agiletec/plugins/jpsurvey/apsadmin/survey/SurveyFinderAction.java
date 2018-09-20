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

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class SurveyFinderAction extends AbstractSurveyAction implements ISurveyFinderAction {
	
	public List<Integer> getSurveysIds() {
		List<Integer> list = null;
		List<String> groupList = null;
		try {
			// FIXME is this check necessary?
			if (null != this.getGroup() && this.getGroup().trim().length() > 0) {
				groupList = new ArrayList<String>();
				groupList.add(this.getGroup());
			}
			list = this.getSurveyManager().searchSurvey(null, this.getDescription(),
					groupList, null, this.getQuestionnaire(), this.getTitle(), null);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSurveysIds");
			throw new RuntimeException("Errore in ricerca survey", t);
		}
		return list;
	}
	
	public Survey getSurvey(Integer id) {
		Survey survey = null;
		List<Question> questions = null;
		try {
			survey = this.getSurveyManager().loadSurvey(id);
			questions = this.getSurveyManager().getSurveyQuestions(id);
			if(questions != null){
				survey.setQuestionsNumber(questions.size());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadSurvey");
			throw new RuntimeException("Error loading survey with id " + id, t);
		}
		return survey;
	}
	
	/**
	 * Restituisce la lista ordinata dei gruppi presenti nel sistema.
	 * @return La lista dei gruppi presenti nel sistema.
	 */
	public List<Group> getGroups() {
		return this.getGroupManager().getGroups();
	}
	
	public void setDescription(String description) {
		this._description = description;
	}
	public String getDescription() {
		return _description;
	}
	
	public void setTitle(String title) {
		this._title = title;
	}
	public String getTitle() {
		return _title;
	}
	
	public void setGroup(String group) {
		this._group = group;
	}
	public String getGroup() {
		return _group;
	}

	public void setQuestionnaire(Boolean questionnaire) {
		this._questionnaire = questionnaire;
	}
	public Boolean getQuestionnaire() {
		return _questionnaire;
	}
	
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	
	// Form variables
	private Boolean _questionnaire;
	private String _description;
	private String _title;
	private String _group;
	
	// Managers
	private IGroupManager _groupManager;

}
