/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.apsadmin.content.attribute.manager;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.util.ICmsAttributeErrorCodes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe manager degli attributi tipo Hypertext.
 * @author E.Santoboni
 */
public class HypertextAttributeManager extends com.agiletec.apsadmin.system.entity.attribute.manager.TextAttributeManager {

	@Override
    protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        String errorCode = attributeFieldError.getErrorCode();
        String messageKey = null;
        if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_PAGE)) {
            messageKey = "HypertextAttribute.fieldError.linkToPage";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.VOID_PAGE)) {
            messageKey = "HypertextAttribute.fieldError.linkToPage.voidPage";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_CONTENT)) {
            messageKey = "HypertextAttribute.fieldError.linkToContent";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_PAGE_GROUPS)) {
            messageKey = "HypertextAttribute.fieldError.linkToPage.wrongGroups";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_CONTENT_GROUPS)) {
            messageKey = "HypertextAttribute.fieldError.linkToContent.wrongGroups";
        }
        if (null != messageKey) {
            String[] args = {attributeFieldError.getTracer().getLang().getDescr()};
            return action.getText(messageKey, args);
        } else {
            return super.getCustomAttributeErrorMessage(attributeFieldError, action);
        }
    }

}