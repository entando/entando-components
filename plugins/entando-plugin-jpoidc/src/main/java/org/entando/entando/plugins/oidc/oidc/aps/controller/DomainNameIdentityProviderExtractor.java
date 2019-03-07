package org.entando.entando.plugins.oidc.oidc.aps.controller;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DomainNameIdentityProviderExtractor implements IdentityProviderExtractor {
    public static final int DEFAULT_NUMBER_OF_SIGNIFICANT_DOMAIN_LEVELS = 3;
    private int significantDomainLevels = DEFAULT_NUMBER_OF_SIGNIFICANT_DOMAIN_LEVELS;
    private Set<String> ignoredSegments = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("www", "staging")));
    protected OidcConfiguration oidcConfiguration;
    @Override
    public boolean hasIdentityProvider(HttpServletRequest request) {
        String[] domainSegments = getDomainSegments(request);
        if (domainSegments.length <= significantDomainLevels) {
            return false;
        } else {
            return !ignoredSegments.contains(getIdentityProviderSegment(domainSegments));
        }
    }

    private String[] getDomainSegments(HttpServletRequest request) {
        try {
            String host = new URL(request.getRequestURL().toString()).getHost();
            return host.split("[\\.\\-_]");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String getIdentityProviderName(HttpServletRequest request) {
        return getIdentityProviderSegment(getDomainSegments(request));
    }

    private String getIdentityProviderSegment(String[] domainSegments) {
        return domainSegments[domainSegments.length - significantDomainLevels - 1];
    }

    @Override
    public String getRedirectUri(HttpServletRequest request) {
        try {
            if(this.getOidcConfiguration().getOidcRedirectBaseUrl()==null) {
                StringBuffer redirectURI = request.getRequestURL();
                if (request.getQueryString() != null) {
                    redirectURI = redirectURI.append("?").append(request.getQueryString());
                }
                URL url = new URL(redirectURI.toString());
                if(hasIdentityProvider(request)) {
                    return url.toString().replace(getIdentityProviderName(request) + ".", "").replace(getIdentityProviderName(request) + "-", "");
                }else{
                    return url.toString();
                }
            }else{
                StringBuffer redirectURI = request.getRequestURL();
                if (request.getQueryString() != null) {
                    redirectURI = redirectURI.append("?").append(request.getQueryString());
                }
                URL url = new URL(redirectURI.toString());
                return url.toString().replace(url.getProtocol() + "://" + url.getAuthority(),this.getOidcConfiguration().getOidcRedirectBaseUrl());
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public OidcConfiguration getOidcConfiguration() {
        return oidcConfiguration;
    }

    public void setOidcConfiguration(OidcConfiguration oidcConfiguration) {
        this.oidcConfiguration = oidcConfiguration;
    }
}
