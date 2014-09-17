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
package com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.model;

/**
 * @author A.Cocco
 */
public class VCardContactField {
	
	/**
	 * Returns the code
	 * @return the code
	 */
	public String getCode() {
		return _code;
	}
	/**
	 * Sets the code
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this._code = code;
	}
	/**
	 * Returns the description
	 * @return the description
	 */
	public String getDescription() {
		return _description;
	}
	/**
	 * Sets the description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this._description = description;
	}
	/**
	 * Returns the profile attribute
	 * @return the profileAttribute
	 */
	public String getProfileAttribute() {
		return _profileAttribute;
	}
	/**
	 * Sets the profile attribute
	 * @param profileAttribute the profileAttribute to set
	 */
	public void setProfileAttribute(String profileAttribute) {
		this._profileAttribute = profileAttribute;
	}
	/**
	 * Returns true if it is enabled
	 * @return the isEnabled
	 */
	public Boolean isEnabled() {
		return _isEnabled;
	}
	/**
	 * Sets if it is enabled
	 * @param isEnabled the isEnabled to set
	 */
	public void setEnabled(Boolean isEnabled) {
		this._isEnabled = isEnabled;
	}
	private String _code;
	private String _description;
	private String _profileAttribute;
	private Boolean _isEnabled;

}
