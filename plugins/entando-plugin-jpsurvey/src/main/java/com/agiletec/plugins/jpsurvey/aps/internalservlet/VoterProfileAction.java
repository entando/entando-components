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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpsurvey.aps.internalservlet.model.CurrentVotingInfoBean;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.VoterResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;

/**
 * @author E.Santoboni
 */
public class VoterProfileAction extends BaseAction implements IVoterProfileAction {
	
	@Override
	public String save() {
		try {
			VoterResponse userResponse =  this.getVoterResponse();
			Voter voter = userResponse.getVoter();
			voter.setAge(this.getAge().shortValue());
			voter.setCountry(this.getCountry());
			voter.setSex(this.getSex().toCharArray()[0]);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveVoterProfile");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public VoterResponse getVoterResponse() {
		return this.getCurrentVotingInfoBean().getVoterResponse();
	}
	
	public CurrentVotingInfoBean getCurrentVotingInfoBean() {
		return (CurrentVotingInfoBean) this.getRequest().getSession().getAttribute(ApsAdminSurveySystemConstants.SURVEY_CURRENT_VOTING_INFO_SESSION_PARAM);
	}
	
	public String getCountry() {
		return _country;
	}
	public void setCountry(String country) {
		this._country = country;
	}
	
	public Integer getAge() {
		return _age;
	}
	public void setAge(Integer age) {
		this._age = age;
	}
	
	public String getSex() {
		return _sex;
	}
	public void setSex(String sex) {
		this._sex = sex;
	}
	
	private String _country;
	private Integer _age;
	private String _sex;
	
}
