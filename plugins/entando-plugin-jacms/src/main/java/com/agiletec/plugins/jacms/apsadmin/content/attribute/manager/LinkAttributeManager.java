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
import com.agiletec.apsadmin.system.entity.attribute.manager.TextAttributeManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.ICmsAttributeErrorCodes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe manager degli attributi tipo link.
 * @author E.Santoboni
 */
public class LinkAttributeManager extends TextAttributeManager {

	@Override
    protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        String errorCode = attributeFieldError.getErrorCode();
        String messageKey = null;
        if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_PAGE)) {
            messageKey = "LinkAttribute.fieldError.linkToPage";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.VOID_PAGE)) {
            messageKey = "LinkAttribute.fieldError.linkToPage.voidPage";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_CONTENT)) {
            messageKey = "LinkAttribute.fieldError.linkToContent";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_PAGE_GROUPS)) {
            messageKey = "LinkAttribute.fieldError.linkToPage.wrongGroups";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_CONTENT_GROUPS)) {
            messageKey = "LinkAttribute.fieldError.linkToContent.wrongGroups";
        }
        if (null != messageKey) {
            return action.getText(messageKey);
        } else {
            return super.getCustomAttributeErrorMessage(attributeFieldError, action);
        }
    }

	@Override
    protected String getInvalidAttributeMessage() {
        return "LinkAttribute.fieldError.invalidLink";
    }

}