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
package com.agiletec.plugins.jpsurvey.aps.internalservlet.model;

import com.agiletec.plugins.jpsurvey.aps.system.services.collect.VoterResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

/**
 * @author E.Santoboni
 */
public class CurrentVotingInfoBean {
	
	public CurrentVotingInfoBean(Survey survey, Voter voter) {
		VoterResponse voterResponse = new VoterResponse(survey, voter);
		this.setVoterResponse(voterResponse);
		this.setCurrentQuestionIndex(0);
	}
	
	public int getCurrentQuestionIndex() {
		return _currentQuestionIndex;
	}
	protected void setCurrentQuestionIndex(int currentQuestionIndex) {
		this._currentQuestionIndex = currentQuestionIndex;
	}
	public void setNextIndex() {
		this._currentQuestionIndex++;
	}
	
	public VoterResponse getVoterResponse() {
		return _voterResponse;
	}
	protected void setVoterResponse(VoterResponse voterResponse) {
		this._voterResponse = voterResponse;
	}
	
	private VoterResponse _voterResponse;
	
	private int _currentQuestionIndex;
	
}