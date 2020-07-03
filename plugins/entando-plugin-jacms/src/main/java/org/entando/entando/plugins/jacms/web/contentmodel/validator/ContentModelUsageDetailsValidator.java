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
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ContentModelUsageDetailsValidator extends AbstractPaginationValidator {

    @Override
    public boolean supports(Class<?> paramClass) {
        return ContentModelDto.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    @Override
    protected String getDefaultSortProperty() {
        return "code";
    }
}
