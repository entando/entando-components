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
package com.agiletec.plugins.jacms.apsadmin.resource;


import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.opensymphony.xwork2.Action;

public class TestMultipleResourceAction extends ApsAdminBaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            _resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    public void testAddUploadField() throws Throwable {
        this.setUserOnSession("admin");
        this.initAction("/do/jacms/Resource", "increaseCount");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
        MultipleResourceAction action = (MultipleResourceAction) this.getAction();
        action.increaseCount();
        assertEquals(2, action.getFieldCount());
    }

    public void testDecreaseUploadField() throws Throwable {
        this.setUserOnSession("admin");
        this.initAction("/do/jacms/Resource", "increaseCount");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
        MultipleResourceAction action = (MultipleResourceAction) this.getAction();
        action.decreaseCount();
        assertEquals(0, action.getFieldCount());
    }
    private IResourceManager _resourceManager = null;

}
