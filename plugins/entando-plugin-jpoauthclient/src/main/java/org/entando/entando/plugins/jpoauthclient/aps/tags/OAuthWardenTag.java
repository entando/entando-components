/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpoauthclient.aps.tags;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.plugins.jpoauthclient.aps.system.ConsumerSystemConstants;
import org.entando.entando.plugins.jpoauthclient.aps.system.CookieMap;
import org.entando.entando.plugins.jpoauthclient.aps.system.RedirectException;
import org.entando.entando.plugins.jpoauthclient.aps.system.services.client.ApiMethodRequestBean;
import org.entando.entando.plugins.jpoauthclient.aps.system.services.client.IProviderConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author E.Santoboni
 */
public class OAuthWardenTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(OAuthWardenTag.class);

	@Override
	public int doEndTag() throws JspException {
		RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
		IProviderConnectionManager providerManager =
				(IProviderConnectionManager) ApsWebApplicationUtils.getBean(ConsumerSystemConstants.API_PROVIDER_CONNECTION_MANAGER, pageContext);
		HttpServletRequest req = reqCtx.getRequest();
		HttpServletResponse resp = reqCtx.getResponse();
		try {
			OAuthCookieStatus cookieStatus = this.checkOAuthCookie(req);
            if (!cookieStatus.equals(OAuthCookieStatus.VALID)) {
                CookieMap cookies = new CookieMap(req, resp);
                providerManager.removeAccessors(cookies);
            }
			this.invokeWardenApi(reqCtx, req, resp);
		} catch (RedirectException re) {
			try {
				reqCtx.getResponse().sendRedirect(re.getTargetURL());
			} catch (IOException ex) {
				_logger.error("error in doEndTag", ex);
			}
            return SKIP_PAGE;
        //} catch (OAuthProblemException oae) {
        //    return ControllerManager.REDIRECT;
        } catch (Throwable t) {
        	_logger.error("Error, could not fulfill the request", t);
        }
		return super.doEndTag();
	}

    private boolean invokeWardenApi(RequestContext reqCtx, HttpServletRequest req, HttpServletResponse resp) throws RedirectException, Exception {
        boolean succesful = false;
        IProviderConnectionManager providerManager =
				(IProviderConnectionManager) ApsWebApplicationUtils.getBean(ConsumerSystemConstants.API_PROVIDER_CONNECTION_MANAGER, pageContext);
		IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, pageContext);
		try {
            ApiMethodRequestBean bean = new ApiMethodRequestBean(this.getConsumerName(),
					ApiMethod.HttpMethod.GET, "core", "myUserProfile", null);
			IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			String url = urlManager.createUrl(currentPage, currentLang, null);
			bean.setRedirectUrl(url);
			bean.setLangCode("en");
			Object apiResponse = providerManager.invokeApiMethod(bean, req, resp, true);
			if (null != apiResponse) {
				_logger.debug("Response not null");
			}
        } catch (RedirectException e) {
			//nothing to catch
            throw e;
        } catch (Exception t) {
			//nothing to catch
            throw t;
        }
        return succesful;
    }

    private OAuthCookieStatus checkOAuthCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (null == cookies) {
			return OAuthCookieStatus.MISSING;
		}
        String accessToken = null;
        String tokenSecret = null;
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (null == cookie) continue;
            if (cookie.getName().equalsIgnoreCase(this.getConsumerName() + ".tokenSecret")) {
                tokenSecret = cookie.getValue();
            }
            if (cookie.getName().equalsIgnoreCase(this.getConsumerName() + ".accessToken")) {
                accessToken = cookie.getValue();
            }
        }
        if (null == accessToken && null == tokenSecret) {
			return OAuthCookieStatus.MISSING;
		}
        if (null != accessToken && accessToken.trim().length() > 0 &&
                null != tokenSecret && tokenSecret.trim().length() > 0) {
            return OAuthCookieStatus.VALID;
        } else {
            return OAuthCookieStatus.INVALID;
        }
    }

	private String getConsumerName() {
		return ConsumerSystemConstants.DEFAULT_CONSUMER_NAME;
	}

	private enum OAuthCookieStatus{MISSING, VALID, INVALID}

}