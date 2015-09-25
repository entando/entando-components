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
