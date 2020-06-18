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
package org.entando.entando.plugins.jpversioning.services.configuration;

import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.entando.entando.plugins.jpversioning.web.configuration.model.VersioningConfigurationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersioningConfigurationService {

    private static String SPLIT_REGEX = "\\s*,\\s*";

    @Autowired
    private BaseConfigManager baseConfigManager;

    public VersioningConfigurationDTO getVersioningConfiguration() {

        final String deleteMidVersionsConfigItem = baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_DELETE_MID_VERSIONS);
        final String contentsToIgnoreConfigItem = baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE);
        final String contentTypesToIgnoreConfigItem = baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE);

        List<String> contentsToIgnoreList = new ArrayList<>();
        if (contentsToIgnoreConfigItem != null) {
            contentsToIgnoreList = Arrays.asList(contentsToIgnoreConfigItem.trim().split(SPLIT_REGEX));
        }

        List<String> contentTypesToIgnoreList = new ArrayList<>();
        if (contentTypesToIgnoreConfigItem != null) {
            contentTypesToIgnoreList = Arrays.asList(contentTypesToIgnoreConfigItem.trim().split(SPLIT_REGEX));
        }

        VersioningConfigurationDTO versioningConfigurationDTO = new VersioningConfigurationDTO();

        if (Boolean.parseBoolean(deleteMidVersionsConfigItem)) {
            versioningConfigurationDTO.setDeleteMidVersions(true);
        } else {
            versioningConfigurationDTO.setDeleteMidVersions(false);
        }
        versioningConfigurationDTO.setContentsToIgnore(contentsToIgnoreList);
        versioningConfigurationDTO.setContentTypesToIgnore(contentTypesToIgnoreList);

        return versioningConfigurationDTO;
    }

}
