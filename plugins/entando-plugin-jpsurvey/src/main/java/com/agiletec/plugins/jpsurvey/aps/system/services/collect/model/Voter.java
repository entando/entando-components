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