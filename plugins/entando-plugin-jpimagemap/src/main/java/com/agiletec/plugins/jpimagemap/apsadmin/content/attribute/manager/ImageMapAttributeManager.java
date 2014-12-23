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
package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.manager;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.apsadmin.system.entity.attribute.manager.AbstractAttributeManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ImageAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.ImageMapAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.util.LinkedArea;

/**
 * Classe manager degli attributi tipo ImageMap.
 * @author E.Santoboni - G.Cocco
 */
public class ImageMapAttributeManager extends AbstractAttributeManager {
	
	@Override
	protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
		this.manageImageMapAttribute(attribute, tracer, request);
	}
	
	private void manageImageMapAttribute(AttributeInterface attribute, 
			AttributeTracer tracer, HttpServletRequest request) {
		ImageMapAttribute imageMapAttribute = (ImageMapAttribute) attribute;
		ImageAttribute imageAttr = imageMapAttribute.getImage();		
		AttributeTracer imageTracer = (AttributeTracer) tracer.clone();
		ResourceAttributeManager imageManager = (ResourceAttributeManager) this.getManager(imageAttr);
		imageManager.updateAttribute(imageAttr, imageTracer, request);
		List<LinkedArea> areas = imageMapAttribute.getAreas();
		for (int i = 0; i < areas.size(); i++) {
			LinkedArea area = (LinkedArea) areas.get(i);
			AttributeTracer areaTracer = (AttributeTracer) tracer.clone();
			areaTracer.setMonoListElement(true);
			areaTracer.setListIndex(i);
			LinkAttribute link = area.getLink();
			LinkAttributeManager linkManager = (LinkAttributeManager) this.getManager(link);
			String coords = request.getParameter(imageMapAttribute.getName() + "_coords_" + areaTracer.getListIndex());
			linkManager.updateAttribute(link, areaTracer, request);
			area.setCoords(coords);
		}
	}
	
	@Override
	protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
		String errorCode = attributeFieldError.getErrorCode();
        String messageKey = null;
        if (errorCode.equals(ImageMapAttribute.INVALID_LINKED_AREA_ERROR)) {
            messageKey = "ImageMapAttribute.fieldError.linkedAreaElement.invalidArea";
        } else if (errorCode.equals(ImageMapAttribute.INTERSECTED_AREA_ERROR)) {
            messageKey = "ImageMapAttribute.fieldError.linkedAreaElement.intersectedArea";
        }
        if (null != messageKey) {
            return action.getText(messageKey);
        } else {
            return super.getCustomAttributeErrorMessage(attributeFieldError, action);
        }
    }
	
}