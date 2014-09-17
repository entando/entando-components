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
