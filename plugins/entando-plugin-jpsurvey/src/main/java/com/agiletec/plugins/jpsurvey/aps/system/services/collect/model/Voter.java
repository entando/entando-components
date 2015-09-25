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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect.model;

import java.util.Date;

public class Voter {
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public short getAge() {
		return _age;
	}
	public void setAge(short age) {
		this._age = age;
	}
	
	public String getCountry() {
		return _country;
	}
	public void setCountry(String country) {
		this._country = country;
	}
	
	public Character getSex() {
		return _sex;
	}
	public void setSex(Character sex) {
		this._sex = sex;
	}
	
	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}
	
	public int getSurveyid() {
		return _surveyid;
	}
	public void setSurveyid(int surveyid) {
		this._surveyid = surveyid;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public String getIpaddress() {
		return _ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this._ipaddress = ipaddress;
	}
	
	private int _id = -1;
	private short _age;
	private String _country;
	private Character _sex;
	private Date _date;
	private int _surveyid;
	private String _username;
	private String _ipaddress;
}