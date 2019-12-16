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
package org.entando.entando.plugins.jacms.aps.util;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import org.entando.entando.aps.util.PageUtils;
import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author E.Santoboni
 */
public class CmsPageUtil extends PageUtils {

    private static final Logger logger = LoggerFactory.getLogger(CmsPageUtil.class);

    /**
     * Check if is possible to publish the content in the given page, checking
     * its online configuration.
     *
     * @param publishingContent The publishing content.
     * @param page The page where you want to publish the content.
     * @return True/false if is possible to publish the content in the given
     * page.
     */
    public static boolean isContentPublishableOnPageOnline(Content publishingContent, IPage page) {
        if (!page.isOnlineInstance()) {
            logger.warn("this check expects an online instance of the page");
            return false;
        }
        return isContentPublishableOnPage(publishingContent, page);
    }

    /**
     * Check if is possible to publish the content in the given page, checking
     * its draft configuration.
     *
     * @param publishingContent The publishing content.
     * @param page The page where you want to publish the content.
     * @return True/false if is possible to publish the content in the given
     * page.
     */
    public static boolean isContentPublishableOnPageDraft(Content publishingContent, IPage page) {
        if (null == page) {
            logger.error("Null page");
            return false;
        }
        if (page.isOnlineInstance()) {
            logger.warn("this check expects a draft instance of the page {}", page.getCode());
            return false;
        }
        return isContentPublishableOnPage(publishingContent, page);
    }

    /**
     * Check if is possible to publish the content in the given page, checking
     * the given page metadata.
     *
     * @param publishingContent The publishing content.
     * @param page The page where you want to publish the content.
     * @return True/false if is possible to publish the content in the given
     * page.
     */
    public static boolean isContentPublishableOnPage(Content publishingContent, IPage page) {
        if (publishingContent.getMainGroup().equals(Group.FREE_GROUP_NAME) || publishingContent.getGroups().contains(
                Group.FREE_GROUP_NAME)) {
            return true;
        }
        // tutti i gruppi posseduti dalla pagina devono essere contemplati nel
        // contenuto.
        List<String> pageGroups = new ArrayList<>();
        pageGroups.add(page.getGroup());
        PageMetadata metadata = page.getMetadata();
        if (metadata != null && null != metadata.getExtraGroups()) {
            pageGroups.addAll(metadata.getExtraGroups());
        }
        List<String> contentGroups = getContentGroups(publishingContent);
        for (int i = 0; i < pageGroups.size(); i++) {
            String pageGroup = pageGroups.get(i);
            if (!pageGroup.equals(Group.ADMINS_GROUP_NAME) && !contentGroups.contains(pageGroup)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if is possible to link a page by the given content, checking the
     * page in its online configuration.
     *
     * @param page The page to link.
     * @param content The content where link the given page.
     * @return True/false if is possible to link the page in the given content.
     */
    public static boolean isPageLinkableByContentOnline(IPage page, Content content) {
        if (!page.isOnlineInstance()) {
            logger.warn("this check expects an online instance of the page");
            return false;
        }
        return isPageLinkableByContent(page, page.getMetadata(), content);
    }

    /**
     * Check if is possible to link a page by the given content, checking the
     * page in its draft configuration.
     *
     * @param page The page to link.
     * @param content The content where link the given page.
     * @return True/false if is possible to link the page in the given content.
     */
    public static boolean isPageLinkableByContentDraft(IPage page, Content content) {
        if (page.isOnlineInstance()) {
            logger.warn("this check expects a draft instance of the page");
            return false;
        }
        return isPageLinkableByContent(page, page.getMetadata(), content);
    }

    /**
     * Check if is possible to link a page by the given content, checking the
     * given page metadata.
     *
     * @param page The page to link.
     * @param metadata The metadata of the page you want to link in the given
     * content.
     * @param content The content where link the given page.
     * @return True/false if is possible to link the page in the given content.
     */
    public static boolean isPageLinkableByContent(IPage page, PageMetadata metadata, Content content) {
        Collection<String> extraPageGroups = metadata.getExtraGroups();
        if (page.getGroup().equals(Group.FREE_GROUP_NAME) || (null != extraPageGroups && extraPageGroups.contains(Group.FREE_GROUP_NAME))) {
            return true;
        }
        if (content.getMainGroup().equals(Group.ADMINS_GROUP_NAME)) {
            return true;
        }
        List<String> contentGroups = getContentGroups(content);
        for (String contentGroup : contentGroups) {
            if (contentGroup.equals(page.getGroup())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getContentGroups(Content content) {
        List<String> contentGroups = new ArrayList<>();
        contentGroups.add(content.getMainGroup());
        if (null != content.getGroups()) {
            contentGroups.addAll(content.getGroups());
        }
        return contentGroups;
    }

    public static Collection<Content> getPublishedContents(String pageCode, boolean draftPage, ApplicationContext applicationContext) {
        IPageManager pageManager = applicationContext.getBean(SystemConstants.PAGE_MANAGER, IPageManager.class);
        Collection<Content> contents = new HashSet<>();
        try {
            IPage page = (draftPage) ? pageManager.getDraftPage(pageCode) : pageManager.getOnlinePage(pageCode);
            if (null == page) {
                return contents;
            }
            addPublishedContents(page.getWidgets(), contents, applicationContext);
        } catch (Throwable t) {
            String msg = "Error extracting published contents on page '" + pageCode + "'";
            logger.error(msg, t);
            throw new RuntimeException(msg, t);
        }
        return contents;
    }

    private static void addPublishedContents(Widget[] widgets, Collection<Content> contents, ApplicationContext applicationContext) {
        try {
            if (widgets != null) {
                IContentManager contentManager = applicationContext.getBean(JacmsSystemConstants.CONTENT_MANAGER, IContentManager.class);
                for (Widget widget : widgets) {
                    ApsProperties config = (null != widget) ? widget.getConfig() : null;
                    if (null == config || config.isEmpty()) {
                        continue;
                    }
                    String extracted = config.getProperty("contentId");
                    addContent(contents, extracted, contentManager);
                    String contentsParam = config.getProperty("contents");
                    List<Properties> properties = (null != contentsParam) ? RowContentListHelper.fromParameterToContents(contentsParam) : null;
                    if (null == properties || properties.isEmpty()) {
                        continue;
                    }
                    for (int j = 0; j < properties.size(); j++) {
                        Properties widgProp = properties.get(j);
                        String extracted2 = widgProp.getProperty("contentId");
                        addContent(contents, extracted2, contentManager);
                    }
                }
            }
        } catch (Throwable t) {
            String msg = "Error extracting published contents on page";
            logger.error(msg, t);
            throw new RuntimeException(msg, t);
        }
    }

    private static void addContent(Collection<Content> contents, String contentId, IContentManager contentManager) {
        try {
            if (null != contentId) {
                Content content = contentManager.loadContent(contentId, true);
                if (null != content) {
                    contents.add(content);
                }
            }
        } catch (Throwable t) {
            logger.error("Error extracting published content '{}'", contentId, t);
        }
    }

}
