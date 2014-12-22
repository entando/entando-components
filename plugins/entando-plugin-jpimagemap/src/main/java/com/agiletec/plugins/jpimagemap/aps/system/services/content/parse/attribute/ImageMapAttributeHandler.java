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
