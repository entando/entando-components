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
