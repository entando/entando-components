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