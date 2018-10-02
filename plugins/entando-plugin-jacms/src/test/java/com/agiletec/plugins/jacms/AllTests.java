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
package com.agiletec.plugins.jacms;

import com.agiletec.plugins.jacms.aps.system.TestApplicationContext;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManagerTest;
import com.agiletec.plugins.jacms.aps.system.services.content.TestCategoryUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.TestContentDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.TestContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.TestGroupUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.TestPublicContentSearcherDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.TestValidateContent;
import com.agiletec.plugins.jacms.aps.system.services.content.authorization.TestContentAuthorization;
import com.agiletec.plugins.jacms.aps.system.services.content.entity.TestContentEntityManager;
import com.agiletec.plugins.jacms.aps.system.services.content.parse.TestContentDOM;
import com.agiletec.plugins.jacms.aps.system.services.content.util.TestContentAttributeIterator;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.TestContentListHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.TestContentViewerHelper;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModelManagerIntegrationTest;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModelManagerTest;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.TestContentModelDAO;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.ContentPageMapperManagerIntegrationTest;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.ContentPageMapperManagerTest;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.cache.ContentMapperCacheWrapperTest;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.TestContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.TestLinkResolverManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.ResourceManagerIntegrationTest;
import com.agiletec.plugins.jacms.aps.system.services.resource.ResourceManagerTest;
import com.agiletec.plugins.jacms.aps.system.services.resource.TestResourceDAO;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.TestResourceDOM;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.TestSearchEngineManager;
import com.agiletec.plugins.jacms.apsadmin.category.TestTrashReferencedCategory;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentAdminAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentFinderAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentGroupAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestContentInspectionAction;
import com.agiletec.plugins.jacms.apsadmin.content.TestIntroNewContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestContentLinkAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestExtendedResourceAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestExtendedResourceFinderAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestHypertextAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestLinkAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestListAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestPageLinkAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestResourceAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.TestUrlLinkAction;
import com.agiletec.plugins.jacms.apsadmin.content.model.TestContentModelAction;
import com.agiletec.plugins.jacms.apsadmin.content.model.TestContentModelFinderAction;
import com.agiletec.plugins.jacms.apsadmin.portal.TestPageAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestBaseFilterAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestContentListViewerWidgetAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestDateAttributeFilterAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestNumberAttributeFilterAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TestTextAttributeFilterAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer.TestContentFinderViewerAction;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer.TestContentViewerWidgetAction;
import com.agiletec.plugins.jacms.apsadmin.resource.TestMultipleResourceAction;
import com.agiletec.plugins.jacms.apsadmin.resource.TestResourceFinderAction;
import com.agiletec.plugins.jacms.TestJacmsLabelsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.AbstractResourceAttributeTest;
import com.agiletec.plugins.jacms.apsadmin.system.entity.TestJacmsEntityAttributeConfigAction;
import com.agiletec.plugins.jacms.apsadmin.system.entity.TestJacmsEntityManagersAction;
import com.agiletec.plugins.jacms.apsadmin.system.entity.TestJacmsEntityTypeConfigAction;
import com.agiletec.plugins.jacms.apsadmin.user.group.TestTrashReferencedGroup;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.plugins.jacms.aps.system.services.api.TestApiContentInterface;
import org.entando.entando.plugins.jacms.aps.system.services.cache.TestCacheInfoManager;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.TestContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.page.TestCmsPageManagerWrapper;
import org.entando.entando.plugins.jacms.apsadmin.common.TestActivityStream;
import org.entando.entando.plugins.jacms.apsadmin.content.TestContentPreviewAction;
import org.entando.entando.plugins.jacms.apsadmin.content.TestSaveBooleanAttributes;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateBooleanAttributes;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateDateAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateMonotextAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateNumberAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateResourceAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.TestValidateTextAttribute;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.TestContentCategoryBulkAction;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.TestContentGroupBulkAction;
import org.entando.entando.plugins.jacms.apsadmin.portal.TestPageActionReferences;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for jACMS");

        System.out.println("Test for jACMS plugin");

        //
        suite.addTestSuite(TestContentAuthorization.class);
        suite.addTestSuite(TestContentBulkCommand.class);
        suite.addTestSuite(TestContentEntityManager.class);
        suite.addTestSuite(TestContentDOM.class);
        suite.addTestSuite(TestContentListHelper.class);
        suite.addTestSuite(TestContentViewerHelper.class);
        suite.addTestSuite(TestContentAttributeIterator.class);
        suite.addTestSuite(TestContentDAO.class);
        suite.addTestSuite(TestContentManager.class);
        suite.addTest(new JUnit4TestAdapter(ContentManagerTest.class));
        suite.addTest(new JUnit4TestAdapter(AbstractResourceAttributeTest.class));

        suite.addTestSuite(TestPublicContentSearcherDAO.class);
        suite.addTestSuite(TestValidateContent.class);
        //
        suite.addTestSuite(TestContentModelDAO.class);
        suite.addTestSuite(ContentModelManagerIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(ContentModelManagerTest.class));
        //
        suite.addTestSuite(ContentPageMapperManagerIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(ContentMapperCacheWrapperTest.class));
        suite.addTest(new JUnit4TestAdapter(ContentPageMapperManagerTest.class));
        //
        suite.addTestSuite(TestContentDispenser.class);
        //
        suite.addTestSuite(TestLinkResolverManager.class);
        //
        suite.addTestSuite(TestCmsPageManagerWrapper.class);
        //
        suite.addTestSuite(TestResourceDOM.class);
        suite.addTestSuite(TestResourceDAO.class);
        suite.addTestSuite(ResourceManagerIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(ResourceManagerTest.class));
        //
        suite.addTestSuite(TestSearchEngineManager.class);
        suite.addTestSuite(TestApplicationContext.class);

        // Test cross utilizers
        suite.addTestSuite(TestCategoryUtilizer.class);
        suite.addTestSuite(TestGroupUtilizer.class);

        suite.addTestSuite(TestCacheInfoManager.class);
        //
        suite.addTestSuite(TestApiContentInterface.class);

        suite.addTest(ControllersAllTests.suite());

        // -------------------------------------
        suite.addTestSuite(TestTrashReferencedCategory.class);

        // Content
        suite.addTestSuite(TestContentAdminAction.class);
        suite.addTestSuite(TestHypertextAttributeAction.class);
        suite.addTestSuite(TestListAttributeAction.class);
        suite.addTestSuite(TestResourceAttributeAction.class);
        suite.addTestSuite(TestExtendedResourceAction.class);
        suite.addTestSuite(TestExtendedResourceFinderAction.class);
        suite.addTestSuite(TestLinkAttributeAction.class);
        suite.addTestSuite(TestPageLinkAction.class);
        suite.addTestSuite(TestContentLinkAction.class);
        suite.addTestSuite(TestUrlLinkAction.class);
        suite.addTestSuite(TestContentModelAction.class);
        suite.addTestSuite(TestContentModelFinderAction.class);
        suite.addTestSuite(TestContentAction.class);
        suite.addTestSuite(TestSaveBooleanAttributes.class);
        suite.addTestSuite(TestValidateBooleanAttributes.class);
        suite.addTestSuite(TestValidateDateAttribute.class);
        suite.addTestSuite(TestValidateMonotextAttribute.class);
        suite.addTestSuite(TestValidateNumberAttribute.class);
        suite.addTestSuite(TestValidateResourceAttribute.class);
        suite.addTestSuite(TestValidateTextAttribute.class);
        suite.addTestSuite(TestContentFinderAction.class);
        suite.addTestSuite(TestContentGroupAction.class);
        suite.addTestSuite(TestContentInspectionAction.class);
        suite.addTestSuite(TestIntroNewContentAction.class);
        suite.addTestSuite(TestContentGroupBulkAction.class);
        suite.addTestSuite(TestContentCategoryBulkAction.class);

        // Page
        suite.addTestSuite(TestContentListViewerWidgetAction.class);
        suite.addTestSuite(TestBaseFilterAction.class);
        suite.addTestSuite(TestDateAttributeFilterAction.class);
        suite.addTestSuite(TestNumberAttributeFilterAction.class);
        suite.addTestSuite(TestTextAttributeFilterAction.class);
        suite.addTestSuite(TestContentFinderViewerAction.class);
        suite.addTestSuite(TestContentViewerWidgetAction.class);
        suite.addTestSuite(TestPageAction.class);

        //Resource
        suite.addTestSuite(TestMultipleResourceAction.class);
        suite.addTestSuite(TestResourceFinderAction.class);
        suite.addTestSuite(TestPageActionReferences.class);

        //Entity
        suite.addTestSuite(TestJacmsEntityAttributeConfigAction.class);
        suite.addTestSuite(TestJacmsEntityTypeConfigAction.class);
        suite.addTestSuite(TestJacmsEntityManagersAction.class);

        //Group
        suite.addTestSuite(TestTrashReferencedGroup.class);

        //Activity Stream
        suite.addTestSuite(TestActivityStream.class);

        // ----------------------------------------
        suite.addTestSuite(TestContentPreviewAction.class);

        // ----------------------------------------
        suite.addTestSuite(TestJacmsLabelsProperties.class);

        return suite;
    }

}
