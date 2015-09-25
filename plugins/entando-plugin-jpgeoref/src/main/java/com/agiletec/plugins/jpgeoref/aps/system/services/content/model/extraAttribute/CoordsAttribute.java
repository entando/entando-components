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
package com.agiletec.plugins.jpgeoref.aps.system.services.content.model.extraAttribute;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractJAXBAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBTextAttribute;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jpgeoref.aps.system.GeoRefSystemConstants;

/**
 * Representation of an information type "geographical coordinates", mono language.
 * @author E.Santoboni
 */
public class CoordsAttribute extends AbstractAttribute {
	
	/**
	 * Returns JDOM element
	 * @return JDOM element
	 */
	@Override
	public Element getJDOMElement() {
		Element attributeElement = new Element(ATTRIBUTE_ELEMENT);
		attributeElement.setAttribute(ATTRIBUTE_ELEMENT_NAME, this.getName());
		attributeElement.setAttribute(ATTRIBUTE_ELEMENT_TYPE, this.getType());
		if (0 != this.getX() && 0 != this.getY()) {
			attributeElement.addContent(this.createCoordElem(GeoRefSystemConstants.COORDS_ATTRIBUTE_X, this.getX()));
			attributeElement.addContent(this.createCoordElem(GeoRefSystemConstants.COORDS_ATTRIBUTE_Y, this.getY()));
			attributeElement.addContent(this.createCoordElem(GeoRefSystemConstants.COORDS_ATTRIBUTE_Z, this.getZ()));
		}
		return attributeElement;
	}

	/**
	 * Create coordinate element
	 * @param name coordinate element
	 * @param coord coordinate value
	 * @return coordinate element
	 */
	private Element createCoordElem(String name, double coord) {
		Element coordElement = new Element(name);
		coordElement.setText(String.valueOf(coord));
		return coordElement;
	}
	
	@Override
	public boolean isSearchableOptionSupported() {
		return true;
	}
	
	/**
	 * Returns search information
	 * @param systemLangs system languages
	 * @return search information
	 */
	@Override
	public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
		if (0 != this.getX() && 0 != this.getY()) {
			List<AttributeSearchInfo> infos = new ArrayList<AttributeSearchInfo>();
			String stringInfo = "["+(String.valueOf(this.getX()) + "," 
					+ String.valueOf(this.getY()) + "," 
					+ String.valueOf(this.getZ()))+"]";
			AttributeSearchInfo info = new AttributeSearchInfo(stringInfo, null, null, null);
			infos.add(info);
			return infos;
		}
		return null;
	}
	
	/**
	 * Sets coordinate x
	 * @param x coordinate x
	 */
	public void setX(double x){
		_x = x;
	}
	/**
	 * Sets coordinate y
	 * @param y coordinate y
	 */
	public void setY(double y){
		_y = y;
	}
	/**
	 * Sets coordinate z
	 * @param z coordinate z
	 */
	public void setZ(double z){
		_z = z ;
	}
	/**
	 * Returns coordinate x
	 * @return coordinate x
	 */
	public double getX() {
		return _x;
	}
	/**
	 * Returns coordinate y
	 * @return coordinate y
	 */
	public double getY() {
		return _y;
	}
	
	/**
	 * Returns coordinate z
	 * @return coordinate z
	 */
	public double getZ() {
		return _z;
	}
	
	@Override
	public Object getValue() {
		if (!this.getStatus().equals(Status.VALUED)) {
			return null;
		}
		StringBuilder coords = new StringBuilder();
		coords.append("(");
		coords.append(this.getX());
		coords.append(",");
		coords.append(this.getY());
		if (this.getZ() != 0) {
			coords.append(",");
			coords.append(this.getZ());
		}
		coords.append(")");
		return coords.toString();
	}
	
	@Override
	public Status getStatus() {
		if (0 == this.getX() && 0 == this.getY() && 0==this.getZ()) {
			return Status.EMPTY;
		} else if (0 != this.getX() && 0 != this.getY()) {
			return Status.VALUED;
		}
		return Status.INCOMPLETE;
	}
	
	@Override
	protected AbstractJAXBAttribute getJAXBAttributeInstance() {
		return new JAXBTextAttribute();
	}
	
	@Override
	public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
		JAXBTextAttribute jaxbAttribute = (JAXBTextAttribute) super.createJAXBAttribute(langCode);
		if (null == jaxbAttribute) return null;
		Object value = this.getValue();
		if (null != value) {
			jaxbAttribute.setText(value.toString());
		}
		return jaxbAttribute;
	}
	
	@Override
	public void valueFrom(AbstractJAXBAttribute jaxbAttribute) {
		if (null == jaxbAttribute) {
			return;
		}
		String coords = ((JAXBTextAttribute) jaxbAttribute).getText();
		if (null == coords) {
			return;
		}
		String section = coords.trim().substring(1, coords.trim().length() - 1);
		String[] coordinates = section.split(",");
		if (coordinates.length < 2) {
			return;
		}
		try {
			this.setX(Double.parseDouble(coordinates[0]));
		} catch (Exception e) {}
		try {
			this.setY(Double.parseDouble(coordinates[1]));
		} catch (Exception e) {}
		if (coordinates.length > 2) {
			try {
				this.setZ(Double.parseDouble(coordinates[2]));
			} catch (Exception e) {}
		}
	}
	
	private static final String ATTRIBUTE_ELEMENT = "attribute";
	private static final String ATTRIBUTE_ELEMENT_NAME = "name";
	private static final String ATTRIBUTE_ELEMENT_TYPE = "attributetype";

	private double _x;
	private double _y;
	private double _z;

}
