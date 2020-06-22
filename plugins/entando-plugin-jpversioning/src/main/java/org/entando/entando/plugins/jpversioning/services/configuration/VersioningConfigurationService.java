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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpversioning.web.configuration.model.VersioningConfigurationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersioningConfigurationService {

    private static String SPLIT_REGEX = "\\s*,\\s*";
    private static String JOIN_STR = ", ";

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

    public VersioningConfigurationDTO putVersioningConfiguration(
            VersioningConfigurationDTO versioningConfigurationDTO) {
        try {
            final String deleteMidVersions = versioningConfigurationDTO.getDeleteMidVersions().toString();
            final List<String> contentsToIgnore = versioningConfigurationDTO.getContentsToIgnore();
            final List<String> contentTypesToIgnore = versioningConfigurationDTO.getContentTypesToIgnore();
            this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_DELETE_MID_VERSIONS, deleteMidVersions);
            this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE,
                    String.join(JOIN_STR, contentsToIgnore));
            this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE,
                    String.join(JOIN_STR, contentTypesToIgnore));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getVersioningConfiguration();
    }

    private void updateConfigItem(String paramKey, String paramValue) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey, paramValue);
        String xmlParams = baseConfigManager.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
        String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, params, true);
        baseConfigManager.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
    }

}
