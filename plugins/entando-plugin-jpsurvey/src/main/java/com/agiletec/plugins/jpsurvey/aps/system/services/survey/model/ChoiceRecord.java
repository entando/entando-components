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
package com.agiletec.plugins.jpsurvey.aps.system.services.survey.model;

import com.agiletec.aps.util.ApsProperties;

public class ChoiceRecord {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public int getQuestionId() {
		return _questionId;
	}
	public void setQuestionId(int questionId) {
		this._questionId = questionId;
	}
	
	public boolean isFreeText() {
		return _freeText;
	}
	public void setFreeText(boolean freeText) {
		this._freeText = freeText;
	}
	
	public void setChoices(ApsProperties choices) {
		this._choices = choices;
	}
	public ApsProperties getChoices() {
		return _choices;
	}
	
	public void setPos(int pos) {
		this._pos = pos;
	}
	public int getPos() {
		return _pos;
	}
	
	private int _id;
	private int _questionId;
	private ApsProperties _choices = new ApsProperties();
	private int _pos;
	private boolean _freeText;
}
