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
package com.agiletec.plugins.jpimagemap.aps.system.services.content.parse.attribute;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.parse.attribute.AbstractAttributeHandler;
import com.agiletec.aps.system.common.entity.parse.attribute.AttributeHandlerInterface;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.ImageMapAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.util.LinkedArea;

/**
 * 
 * 
<imagemap name="ImageMap_1" attributetype="ImageMap" timestamp="92376410723" >
 <attribute name="ImageMap_1" attributetype="Image"
  <timestamp>1183115083983</timestamp>
  <resource resourcetype="Image" id="____"/>
  <text lang="__">_________</text>
  <text lang="__">_________</text>
 </attribute>
 <areas>
  <area shape="rect" coords="0,0,0,0">
   <attribute name="_______" attributetype="Link">
    <link type="_______">
     <urldest>______</urldest>
     <pagedest>______</pagedest>
     <contentdest>______</contentdest>
    </link>
    <text lang="__">_______</text>
    <text lang="__">_______</text>
   </attribute>
  </area>
  <area shape="rect" coords="10,10,200,200">
   <attribute name="ImageMap_1" attributetype="Link">
    <link type="_______">
     <urldest>______</urldest>
     <pagedest>______</pagedest>
     <contentdest>______</contentdest>
    </link>
    <text lang="__">_______</text>
    <text lang="__">_______</text>
   </attribute>
  </area>
  ....
  ....
 </areas>
</imagemap>
 * 
 * 
 * @version 1.0
 * @author E.Santoboni
 */
public class ImageMapAttributeHandler extends AbstractAttributeHandler {
	
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("imagemap")) {
			startImageMap(attributes, qName);
		} else if (qName.equals("areas")) {
			startAreas(attributes, qName);
		} else if (qName.equals("timestamp")) {
			startTimeStamp(attributes, qName);
		} else if (qName.equals("area")) {
			startArea(attributes, qName);
		} else {
			this.startAttributeElement(attributes, qName);
		}
	}
	
	private void startTimeStamp(Attributes attributes, String name) {
		//do nothing
	}

	private void startAreas(Attributes attributes, String qName) throws SAXException {
		//nothing to do
	}
	
	private void startArea(Attributes attributes, String qName) throws SAXException {
		//<area shape="rect" coords="0,0,0,0">
		_area = ((ImageMapAttribute) this.getCurrentAttr()).addArea();
		_area.setShape(this.extractAttribute(attributes, "shape", qName, true));
		_area.setCoords(this.extractAttribute(attributes, "coords", qName, true));
		this._elementAttribute = _area.getLink();
	}
	
	private void startAttributeElement(Attributes attributes, String qName) throws SAXException {
		if (null == _elementAttribute) {
			_elementAttribute = ((ImageMapAttribute) this.getCurrentAttr()).getImage();
		} else {
			if (null == _elementAttributeHandler) {
				_elementAttributeHandler = _elementAttribute.getHandler();
				_elementAttributeHandler.setCurrentAttr(_elementAttribute);
			}
			
			_elementAttributeHandler.startAttribute(attributes, qName);
		}
	}
	
	private void startImageMap(Attributes attributes, String qName) throws SAXException {
		//nulla da fare
	}
	
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("imagemap")) {
			endImageMap();
		} else if (qName.equals("areas")) {
			endAreas();
		} else if (qName.equals("timestamp")){
			endTimeStamp(textBuffer);
		} else if (qName.equals("area")) {
			endArea();
		} else {
			if (null == _elementAttributeHandler || _elementAttributeHandler.isEndAttribute(qName)) {
				_elementAttribute = null;
				_elementAttributeHandler = null;
			} else {
				_elementAttributeHandler.endAttribute(qName, textBuffer);
			}
		}
	}
	
	private void endTimeStamp(StringBuffer textBuffer) {
		if (null != textBuffer && null != this.getCurrentAttr()) {
			((ImageMapAttribute) this.getCurrentAttr()).setTimeStamp(textBuffer.toString());
		}
	}

	private void endArea() {
		this._area = null;
		_elementAttribute = null;
		_elementAttributeHandler = null;
	}

	private void endAreas() {
		this._area = null;
		_elementAttribute = null;
		_elementAttributeHandler = null;
	}
	
	private void endImageMap() {
		_elementAttribute = null;
		_elementAttributeHandler = null;
	}
	
	public boolean isEndAttribute(String qName) {
		return qName.equals("imagemap");
	}
	
	private LinkedArea _area;
	private AttributeInterface _elementAttribute;
	private AttributeHandlerInterface _elementAttributeHandler;

}
