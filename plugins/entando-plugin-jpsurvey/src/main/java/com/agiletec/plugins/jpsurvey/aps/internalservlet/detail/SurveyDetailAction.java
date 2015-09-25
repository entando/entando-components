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
