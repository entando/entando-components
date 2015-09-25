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
