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
package org.entando.entando.plugins.jpwebform.aps.system.services.message.model;

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