/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jprestapi.aps.core.helper.model;

/**
 *
 * @author Entando
 */
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//import org.entando.iot.connector.core.IIotUser;

/**
 *
 * @author entando
 *
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestUser implements IIotUser {


	private String id;
	private String accountId;
	private String username;
	private String createdOn;
	private String createdBy;
	private Date modifiedOn;
	private Date modifiedBy;
	private String status;
	private String password;
	private String displayName;
	private String email;
	private String phoneNumber;
	private String loginOn;
	private String loginAttempts;
	private String lockedOn;
	private List<TestUserRole> roles;
	private List<String>permissions;
	private String optlock;
	private String loginAttemptsResetOn;
	private String unlockOn;
	private String trust_date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Date getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Date modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLoginOn() {
		return loginOn;
	}

	public void setLoginOn(String loginOn) {
		this.loginOn = loginOn;
	}

	public String getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(String loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public String getLockedOn() {
		return lockedOn;
	}

	public void setLockedOn(String lockedOn) {
		this.lockedOn = lockedOn;
	}

	public List<TestUserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<TestUserRole> roles) {
		this.roles = roles;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public String getOptlock() {
		return optlock;
	}

	public void setOptlock(String optlock) {
		this.optlock = optlock;
	}

	public String getLoginAttemptsResetOn() {
		return loginAttemptsResetOn;
	}

	public void setLoginAttemptsResetOn(String loginAttemptsResetOn) {
		this.loginAttemptsResetOn = loginAttemptsResetOn;
	}

	public String getUnlockOn() {
		return unlockOn;
	}

	public void setUnlockOn(String unlockOn) {
		this.unlockOn = unlockOn;
	}

	public String getTrust_date() {
		return trust_date;
	}

	public void setTrust_date(String trust_date) {
		this.trust_date = trust_date;
	}


@Override
  public String toString() {
    return String.format("id %s, username: %s",
            id,
            username);
  }

}