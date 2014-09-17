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
package com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DefaultJAXBAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ImageAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBResourceValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ResourceAttributeInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model.JAXBAreaValue;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model.JAXBImageMapValue;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.util.LinkedArea;

/**
 * Rappresenta un informazione tipo ImageMap.
 * @author E.Santoboni - G.Cocco
 */
public class ImageMapAttribute extends AbstractComplexAttribute implements ResourceAttributeInterface {

	private static final Logger _logger = LoggerFactory.getLogger(ImageMapAttribute.class);
	
	@Override
	public void setRenderingLang(String langCode) {
		super.setRenderingLang(langCode);
		if (this.getImage() != null) {
			this.getImage().setRenderingLang(langCode);
			if (_areas.size() > 0) {
				Iterator<LinkedArea> iteratorAreas = _areas.iterator();
				while (iteratorAreas.hasNext()) {
					LinkedArea linkedArea = (LinkedArea) iteratorAreas.next();
					linkedArea.getLink().setRenderingLang(langCode);
				}
			}
		}
	}
	
	@Override
	public Object getAttributePrototype() {
		ImageMapAttribute clone = (ImageMapAttribute) super.getAttributePrototype();
		if (this.getImage() != null) {
			clone.setImage((ImageAttribute)this.getImage().getAttributePrototype());
		}
		if (this.getPrototype() != null) {
			clone.setPrototype((LinkedArea)this.getPrototype().clone());
		}
		return clone;
	}
	
	@Override
	public List<AttributeInterface> getAttributes() {
		List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
		attributes.add(_image);
		for(int i = 0; i < _areas.size(); i++ ){
			attributes.add(((LinkedArea)_areas.get(i)).getLink());
		}
		return attributes;
	}
	
	@Override
	public Object getRenderingAttributes() {
		return this;
	}
	
	@Override
	public void setComplexAttributeConfig(Element attributeElement, Map attrTypes) throws ApsSystemException {
		_image = (ImageAttribute) ((ImageAttribute) attrTypes.get("Image")).getAttributePrototype();
		_image.setAttributeConfig(attributeElement);
		_prototype = new LinkedArea();
		_prototype.setLink((LinkAttribute)((LinkAttribute) attrTypes.get("Link")).getAttributePrototype());
		_prototype.getLink().setAttributeConfig(attributeElement);
	}
	
	@Override
	public Element getJDOMElement() {
		Element attributeElement = new Element("imagemap");
		attributeElement.setAttribute("name", this.getName());
		attributeElement.setAttribute("attributetype", "ImageMap");
		
		this.setTimeStamp();
		Element timestampElement = new Element("timestamp");
		timestampElement.setText(this.getTimestamp());
		attributeElement.addContent(timestampElement);
		
		if (_image != null) {
			attributeElement.addContent(_image.getJDOMElement());
			if (_areas.size() > 0) {
				Element areas = new Element("areas");
				Iterator iteratorAreas = _areas.iterator();
				while (iteratorAreas.hasNext()) {
					Element area = new Element("area");
					LinkedArea linkedArea = (LinkedArea) iteratorAreas.next();
					area.setAttribute("shape", linkedArea.getShape());
					area.setAttribute("coords", linkedArea.getCoords());
					area.addContent(linkedArea.getLink().getJDOMElement());
					areas.addContent(area);
				}
				attributeElement.addContent(areas);
			}
		}
		return attributeElement;
	}
	
	public LinkedArea getArea(int index){
		return _areas.get(index);
	}
	public LinkedArea addArea() {
		LinkedArea newArea = (LinkedArea) this._prototype.clone();
		newArea.setShape("rect");
		//TODO per ora solo rect come forma
		newArea.setCoords("0,0,0,0");
		newArea.getLink().setDefaultLangCode(this.getDefaultLangCode());
		this._areas.add(newArea);
		return newArea;
	}
	public void removeArea(int index){
		this._areas.remove(index);
	}
	
