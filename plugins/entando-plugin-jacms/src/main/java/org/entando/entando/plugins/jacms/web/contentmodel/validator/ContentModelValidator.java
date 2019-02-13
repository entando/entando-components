/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.web.contentmodel.validator;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.validation.Errors;

public class ContentModelValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_CONTENTMODEL_NOT_FOUND = "1";
    public static final String ERRCODE_CONTENTMODEL_ALREADY_EXISTS = "2";
    public static final String ERRCODE_URINAME_MISMATCH = "3";
    public static final String ERRCODE_CONTENTMODEL_CANNOT_UPDATE_CONTENT_TYPE = "4";
    public static final String ERRCODE_CONTENTMODEL_REFERENCES = "5";

    public static final String ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND = "6";
    public static final String ERRCODE_CONTENTMODEL_WRONG_UTILIZER = "7";

    @Override
    public boolean supports(Class<?> paramClass) {
        return ContentModelDto.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validateBodyName(long modelId, ContentModelDto contentModel, Errors errors) {
        if (modelId != contentModel.getId()) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{modelId, contentModel.getId()}, "contentmodel.code.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }
}
