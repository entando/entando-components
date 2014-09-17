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

/**
 * Represents a Message Type in small form.
 * Contains the minimal elements needful to show the code and the description of the Message Type.
 * @author E.Santoboni, E.Mezzano
 */
public class SmallMessageType implements Comparable<SmallMessageType> {
	
	/**
	 * returns the code of the message type.
	 * @return The code of the message type.
	 */
	public String getCode() {
		return _code;
	}
	
	/**
	 * Sets the code of the message type.
	 * @param code The code of the message type.
	 */
	public void setCode(String code) {
		this._code = code;
	}
	
	/**
	 * Returns the description of the message type.
	 * @return The description of the message type.
	 */
	public String getDescr() {
		return _descr;
	}
	
	/**
	 * Sets the description of the message type.
	 * @param descr The description of the message type.
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	@Override
	public int compareTo(SmallMessageType smallMessageType) {
		return _descr.compareTo(smallMessageType.getDescr());
	}
	
	private String _code;
	private String _descr;
	
}