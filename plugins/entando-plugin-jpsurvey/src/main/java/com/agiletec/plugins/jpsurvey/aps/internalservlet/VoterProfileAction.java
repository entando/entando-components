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
