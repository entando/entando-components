/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.util;

import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;

/**
 * @author E.Mezzano
 */
public class LinkedArea {
	
	public Object clone() {
		LinkedArea clone = new LinkedArea();
		clone.setCoords(this.getCoords());
		clone.setShape(this.getShape());
		clone.setLink((LinkAttribute) this.getLink().getAttributePrototype());
		return clone;
	}
	
	public Integer[] getArrayCoords() {
		if (null == this.getCoords()) return null;
		String[] array = this.getCoords().split(",");
		if (array == null || array.length != 4) {
			array = "0,0,10,10".split(",");
		}
		Integer[] coordsArray = new Integer[array.length];
		for (int i = 0; i < 4; i++) {
			coordsArray[i] = new Integer(array[i]);
		}
		return coordsArray;
	}
	
	public String getCoords() {
		return _coords;
	}
	public void setCoords(String coords) {
		this._coords = coords;
	}
	
	public LinkAttribute getLink() {
		return _link;
	}
	public void setLink(LinkAttribute link) {
		this._link = link;
	}
	
	public String getShape() {
		return _shape;
	}
	public void setShape(String shape) {
		this._shape = shape;
	}
	
	private LinkAttribute _link;
	private String _shape;
	private String _coords;
	
}