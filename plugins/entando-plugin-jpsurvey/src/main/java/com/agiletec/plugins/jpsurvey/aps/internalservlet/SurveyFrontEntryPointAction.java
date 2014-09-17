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