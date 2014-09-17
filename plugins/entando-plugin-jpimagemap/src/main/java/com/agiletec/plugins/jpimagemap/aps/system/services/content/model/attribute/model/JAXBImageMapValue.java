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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBResourceValue;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "imagemap")
@XmlType(propOrder = {"image", "areas"})
@XmlSeeAlso({ArrayList.class, JAXBAreaValue.class, JAXBResourceValue.class, JAXBLinkValue.class})
public class JAXBImageMapValue {
	
	@XmlElement(name = "image", required = true)
	public JAXBResourceValue getImage() {
		return _image;
	}
	public void setImage(JAXBResourceValue image) {
		this._image = image;
	}
	
	@XmlElement(name = "area", required = true)
	@XmlElementWrapper(name = "areas")
	public List<JAXBAreaValue> getAreas() {
		return _areas;
	}
	public void setAreas(List<JAXBAreaValue> areas) {
		this._areas = areas;
	}
	public void addArea(JAXBAreaValue area) {
		this.getAreas().add(area);
	}
	
	private JAXBResourceValue _image;
	private List<JAXBAreaValue> _areas = new ArrayList<JAXBAreaValue>();
	
}
