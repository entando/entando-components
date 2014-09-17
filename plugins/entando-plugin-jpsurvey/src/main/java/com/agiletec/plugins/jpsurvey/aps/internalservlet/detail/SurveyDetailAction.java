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
package com.agiletec.plugins.jpsurvey.aps.internalservlet.detail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.system.AbstractSurveyAction;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

/**
 * @author E.Santoboni
 */
public class SurveyDetailAction extends AbstractSurveyAction implements ISurveyDetailAction {
	
	public Survey getSurvey() {
		Survey survey = null;
		try {
			survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSurvey");
			throw new RuntimeException("Error loading survey: " + this.getSurveyId(), t);
		}
		return survey;
	}
	
	@Override
	public Integer getTotalVoters(Integer surveyId) {
		Integer voters = new Integer(0);
		try {
			List<Integer> votersId = this.getVoterManager().searchVotersByIds(null, null, null, null, null, surveyId, null);
			if (null == votersId) return voters;
			voters = new Integer(votersId.size());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTotalVoters");
			throw new RuntimeException("getTotalVoters surveyId " + surveyId, t);
		}
		return voters;
	}
	
	@Override
	public Map<Integer, Integer> getQuestionStatistics(Integer questionId) {
		Map<Integer, Integer> stats = null;
		try {
			stats = this.getResponseManager().loadQuestionStatistics(questionId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getQuestionStatistics");
			throw new RuntimeException("getQuestionStatistics id " + questionId, t);
		}
		return stats;
	}
	
	@Override
	public double getChoicePercentage(Map<Integer, Integer> questionStats, Integer choiceId) {
		int sum = 0;
		Iterator<Integer> iter = questionStats.values().iterator();
		while (iter.hasNext()) {
			Integer integer = (Integer) iter.next();
			sum += integer;
		}
		int occurrence = questionStats.get(choiceId);
		double percent = (((double) occurrence)/((double) sum))*100;
		return percent;
	}
	
	protected IVoterManager getVoterManager() {
		return _voterManager;
	}
	public void setVoterManager(IVoterManager voterManager) {
		this._voterManager = voterManager;
	}
	
	protected IResponseManager getResponseManager() {
		return _responseManager;
	}
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	
	private IVoterManager _voterManager;
	
	private IResponseManager _responseManager;
	
}
