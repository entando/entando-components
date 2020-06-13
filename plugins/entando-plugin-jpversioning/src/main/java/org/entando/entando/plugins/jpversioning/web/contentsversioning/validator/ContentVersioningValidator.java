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
package org.entando.entando.plugins.jpversioning.web.contentsversioning.validator;

import static com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager.VERSION_CONTENT_TYPE_FILTER_KEY;
import static com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager.VERSION_DESCRIPTION_FILTER_KEY;
import static com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager.VERSION_ID_FILTER_KEY;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import java.util.Arrays;
import java.util.List;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ContentVersioningValidator extends AbstractPaginationValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private VersioningManager versioningManager;

    public boolean contentVersioningExist(String contentId) {
        boolean validationResult = false;
        try {
            if (versioningManager.getVersions(contentId) != null) {
                return true;
            }
        } catch (ApsSystemException e) {
            e.printStackTrace();
        }
        return validationResult;
    }

    public void checkFilterKeys(FieldSearchFilter[] filters) throws ApsSystemException {
        if (null != filters && filters.length > 0) {
            String[] allowedFilterKeys = {VERSION_CONTENT_TYPE_FILTER_KEY, VERSION_DESCRIPTION_FILTER_KEY};
            List<String> allowedFilterKeysList = Arrays.asList(allowedFilterKeys);
            for (int i = 0; i < filters.length; i++) {
                FieldSearchFilter filter = filters[i];
                if (!allowedFilterKeysList.contains(filter.getKey())) {
                    throw new ApsSystemException("Invalid filter key - '" + filter.getKey() + "'");
                }
            }
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void validate(Object o, Errors errors) {}

    @Override
    protected String getDefaultSortProperty() {
        return VERSION_ID_FILTER_KEY;
    }

    @Override
    public boolean isValidField(String fieldName, Class<?> type) {
        if (fieldName.contains(".")) {
            return true;
        } else {
            return Arrays.asList(IVersioningManager.METADATA_FILTER_KEYS).contains(fieldName);
        }
    }
}
