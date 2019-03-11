package org.entando.entando.plugins.oidc.oidc.aps.controller;

import java.util.Map;

public class OidcConfiguration {
    private boolean oidcActive;
    private String oidcAuthLocation;
    private String oidcTokenLocation;
    private String oidcClientId;
    private String oidcRedirectBaseUrl;
    private String defaultIdentityProvider;
    /*
entando.oidc.location=https://accesso.cdptest.it/adfs/oauth2/authorize
entando.oidc.clientId=fe2bccdd-0c5f-4856-b6d0-59aef738d35d
entando.oidc.redirectBaseUrl=http://cdprh96:8230/portalexample/
entando.oidc.resource=http://cdprh96:8230/portalexample/
     */

    public boolean isOidcActive() {
        return  "true".equals(System.getenv("ENTANDO_OIDC_ACTIVE")) || oidcActive;
    }

    public void setOidcActive(boolean oidcActive) {
        this.oidcActive = oidcActive;
    }

    public String getOidcAuthLocation() {
        return oidcAuthLocation;
    }


    public void setOidcAuthLocation(String oidcAuthLocation) {
        this.oidcAuthLocation = oidcAuthLocation;
    }

    public String getOidcTokenLocation() {
        return oidcTokenLocation;
    }

    public void setOidcTokenLocation(String oidcTokenLocation) {
        this.oidcTokenLocation = oidcTokenLocation;
    }

    public String getOidcClientId() {
        return oidcClientId;
    }

    public void setOidcClientId(String oidcClientId) {
        this.oidcClientId = oidcClientId;
    }

    public String getOidcRedirectBaseUrl() {
        return oidcRedirectBaseUrl;
    }

    public void setOidcRedirectBaseUrl(String oidcRedirectBaseUrl) {
        this.oidcRedirectBaseUrl = oidcRedirectBaseUrl;
    }

    public String getDefaultIdentityProvider() {
        return defaultIdentityProvider;
    }

    public void setDefaultIdentityProvider(String defaultIdentityProvider) {
        this.defaultIdentityProvider = defaultIdentityProvider;
    }
}
