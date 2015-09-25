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