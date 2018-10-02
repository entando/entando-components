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
package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.ArrayList;
import java.util.Collection;

import org.entando.entando.aps.system.common.command.tracer.DefaultBulkCommandTracer;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.category.JoinCategoryBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.category.RemoveCategoryBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.JoinGroupBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.RemoveGroupBulkCommand;
import org.springframework.context.ApplicationContext;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import static java.lang.Compiler.command;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.services.command.IBulkCommandManager;

public class TestContentBulkCommand extends BaseTestCase {

    private ICategoryManager categoryManager;
    private IContentManager contentManager;
    private IBulkCommandManager bulkCommandManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
            categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
            bulkCommandManager = (IBulkCommandManager) this.getService("BulkCommandManager");
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    public void testGroupCommands() {
        Collection<String> items = new ArrayList<String>();
        Collection<String> groups = new ArrayList<String>();
        UserDetails currentUser = null;
        BaseContentPropertyBulkCommand<String> groupCommand = this.initGroupsCommand(JoinGroupBulkCommand.BEAN_NAME, items, groups, currentUser);
        assertNotNull(groupCommand);

        groupCommand = this.initGroupsCommand(RemoveGroupBulkCommand.BEAN_NAME, items, groups, currentUser);
        assertNotNull(groupCommand);
    }

    public void testCategoryCommands() throws Exception {
        Category cat1 = this.createCategory("1");
        Category cat2 = this.createCategory("2");
        try {
            categoryManager.addCategory(cat1);
            categoryManager.addCategory(cat2);
            Collection<String> items = new ArrayList<String>();
            items.add("ART102");
            items.add("ART111");
            items.add("ART120");
            items.add("ART122");
            Collection<Category> categories = new ArrayList<Category>();
            categories.add(cat1);
            categories.add(cat2);
            UserDetails currentUser = this.getUser("admin");

            //join
            BaseContentPropertyBulkCommand<Category> categoryCommand = this.initCategoriesCommand(JoinCategoryBulkCommand.BEAN_NAME, items, categories, currentUser);
            assertNotNull(categoryCommand);
            BulkCommandReport<String> report = bulkCommandManager.addCommand("jacms", categoryCommand);
            assertNotNull(report);
            Content content = contentManager.loadContent("ART102", false);
            assertEquals(1, content.getCategories().stream().filter(cat -> cat.getCode().equals(cat1.getCode())).count());
            Content content2 = contentManager.loadContent("ART122", false);
            assertEquals(1, content2.getCategories().stream().filter(cat -> cat.getCode().equals(cat2.getCode())).count());

            //remove
            categoryCommand = this.initCategoriesCommand(RemoveCategoryBulkCommand.BEAN_NAME, items, categories, currentUser);
            assertNotNull(categoryCommand);
            report = bulkCommandManager.addCommand("jacms", categoryCommand);
            assertNotNull(report);
            content = contentManager.loadContent("ART102", false);
            assertEquals(0, content.getCategories().stream().filter(cat -> cat.getCode().equals(cat1.getCode())).count());
            content2 = contentManager.loadContent("ART122", false);
            assertEquals(0, content2.getCategories().stream().filter(cat -> cat.getCode().equals(cat2.getCode())).count());
        } finally {
            categoryManager.deleteCategory(cat1.getCode());
            categoryManager.deleteCategory(cat2.getCode());
        }
    }

    private BaseContentPropertyBulkCommand<Category> initCategoriesCommand(String commandBeanName,
            Collection<String> items, Collection<Category> categories, UserDetails currentUser) {
        ApplicationContext applicationContext = this.getApplicationContext();
        BaseContentPropertyBulkCommand<Category> command = (BaseContentPropertyBulkCommand<Category>) applicationContext.getBean(commandBeanName);
        ContentPropertyBulkCommandContext<Category> context = new ContentPropertyBulkCommandContext<Category>(items,
                categories, currentUser, new DefaultBulkCommandTracer<String>());
        command.init(context);
        return command;
    }

    private BaseContentPropertyBulkCommand<String> initGroupsCommand(String commandBeanName,
            Collection<String> items, Collection<String> groups, UserDetails currentUser) {
        ApplicationContext applicationContext = this.getApplicationContext();
        BaseContentPropertyBulkCommand<String> command = (BaseContentPropertyBulkCommand<String>) applicationContext.getBean(commandBeanName);
        ContentPropertyBulkCommandContext<String> context = new ContentPropertyBulkCommandContext<String>(items,
                groups, currentUser, new DefaultBulkCommandTracer<String>());
        command.init(context);
        return command;
    }

    private Category createCategory(String id) {
        Category cat = new Category();
        cat.setDefaultLang("it");
        cat.setCode("cat_" + id);
        Category parent = categoryManager.getCategory("cat1");
        cat.setParent(parent);
        cat.setParentCode(parent.getCode());
        ApsProperties titles = new ApsProperties();
        titles.put("it", "Titolo in Italiano per " + id);
        titles.put("en", "Titolo in Inglese per " + id);
        cat.setTitles(titles);
        cat.setDefaultLang("en");
        return cat;
    }

}
