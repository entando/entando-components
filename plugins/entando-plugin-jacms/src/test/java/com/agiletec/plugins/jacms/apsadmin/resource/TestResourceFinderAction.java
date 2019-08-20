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

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class TestResourceFinderAction extends ApsAdminBaseTestCase {

    public void testViewImageResources() throws Throwable {
        String result = this.executeShowList("admin", "Image");
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        String resourceTypeCode = action.getResourceTypeCode();
        assertNotNull(resourceTypeCode);
        assertEquals("Image", resourceTypeCode);
        assertEquals(3, action.getResources().size());
        Category root = ((ResourceFinderAction) action).getCategoryRoot();
        assertNotNull(root);
        assertEquals("Home", root.getTitle());
    }

    public void testViewAttachResources() throws Throwable {
        String result = this.executeShowList("admin", "Attach");
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        String resourceTypeCode = action.getResourceTypeCode();
        assertNotNull(resourceTypeCode);
        assertEquals("Attach", resourceTypeCode);
        assertEquals(3, action.getResources().size());
        Category root = ((ResourceFinderAction) action).getCategoryRoot();
        assertNotNull(root);
        assertEquals("Home", root.getTitle());
    }

    public void testViewImageResourcesByCustomerUser() throws Throwable {
        String result = this.executeShowList("editorCustomers", "Image");
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        String resourceTypeCode = action.getResourceTypeCode();
        assertNotNull(resourceTypeCode);
        assertEquals("Image", resourceTypeCode);
        assertEquals(1, action.getResources().size());
        Category root = ((ResourceFinderAction) action).getCategoryRoot();
        assertNotNull(root);
        assertEquals("Home", root.getTitle());
    }

    public void testViewImagesWithUserNotAllowed() throws Throwable {
        String result = this.executeShowList("pageManagerCustomers", "Image");
        assertEquals("userNotAllowed", result);
    }

    private String executeShowList(String userName, String resourceTypeCode) throws Throwable {
        this.setUserOnSession(userName);
        this.initAction("/do/jacms/Resource", "list");
        this.addParameter("resourceTypeCode", resourceTypeCode);
        return this.executeAction();
    }

    public void testSearchResources_1() throws Throwable {
        String result = this.executeSearchResource("admin", "Attach", "WrongDescription", null, null, null);
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        assertTrue(action.getResources().isEmpty());
        assertEquals("WrongDescription", action.getText());
    }

    public void testSearchResources_2() throws Throwable {
        String result = this.executeSearchResource("admin", "Attach", "", null, null, null);
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        assertEquals(3, action.getResources().size());
        assertEquals("", action.getText());
    }

    public void testSearchResources_3() throws Throwable {
        String result = this.executeSearchResource("admin", "Image", null, null, "jpg", null);
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        assertEquals(3, action.getResources().size());
        assertEquals("jpg", action.getFileName());

        result = this.executeSearchResource("admin", "Image", null, null, "and", null);
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        assertEquals(2, action.getResources().size());
        assertEquals("and", action.getFileName());

        result = this.executeSearchResource("admin", "Image", null, null, "ando.JPG", null);
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        assertEquals(1, action.getResources().size());
        assertEquals("ando.JPG", action.getFileName());
    }

    public void testSearchByCategory() throws Throwable {
        String result = this.executeSearchResource("admin", "Image", "", null, null, "resCat1");
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        assertEquals(1, action.getResources().size());
        assertEquals("", action.getText());
        assertEquals("resCat1", action.getCategoryCode());

        result = this.executeSearchResource("admin", "Image", "log", null, null, "resCat1");
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        assertEquals(1, action.getResources().size());

        result = this.executeSearchResource("admin", "Image", "japs", null, null, "resCat1");
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        assertTrue(action.getResources().isEmpty());
    }

    public void testSearchByGroup_1() throws Throwable {
        String result = this.executeSearchResource("admin", "Image", null, Group.FREE_GROUP_NAME, null, null);
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        assertEquals(2, action.getResources().size());

        result = this.executeSearchResource("admin", "Image", null, Group.ADMINS_GROUP_NAME, null, null);
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        assertEquals(0, action.getResources().size());

        result = this.executeSearchResource("admin", "Image", null, "customers", null, null);
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        assertEquals(1, action.getResources().size());
    }

    public void testSearchByGroup_2() throws Throwable {
        String result = this.executeSearchResource("editorCoach", "Image", null, Group.FREE_GROUP_NAME, null, null);
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        assertEquals(0, action.getResources().size());

        result = this.executeSearchResource("editorCoach", "Image", null, Group.ADMINS_GROUP_NAME, null, null);
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        assertEquals(0, action.getResources().size());

        result = this.executeSearchResource("editorCoach", "Image", null, "customers", null, null);
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        assertEquals(1, action.getResources().size());
    }

    public void testSearchWithOrder_1() throws Throwable {
        List<String> expected = Arrays.asList(new String[]{"22", "44", "82"});
        this.executeTestSearchWithOrder_1(expected, "created");
        expected = Arrays.asList(new String[]{"44", "82", "22"});
        this.executeTestSearchWithOrder_1(expected, "lastModified");
        expected = Arrays.asList(new String[]{"82", "22", "44"});
        this.executeTestSearchWithOrder_1(expected, "descr");
    }

    public void testSearchWithOrder_2() throws Throwable {
        String result = this.executeSearchResourceWithOrder("admin", "Image", "lastModified", "created", "DESC");
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        List<String> listResult = action.getResources();
        List<String> expected = Arrays.asList(new String[]{"44", "82", "22"});
        assertEquals(expected, listResult);
    }

    private void executeTestSearchWithOrder_1(List<String> expected, String field) throws Throwable {
        String result = this.executeSearchResourceWithOrder("admin", "Image", field, field, "DESC");
        assertEquals(Action.SUCCESS, result);
        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        List<String> listResult = action.getResources();
        assertEquals(expected, listResult);

        result = this.executeSearchResourceWithOrder("admin", "Image", field, field, "ASC");
        assertEquals(Action.SUCCESS, result);
        action = (ResourceFinderAction) this.getAction();
        listResult = action.getResources();
        Collections.reverse(expected);
        assertEquals(expected, listResult);
    }

    private String executeSearchResource(String username, String resourceTypeCode,
            String text, String ownerGroupName, String fileName, String categoryCode) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/jacms/Resource", "search");
        this.addParameter("resourceTypeCode", resourceTypeCode);
        this.addParameter("text", text);
        this.addParameter("fileName", fileName);
        this.addParameter("ownerGroupName", ownerGroupName);
        this.addParameter("categoryCode", categoryCode);
        return this.executeAction();
    }

    private String executeSearchResourceWithOrder(String username, String resourceTypeCode,
            String groupBy, String lastGroupBy, String lastOrder) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/jacms/Resource", "search");
        this.addParameter("resourceTypeCode", resourceTypeCode);
        this.addParameter("groupBy", groupBy);
        this.addParameter("lastGroupBy", lastGroupBy);
        this.addParameter("lastOrder", lastOrder);
        return this.executeAction();
    }

}
