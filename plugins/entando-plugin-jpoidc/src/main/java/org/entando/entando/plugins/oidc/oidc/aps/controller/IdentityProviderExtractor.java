package org.entando.entando.plugins.oidc.oidc.aps.controller;

import javax.servlet.http.HttpServletRequest;

public interface IdentityProviderExtractor {
    boolean hasIdentityProvider(HttpServletRequest requestUrl);
    String getIdentityProviderName(HttpServletRequest requestUrl);
    String getRedirectUri(HttpServletRequest requestUrl);
}
