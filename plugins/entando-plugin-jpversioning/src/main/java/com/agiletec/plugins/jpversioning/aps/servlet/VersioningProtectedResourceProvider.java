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
package com.agiletec.plugins.jpversioning.aps.servlet;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.ITrashedResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TrashedResourceManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.servlet.IProtectedResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersioningProtectedResourceProvider implements IProtectedResourceProvider {

    private static final Logger logger = LoggerFactory.getLogger(VersioningProtectedResourceProvider.class);

    private IUserManager userManager;
    private IAuthorizationManager authorizationManager;
    private ITrashedResourceManager trashedResourceManager;

    @Override
    public boolean provideProtectedResource(HttpServletRequest request, HttpServletResponse response) throws ApsSystemException {
        try {
            String[] uriSegments = request.getRequestURI().split("/");
            int segments = uriSegments.length;
            if (segments < 3) {
                return false;
            }
            String protectedStr = uriSegments[segments - 4];
            String trashedStr = uriSegments[segments - 3];
            String resId = uriSegments[segments - 2];
            String sizeStr = uriSegments[segments - 1];

            String[] matchUri = JpversioningSystemConstants.PROTECTED_TRASH_FOLDER.split("/");

            if (!matchUri[0].equalsIgnoreCase(protectedStr) || !matchUri[1].equalsIgnoreCase(trashedStr) || !StringUtils
                    .isNumeric(resId) || !StringUtils.isNumeric(sizeStr)) {
                return false;
            }

            Integer size = Integer.parseInt(sizeStr);

            UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
            if (currentUser == null) {
                currentUser = this.getUserManager().getGuestUser();
            }

            ResourceInterface resource = this.getTrashedResourceManager().loadTrashedResource(resId);
            if (resource == null) {
                return false;
            }

            IAuthorizationManager authManager = this.getAuthorizationManager();
            if (authManager.isAuthOnGroup(currentUser, resource.getMainGroup())
                    || authManager.isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME)) {
                ResourceInstance instance = null;
                if (resource.isMultiInstance()) {
                    instance = ((AbstractMultiInstanceResource) resource).getInstance(size, null);
                } else {
                    instance = ((AbstractMonoInstanceResource) resource).getInstance();
                }
                this.createResponse(response, resource, instance, size, currentUser);
                return true;
            }
        } catch (Throwable t) {
            logger.error("Error extracting protected resource", t);
            throw new ApsSystemException("Error extracting protected resource", t);
        }
        return false;
    }

    protected void createResponse(HttpServletResponse resp, ResourceInterface resource,
            ResourceInstance instance, Integer size, UserDetails user) throws IOException, ServletException {

        resp.setContentType(instance.getMimeType());
        resp.setHeader("Content-Disposition", "inline; filename=" + instance.getFileName());
        ServletOutputStream out = resp.getOutputStream();
        try {
            InputStream is = getInputStream(size, user, resource);
            if (null != is) {
                byte[] buffer = new byte[2048];
                int length = -1;
                while ((length = is.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                    out.flush();
                }
                is.close();
            }
        } catch (Throwable t) {
            logger.error("Error extracting protected resource", t);
            throw new ServletException("Error extracting protected resource", t);
        } finally {
            out.close();
        }
    }

    private InputStream getInputStream(Integer size, UserDetails user, ResourceInterface resource)
            throws ApsSystemException {
        String mainGroup = resource.getMainGroup();
        if (authorizationManager.isAuthOnGroup(user, mainGroup)) {
            return trashedResourceManager.getTrashFileStream(resource, getResourceInstance(size, resource));
        }
        return null;
    }

    private ResourceInstance getResourceInstance(Integer size, ResourceInterface resource) {
        ResourceInstance instance = null;
        if (resource.isMultiInstance()) {
            instance = ((AbstractMultiInstanceResource) resource).getInstance(size, null);
        } else {
            instance = ((AbstractMonoInstanceResource) resource).getInstance();
        }
        return instance;
    }

    public ITrashedResourceManager getTrashedResourceManager() {
        return trashedResourceManager;
    }

    public void setTrashedResourceManager(
            ITrashedResourceManager trashedResourceManager) {
        this.trashedResourceManager = trashedResourceManager;
    }

    protected IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    protected IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

}
