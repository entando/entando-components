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

import java.util.Date;

import com.agiletec.aps.util.ApsProperties;

/**
 * This class describes the object 'survey' as mapped in the database table. This is later extended so to
 * include logical informations (such as the questions list) needed by the relative manager
 * logic.
 * @author M.E. Minnai
 */
public class SurveyRecord {
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public ApsProperties getDescriptions() {
		return _descriptions;
	}
	public void setDescriptions(ApsProperties descriptions) {
		this._descriptions = descriptions;
	}
	
	public String getGroupName() {
		return _groupName;
	}
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}
	
	public Date getStartDate() {
		return _startDate;
	}
	public void setStartDate(Date startDate) {
		this._startDate = startDate;
	}
	
	public Date getEndDate() {
		return _endDate;
	}
	public void setEndDate(Date endDate) {
		this._endDate = endDate;
	}
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	public boolean isPublicPartialResult() {
		return _publicPartialResult;
	}
	public void setPublicPartialResult(boolean publicPartialResult) {
		this._publicPartialResult = publicPartialResult;
	}
	
	public boolean isPublicResult() {
		return _publicResult;
	}
	public void setPublicResult(boolean publicResult) {
		this._publicResult = publicResult;
	}
	
	public void setQuestionnaire(boolean questionnaire) {
		this._questionnaire = questionnaire;
	}
	public boolean isQuestionnaire() {
		return _questionnaire;
	}
	
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}
	public ApsProperties getTitles() {
		return _titles;
	}
	
	public void setRestricted(boolean restricted) {
		this._restricted = restricted;
	}
	public boolean isRestricted() {
		return _restricted;
	}
	
	public boolean isCheckCookie() {
		return _checkCookie;
	}
	public void setCheckCookie(boolean checkCookie) {
		this._checkCookie = checkCookie;
	}
	
	public boolean isCheckIpAddress() {
		return _checkIpAddress;
	}
	public void setCheckIpAddress(boolean checkIpAddress) {
		this._checkIpAddress = checkIpAddress;
	}
	
	public void setImageId(String imageId) {
		this._imageId = imageId;
	}
	public String getImageId() {
		return _imageId;
	}
	
	public boolean isOpen() {
		if (!this.isActive()) return false;
		Date today = new Date();
		if (today.getTime() < this.getStartDate().getTime()) {
			return false;
		}
		if (null != this.getEndDate() && today.getTime() > this.getEndDate().getTime()) {
			return false;
		}
		return true;
	}
	
	// FIXME che ci fa qui?
	public boolean isArchive() {
		if (!this.isActive()) return false;
		Date today = new Date();
		if (today.getTime() < this.getStartDate().getTime()) {
			return false;
		}
		if (null == this.getEndDate()) {
			return true;
		}
		if (today.getTime() > this.getEndDate().getTime()) {
			return true;
		}
		return false;
	}

	public void setImageDescriptions(ApsProperties imageDescriptions) {
		this._imageDescriptions = imageDescriptions;
	}
	public ApsProperties getImageDescriptions() {
		return _imageDescriptions;
	}

	public void setGatherUserInfo(boolean gatherUserInfo) {
		this._gatherUserInfo = gatherUserInfo;
	}
	public boolean isGatherUserInfo() {
		return _gatherUserInfo;
	}
	
	public boolean isCheckUsername() {
		return _checkUsername;
	}
	public void setCheckUsername(boolean checkUsername) {
		this._checkUsername = checkUsername;
	}


	private int _id;
	private ApsProperties _descriptions = new ApsProperties();
	private String _groupName;
	private Date _startDate;
	private Date _endDate;
	private boolean _active;
	private boolean _publicPartialResult;
	private boolean _publicResult;
	private boolean _questionnaire;
//	private boolean _profileUser;  
	private boolean _gatherUserInfo; //TODO REQUIRED USER FIELD???
	private ApsProperties _titles = new ApsProperties();
	private boolean _restricted;
	private String _imageId;
	private ApsProperties _imageDescriptions = new ApsProperties();
	public boolean _checkCookie;
	public boolean _checkIpAddress;
	private boolean _checkUsername;
	
}