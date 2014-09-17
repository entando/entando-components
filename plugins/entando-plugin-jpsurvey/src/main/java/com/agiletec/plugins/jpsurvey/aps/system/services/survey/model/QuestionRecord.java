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

/**
 * This class describes the object 'question' as mapped in the database table. This is later extended so to
 * include logical informations (such as the choices list) needed by the relative manager
 * @author M.E. Minnai
 */
public class QuestionRecord {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public int getSurveyId() {
		return _surveyId;
	}
	public void setSurveyId(int surveyId) {
		this._surveyId = surveyId;
	}
	
	public ApsProperties getQuestions() {
		return _questions;
	}
	public void setQuestions(ApsProperties questions) {
		this._questions = questions;
	}
	
	public int getPos() {
		return _pos;
	}
	public void setPos(int pos) {
		this._pos = pos;
	}
	
	public void setSingleChoice(boolean singleChoice) {
		this._singleChoice = singleChoice;
	}
	public boolean isSingleChoice() {
		return _singleChoice;
	}
	
	public void setMinResponseNumber(int minResponseNumber) {
		this._minResponseNumber = minResponseNumber;
	}
	public int getMinResponseNumber() {
		return _minResponseNumber;
	}
	
	public void setMaxResponseNumber(int maxResponseNumber) {
		this._maxResponseNumber = maxResponseNumber;
	}
	public int getMaxResponseNumber() {
		return _maxResponseNumber;
	}
	
	private int _id;
	private int _surveyId;
	private ApsProperties _questions = new ApsProperties();
	private int _pos;
	private boolean _singleChoice;
	private int _minResponseNumber;
	private int _maxResponseNumber;
}
