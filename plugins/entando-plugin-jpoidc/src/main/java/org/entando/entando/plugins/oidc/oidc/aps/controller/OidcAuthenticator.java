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
package org.entando.entando.plugins.oidc.oidc.aps.controller;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.Authenticator;
import com.agiletec.aps.system.services.user.UserDetails;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;

/**
 * OIDC variant of the Entando Authenticator controller
 *
 * @author A Barnard
 * !!NB!! This class should ideally be integration tested, but given the effort involved (settting up Keycloak, etc) and
 * given the fact that we are likely to replace it entirely with Spring's internal classes, classes that are already
 * thoroughly tested, I took a rain check on the integration test.
 */
public class OidcAuthenticator extends Authenticator {

    private static final Logger _logger = LoggerFactory.getLogger(OidcAuthenticator.class);
    @Autowired
    protected OidcHelper oidcHelper;

    @Override
    public void afterPropertiesSet() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }

    @Override
    public int service(RequestContext reqCtx, int status) {
        _logger.debug("Invoked {}", this.getClass().getName());
        if (status == ControllerManager.ERROR) {
            return status;
        }
        try {
            HttpServletRequest req = reqCtx.getRequest();
            //Login using internal database
            if (req.getParameter(SystemConstants.LOGIN_USERNAME_PARAM_NAME) != null && req.getParameter(SystemConstants.LOGIN_PASSWORD_PARAM_NAME) != null) {
                return super.service(reqCtx, status);
            } else if (req.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) == null) {
                if (this.oidcHelper.isOidcActive()) {
                    //Login using OIDC
                    if (null == req.getParameter("code")) {
                        //no user, no authorization code, start the authentication flow here
                        String result = this.oidcHelper.buildOauthRequestString(req);
                        _logger.info("Redirecting to OAuth2 provider -> " + result);
                        return super.redirectUrl(URLDecoder.decode(result, "ISO-8859-1"), reqCtx);
                    } else {
                        // exchange authorization code for token
                        fetchAndProcessToken(req, req.getParameter("code"));
                    }
                } else {
                    //Set guest user
                    UserDetails guestUser = this.getUserManager().getGuestUser();
                    req.getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, guestUser);
                }
            }
            return ControllerManager.CONTINUE;
        } catch (Throwable t) {
            _logger.error("Error, could not fulfill the request", t);
            reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ControllerManager.SYS_ERROR;
        }
    }

    private void fetchAndProcessToken(HttpServletRequest req, String code) throws
            OAuthSystemException, OAuthProblemException, ApsSystemException {
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest oAuthClientRequest = this.oidcHelper.buildOauthRequest(req, code);
        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.resource(oAuthClientRequest, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);

        _logger.info("----------------------TOKEN------------------- ");
        String accessToken = oAuthResponse.getAccessToken();
        _logger.info("accessToken -> " + accessToken);
        UserDetails cdpUser = this.oidcHelper.getOidcUser(oAuthResponse.getAccessToken());
        HttpSession session = req.getSession();
        session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, cdpUser);
    }

}
