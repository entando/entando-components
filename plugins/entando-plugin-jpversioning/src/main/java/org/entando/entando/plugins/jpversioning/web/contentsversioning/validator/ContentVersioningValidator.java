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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentVersioningValidator {

    @Autowired
    private VersioningManager versioningManager;

    public boolean contentVersioningExist(String contentId){
        boolean validationResult =false;
        try {
            if (versioningManager.getVersions(contentId) != null) {
                validationResult = true;
            }
        } catch (ApsSystemException e) {
            e.printStackTrace();
        }
        return validationResult;
    }

    public boolean checkContentIdForVersion(String contentId, Long versionId) {
        boolean validationResult = false;
        try {
            final ContentVersion version = versioningManager.getVersion(versionId);
            if (version.getContentId().equals(contentId)){
                return true;
            }
        } catch (ApsSystemException e) {
            e.printStackTrace();
        }
        return validationResult;
    }
}
