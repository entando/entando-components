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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect.model;

public class SingleQuestionResponse {
	
	public int getVoterId() {
		return _voterId;
	}
	public void setVoterId(int voterId) {
		this._voterId = voterId;
	}
	
	public int getQuestionId() {
		return _questionId;
	}
	public void setQuestionId(int questionId) {
		this._questionId = questionId;
	}
	
	public int getChoiceId() {
		return _choiceId;
	}
	public void setChoiceId(int choiceId) {
		this._choiceId = choiceId;
	}

	public String getFreeText() {
		return _freeText;
	}
	public void setFreeText(String freeText) {
		this._freeText = freeText;
	}
	
	private int _voterId = -1;
	private int _questionId;
	private int _choiceId;
	private String _freeText;
}
