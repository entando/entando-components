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
package com.agiletec.plugins.jacms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.agiletec.apsadmin.TestLabelsProperties;

public class TestJacmsLabelsProperties extends TestLabelsProperties {

    private static final Logger logger = LoggerFactory.getLogger(TestLabelsProperties.class);
    private static String PLUGIN_PATH = "com/agiletec/plugins/jacms/apsadmin/";

    public void testGlobalProperties() throws Throwable {
        super.testGlobalMessagesLabelsTranslations(PLUGIN_PATH);
    }

    public void testResourceProperties() throws Throwable {
        super.testPackageLabelsTranslations(PLUGIN_PATH + "resource/");
    }

}