	public ImageAttribute getImage() {
		if (null != _image) {
			this._image.setDefaultLangCode(this.getDefaultLangCode());
			this._image.setParentEntity(this.getParentEntity());
		}
		return _image;
	}
	public void setImage(ImageAttribute image) {
		this._image = image;
	}
	
	private LinkedArea getPrototype() {
		return this._prototype;
	}
	private void setPrototype(LinkedArea prototype) {
		this._prototype = prototype;
	}
	
	@Override
	public ResourceInterface getResource() {
		if (this.getImage() != null) {
			return this.getImage().getResource();
		}
		return null;
	}
	@Override
	public ResourceInterface getResource(String langCode) {
		return this.getResource();
	}
	@Override
	public void setResource(ResourceInterface res, String langCode) {
		if (this.getImage() != null) {
			this.getImage().setResource(res, this.getDefaultLangCode());
		}
	}
	
	public List<LinkedArea> getAreas() {
		return this._areas;
	}
	public void setAreas(List<LinkedArea> areas) {		
		this._areas = areas;
	}
	
	public String getTimestamp() {
		return _timestamp;
	}
	private void setTimeStamp() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		String timemillis = String.valueOf(gregorianCalendar.getTimeInMillis());
		this.setTimeStamp(timemillis);
	}
	public void setTimeStamp(String timestamp) {
		this._timestamp = timestamp;
	}
	
	@Override
	public Object getValue() {
		return this;
	}
	
	@Override
	protected Object getJAXBValue(String langCode) {
		JAXBImageMapValue imageMapValue = new JAXBImageMapValue();
		JAXBResourceValue jaxbImageValue = (JAXBResourceValue) this.getImage().getJAXBAttribute(langCode).getValue();
		imageMapValue.setImage(jaxbImageValue);
		for (int i = 0; i < this.getAreas().size(); i++) {
			LinkedArea area = this.getAreas().get(i);
			JAXBAreaValue areaValue = new JAXBAreaValue();
			JAXBLinkValue areaLinkValue = (JAXBLinkValue) area.getLink().getJAXBAttribute(langCode).getValue();
			areaValue.setLink(areaLinkValue);
			areaValue.setShape(area.getShape());
			areaValue.setCoords(area.getCoords());
			imageMapValue.addArea(areaValue);
		}
		return imageMapValue;
	}
	
    public void valueFrom(DefaultJAXBAttribute jaxbAttribute) {
        JAXBImageMapValue value = (JAXBImageMapValue) jaxbAttribute.getValue();
        if (null == value) return;
        JAXBResourceValue jaxbImageValue = value.getImage();
        if (null == jaxbImageValue) return;
        try {
            IResourceManager resourceManager = this.getResourceManager();
            ResourceInterface resource = resourceManager.loadResource(jaxbImageValue.getResourceId().toString());
            if (null != resource) {
                this.setResource(resource, this.getDefaultLangCode());
            }
			if (null != value.getAreas()) {
				for (int i = 0; i < value.getAreas().size(); i++) {
					JAXBAreaValue areaValue = value.getAreas().get(i);
					JAXBLinkValue areaLinkValue = areaValue.getLink();
					if (null == areaValue || null == areaLinkValue) continue;
					LinkedArea linkedArea = (LinkedArea) this.getPrototype().clone();
					linkedArea.setShape(areaValue.getShape());
					linkedArea.setCoords(areaValue.getCoords());
					linkedArea.getLink().setSymbolicLink(areaLinkValue.getSymbolikLink());
					Object textValue = areaLinkValue.getText();
					if (null == textValue) return;
					linkedArea.getLink().getTextMap().put(this.getDefaultLangCode(), textValue.toString());
				}
			}
        } catch (Exception e) {
        	_logger.error("Error extracting linked area from jaxbAttribute", e);
        }
    }
    
    protected IResourceManager getResourceManager() {
        return (IResourceManager) this.getBeanFactory().getBean(JacmsSystemConstants.RESOURCE_MANAGER);
    }
    
    public Status getStatus() {
        Status resourceStatus = (null != this.getResource()) ? Status.VALUED : Status.EMPTY;
        Status linksStatus = (null != this.getAreas() && this.getAreas().size() > 0) ? Status.VALUED : Status.EMPTY;
        if (!linksStatus.equals(resourceStatus)) return Status.INCOMPLETE;
        if (linksStatus.equals(resourceStatus) && linksStatus.equals(Status.VALUED)) return Status.VALUED;
        return Status.EMPTY;
    }
    
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = super.validate(tracer);
        try {
            if (null == this.getResource()) return errors;
			List<LinkedArea> areas = this.getAreas();
			for (int i = 0; i < areas.size(); i++) {
				LinkedArea area = (LinkedArea) areas.get(i);
				AttributeTracer areaTracer = (AttributeTracer) tracer.clone();
				areaTracer.setMonoListElement(true);
				areaTracer.setListIndex(i);
				LinkAttribute linkAttribute = area.getLink();
				if (null != linkAttribute) {
					errors.addAll(linkAttribute.validate(areaTracer));
				}
				String coords = area.getCoords();
				boolean isShapeValued = (area.getShape() != null && area.getShape().trim().length() > 0 );
				boolean isCoordsValued = (coords!= null && coords.trim().length() > 0 && this.isValidNumber(coords));
				if (!isShapeValued || !isCoordsValued) {
					errors.add(new AttributeFieldError(this, INVALID_LINKED_AREA_ERROR, areaTracer));
					/*
					String formFieldName = tracer.getFormFieldName(imageMapAttribute);
					String[] args = { imageMapAttribute.getName(), String.valueOf(tracer.getListIndex()+1) };
					this.addFieldError(action, formFieldName, "Content.linkedAreaElement.invalidArea.maskmsg", args);
					 */
				}
				this.isIntersected(area, areaTracer, errors);
			}
        } catch (Throwable t) {
        	_logger.error("Error validating image map attribute", t);
            throw new RuntimeException("Error validating image map attribute", t);
        }
        return errors;
    }
    
	private boolean isValidNumber(String coords) {
		Pattern pattern = Pattern.compile("^\\d+,\\d+,\\d+,\\d+$");
		Matcher matcher = pattern.matcher(coords.trim());
		return matcher.matches();
	}
	
	private void isIntersected(LinkedArea area, AttributeTracer tracer, List<AttributeFieldError> errors) {
		int index = tracer.getListIndex();
		Integer[] coordsArray = area.getArrayCoords();
		if (null == coordsArray) return;
		Rectangle areaRect = 
			new Rectangle(coordsArray[0].intValue(), coordsArray[1].intValue(), coordsArray[2].intValue() - coordsArray[0].intValue() , coordsArray[3].intValue() - coordsArray[1].intValue());
		for (int i=index-1; i>=0 ; i--){
			LinkedArea currentArea = this.getArea(i);
			Integer[] currentCoordsArray = currentArea.getArrayCoords();
			Rectangle currentAreaRect = new Rectangle(currentCoordsArray[0].intValue(),currentCoordsArray[1].intValue(), 
					currentCoordsArray[2].intValue() - currentCoordsArray[0].intValue() , currentCoordsArray[3].intValue() - currentCoordsArray[1].intValue());
			boolean intersect = areaRect.intersects(currentAreaRect);
			if (intersect) {
				errors.add(new AttributeFieldError(this, INVALID_LINKED_AREA_ERROR, tracer));
			}
		}
	}
	
	public static final String INVALID_LINKED_AREA_ERROR = "INVALID_LINKED_AREA_ERROR";
	public static final String INTERSECTED_AREA_ERROR = "INTERSECTED_AREA_ERROR";
	
	private ImageAttribute _image;
	private String _timestamp;
	private List<LinkedArea> _areas = new ArrayList<LinkedArea>();
	private LinkedArea _prototype;
	
}