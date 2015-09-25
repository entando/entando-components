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
package com.agiletec.plugins.jpsurvey.aps.internalservlet;

import java.util.Date;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.model.CurrentVotingInfoBean;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.system.AbstractSurveyAction;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

/**
 * @author E.Santoboni
 */
public class SurveyFrontEntryPointAction extends AbstractSurveyAction implements ISurveyFrontEntryPointAction {
	
	@Override
	public String startSurvey() {
		Survey survey = null;
		try {
			this.getRequest().getSession().removeAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_VOTING_INFO_SESSION_PARAM);
			this.getRequest().getSession().removeAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_QUESTION_INDEX_SESSION_PARAM);
			survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
			Voter voter = this.createVoter();
			
			CurrentVotingInfoBean currentVotingInfoBean = new CurrentVotingInfoBean(survey, voter);
			this.getRequest().getSession().setAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_VOTING_INFO_SESSION_PARAM, currentVotingInfoBean);
			this.getRequest().getSession().setAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_QUESTION_INDEX_SESSION_PARAM, new Integer(-1));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "startSurvey");
			return FAILURE;
		}
		if (survey.isGatherUserInfo()) {
			return "profileVoterUser";
		}
		return SUCCESS;
	}
	
	protected Voter createVoter() {
		Voter voter = new Voter();
		voter.setDate(new Date());
		voter.setSurveyid(this.getSurveyId());
		voter.setUsername(this.getCurrentUser().getUsername());
		voter.setIpaddress(this.getRequest().getRemoteAddr());
		return voter;
	}
	
	public boolean isVotable(Survey survey) {
		boolean votable = (null != survey && survey.isActive() 
				&& (survey.getEndDate() == null || survey.getEndDate().after(new Date())));
		return votable;
	}
	
}