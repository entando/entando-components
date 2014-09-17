/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model;

import java.util.Date;

import com.agiletec.aps.system.common.entity.model.ApsEntity;

/**
 * Implementation of an entity for a Messaging interaction.
 * The elements of the 'message' entity, as declared in the configuration item, is build invoking
 * the addAttribute() method, but this is allowed only during service initialization - in all the remaining
 * cases this class must be instantiated invoking the associated service, which will get it cloning the prototype
 * previously build.
 * @author M.Minnai, E.Mezzano
 */
public class Message extends ApsEntity {
	
	/**
	 * returns the username of the author of the message.
	 * @return The username of the author of the message.
	 */
	public String getUsername() {
		return _username;
	}
	/**
	 * Sets the username of the author of the message.
	 * @param username The username of the author of the message.
	 */
	public void setUsername(String username) {
		this._username = username;
	}
	
	/**
	 * Returns the date of creation of the message.
	 * @return The date of creation of the message.
	 */
	public Date getCreationDate() {
		return _creationDate;
	}
	/**
	 * Returns the date of creation of the message.
	 * @param creationDate The date of creation of the message.
	 */
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	
	/**
	 * Returns the lang code of the author of the message.
	 * @return The lang code of the author of the message.
	 */
	public String getLangCode() {
		return _langCode;
	}
	/**
	 * Sets the lang code of the author of the message.
	 * @param langCode The lang code of the author of the message.
	 */
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	private String _username;
	private Date _creationDate;
	private String _langCode;
	
}