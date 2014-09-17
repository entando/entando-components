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
package com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "area")
@XmlType(propOrder = {"coords", "shape", "link"})
@XmlSeeAlso({JAXBLinkValue.class})
public class JAXBAreaValue {
	
	@XmlElement(name = "coords", required = true)
	public String getCoords() {
		return coords;
	}
	public void setCoords(String coords) {
		this.coords = coords;
	}
	
	@XmlElement(name = "shape", required = true)
	public String getShape() {
		return _shape;
	}
	public void setShape(String shape) {
		this._shape = shape;
	}
	
	@XmlElement(name = "link", required = true)
	public JAXBLinkValue getLink() {
		return _link;
	}
	public void setLink(JAXBLinkValue link) {
		this._link = link;
	}
	
	private String coords;
	private String _shape;
	private JAXBLinkValue _link;
	
}
