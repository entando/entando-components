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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class VoterResponse {
	
	public VoterResponse(Survey survey, Voter voter) {
		this.setSurvey(survey);
		this.setVoter(voter);
	}
	
	public Voter getVoter() {
		return _voter;
	}
	public void setVoter(Voter voter) {
		this._voter = voter;
	}
	
	public Survey getSurvey() {
		return _survey;
	}
	public void setSurvey(Survey survey) {
		this._survey = survey;
	}
	
	public List<SingleQuestionResponse> getResponses() {
		return _responses;
	}
	public void setResponses(List<SingleQuestionResponse> responses) {
		this._responses = responses;
	}
	
	public List<SingleQuestionResponse> gerResponsesByQuestion(int questionId) {
		List<SingleQuestionResponse> responses = new ArrayList<SingleQuestionResponse>();
		for (int i=0; i<this.getResponses().size(); i++) {
			SingleQuestionResponse response = this.getResponses().get(i);
			if (response.getQuestionId() == questionId) {
				responses.add(response);
			}
		}
		return null;
	}
	
	protected void setVoterOnResponses(int voterId) {
		for (int i=0; i<this.getResponses().size(); i++) {
			SingleQuestionResponse response = this.getResponses().get(i);
			response.setVoterId(voterId);
		}
	}
	
	private Voter _voter;
	private Survey _survey;
	private List<SingleQuestionResponse> _responses = new ArrayList<SingleQuestionResponse>();
	
}
