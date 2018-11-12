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
package com.agiletec.plugins.jacms.apsadmin.content;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.TreeNodeWrapper;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;
import java.util.Arrays;
import java.util.List;

public class ContentCategoryActionIntegrationTest extends AbstractBaseTestContentAction {

    private String contentOnSessionMarker;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    /**
     * Open a new content for editing.
     */
    private void init() throws Exception {
        try {
            this.setUserOnSession("admin");

            String contentTypeCode = "ART";

            this.initAction("/do/jacms/Content", "createNew");
            this.addParameter("contentTypeCode", "ART");
            assertEquals(Action.SUCCESS, this.executeAction());
            contentOnSessionMarker = this.extractSessionMarker(contentTypeCode, ApsAdminSystemConstants.ADD);
            assertNotNull(getContentOnEdit(contentOnSessionMarker));
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    @Override
    protected ContentCategoryAction getAction() {
        return (ContentCategoryAction) super.getAction();
    }

    /**
     * This test has been deliberately created to avoid regression, indeed in
     * the past join a category also caused tree node expansion.
     */
    public void testTreeStateOnJoinCategory() throws Throwable {

        String categoryCode = "general";

        // Join the category
        this.initAction("/do/jacms/Content", "joinCategory");
        this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
        this.addParameter("treeNodesToOpen", "home");
        this.addParameter("categoryCode", categoryCode);
        assertEquals(Action.SUCCESS, this.executeAction());
        checkNodeOpen("home");
        checkNodeClose("home", categoryCode);
        List<Category> categories = getAction().getContent().getCategories();
        assertEquals(1, categories.size());
        assertEquals(categoryCode, categories.get(0).getCode());

        // Check tree state
        this.initAction("/do/jacms/Content", "showCategoryTreeOnContentFinding");
        this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
        this.addParameter("treeNodesToOpen", "home");
        this.addParameter("categoryCode", categoryCode);
        assertEquals(Action.SUCCESS, this.executeAction());
        // if the list contains 2 elements the joined category node has been expanded
        assertEquals(1, getAction().getTreeNodesToOpen().size());
        assertTrue(getAction().getTreeNodesToOpen().contains("home"));
    }

    public void testRemoveCategory() throws Throwable {
        String categoryCode = "cat1";

        // Join the category
        this.initAction("/do/jacms/Content", "joinCategory");
        this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
        this.addParameter("categoryCode", categoryCode);
        assertEquals(Action.SUCCESS, this.executeAction());
        List<Category> categories = getAction().getContent().getCategories();
        assertEquals(1, categories.size());
        assertEquals(categoryCode, categories.get(0).getCode());
        
        // Remove the category
        this.initAction("/do/jacms/Content", "removeCategory");
        this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
        this.addParameter("categoryCode", categoryCode);
        assertEquals(Action.SUCCESS, this.executeAction());
        assertTrue(getAction().getContent().getCategories().isEmpty());
    }

    public void testNodeTreeOpen() throws Throwable {
        this.initAction("/do/jacms/Content", "showCategoryTreeOnContentFinding");
        this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
        this.addParameter("treeNodeActionMarkerCode", "open");
        this.addParameter("treeNodesToOpen", "home");
        this.addParameter("targetNode", "general");
        this.executeAction();
        assertEquals(2, getAction().getTreeNodesToOpen().size());
        assertTrue(getAction().getTreeNodesToOpen().containsAll(Arrays.asList("home", "general")));
        checkNodeOpen("home");
        checkNodeOpen("home", "general");
        checkNodeClose("home", "general", "general_cat1");
    }

    public void testNodeTreeClose() throws Throwable {
        this.initAction("/do/jacms/Content", "showCategoryTreeOnContentFinding");
        this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
        this.addParameter("treeNodeActionMarkerCode", "close");
        this.addParameter("treeNodesToOpen", "home");
        this.addParameter("targetNode", "home");
        this.executeAction();
        assertTrue(getAction().getTreeNodesToOpen().isEmpty());
        checkNodeClose("home");
    }

    private void checkNodeOpen(String... path) {
        checkNodeStatus(true, path);
    }

    private void checkNodeClose(String... path) {
        checkNodeStatus(false, path);
    }

    private void checkNodeStatus(boolean open, String... path) {
        ITreeNode nodes[] = new ITreeNode[]{(TreeNodeWrapper) getAction().getShowableTree()};
        for (int i = 0; i < path.length; i++) {
            String nodeCode = path[i];
            TreeNodeWrapper nodeFound = null;
            for (ITreeNode node : nodes) {
                if (nodeCode.equals(node.getCode())) {
                    nodeFound = (TreeNodeWrapper) node;
                    nodes = nodeFound.getChildren();
                    break;
                }
            }
            assertNotNull("Node " + nodeCode + " not found", nodeFound);
            if (i < path.length - 1) {
                assertTrue(nodeCode, nodeFound.isOpen());
            } else {
                assertEquals(nodeCode, open, nodeFound.isOpen());
            }
        }
    }
}
