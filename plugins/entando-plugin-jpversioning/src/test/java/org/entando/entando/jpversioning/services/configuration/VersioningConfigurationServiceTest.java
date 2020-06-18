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
package org.entando.entando.jpversioning.services.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.entando.entando.plugins.jpversioning.services.configuration.VersioningConfigurationService;
import org.entando.entando.plugins.jpversioning.web.configuration.model.VersioningConfigurationDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VersioningConfigurationServiceTest extends TestCase {
    private static String DELETE_MID_VERSION_FALSE = "false";
    private static String DELETE_MID_VERSION_TRUE = "true";
    private static String CONTENTS_TO_IGNORE = "TST1, TST2";
    private static String CONTENT_TYPES_TO_IGNORE = "CT1, CT2";
    private List<String> contentsToIgnoreList = new ArrayList<>();
    private List<String> contentTypesToIgnoreList = new ArrayList<>();
    private static String SPLIT_REGEX = "\\s*,\\s*";

    @Mock
    private BaseConfigManager baseConfigManager;

    @InjectMocks
    private VersioningConfigurationService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        contentsToIgnoreList = Arrays.asList(CONTENTS_TO_IGNORE.trim().split(SPLIT_REGEX));
        contentTypesToIgnoreList =Arrays.asList(CONTENT_TYPES_TO_IGNORE.trim().split(SPLIT_REGEX));

    }
    @Test
    public void testGetVersioningConfigurationNotExists() {
        when(baseConfigManager.getParam(JpversioningSystemConstants.CONFIG_PARAM_DELETE_MID_VERSIONS)).thenReturn(null);
        when(baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE)).thenReturn(null);
        when(baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE)).thenReturn(null);

        final VersioningConfigurationDTO versioningConfiguration = service.getVersioningConfiguration();

        List<String> contentsToIgnoreList = new ArrayList<>();
        List<String> contentTypesToIgnoreList = new ArrayList<>();

        assertThat(versioningConfiguration.getDeleteMidVersions()).isEqualTo(false);
        assertThat(versioningConfiguration.getContentsToIgnore()).isEqualTo(contentsToIgnoreList);
        assertThat(versioningConfiguration.getContentTypesToIgnore()).isEqualTo(contentTypesToIgnoreList);
    }

    @Test
    public void testGetVersioningConfigurationDeleteMidVersionTrue() {
        when(baseConfigManager.getParam(JpversioningSystemConstants.CONFIG_PARAM_DELETE_MID_VERSIONS)).thenReturn(DELETE_MID_VERSION_TRUE);
        when(baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE)).thenReturn(CONTENTS_TO_IGNORE);
        when(baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE)).thenReturn(CONTENT_TYPES_TO_IGNORE);

        final VersioningConfigurationDTO versioningConfiguration = service.getVersioningConfiguration();

        assertThat(versioningConfiguration.getDeleteMidVersions()).isEqualTo(true);
        assertThat(versioningConfiguration.getContentsToIgnore()).isEqualTo(contentsToIgnoreList);
        assertThat(versioningConfiguration.getContentTypesToIgnore()).isEqualTo(contentTypesToIgnoreList);
    }
    @Test
    public void testGetVersioningConfigurationDeleteMidVersionFalse() {
        when(baseConfigManager.getParam(JpversioningSystemConstants.CONFIG_PARAM_DELETE_MID_VERSIONS)).thenReturn(DELETE_MID_VERSION_FALSE);
        when(baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE)).thenReturn(CONTENTS_TO_IGNORE);
        when(baseConfigManager.getParam(
                JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE)).thenReturn(CONTENT_TYPES_TO_IGNORE);

        final VersioningConfigurationDTO versioningConfiguration = service.getVersioningConfiguration();


        assertThat(versioningConfiguration.getDeleteMidVersions()).isEqualTo(false);
        assertThat(versioningConfiguration.getContentsToIgnore()).isEqualTo(contentsToIgnoreList);
        assertThat(versioningConfiguration.getContentTypesToIgnore()).isEqualTo(contentTypesToIgnoreList);
    }
}
