package org.entando.entando.plugins.oidc.oidc.aps.controller;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ampie Barnard
 *
 **/
public class OidcHelper extends AbstractService {
    @Autowired
    protected IAuthenticationProviderManager authenticationProviderManager;
    @Autowired
    protected IdentityProviderExtractor identityProviderExtractor;
    @Autowired
    protected OidcConfiguration oidcConfiguration;

    private static final Logger logger = LoggerFactory.getLogger(OidcHelper.class);

    private static final String USERNAME_PARAM_NAME = "preferred_username";

    private static final String RESERVED_PASSWORD = "**##@@KEYCLOAK@@##**";

    /**
     * Builds an request url that the browser needs to be redirected to for OAuth.
     * @param req
     * @return
     * @throws OAuthSystemException
     */
    public String buildOauthRequestString(HttpServletRequest req) throws OAuthSystemException {
        OAuthClientRequest.AuthenticationRequestBuilder requestBuilder = OAuthClientRequest
                .authorizationLocation(this.oidcConfiguration.getOidcAuthLocation())
                .setClientId(this.oidcConfiguration.getOidcClientId())
                .setParameter("response_mode", "form_post")
                .setParameter("response_type", "code");
        if (identityProviderExtractor.hasIdentityProvider(req)) {
            requestBuilder = requestBuilder.setParameter("kc_idp_hint", identityProviderExtractor.getIdentityProviderName(req))
                    .setRedirectURI(identityProviderExtractor.getRedirectUri(req));
        } else if (StringUtils.isNotEmpty(this.oidcConfiguration.getDefaultIdentityProvider())) {
            requestBuilder = requestBuilder.setParameter("kc_idp_hint", this.oidcConfiguration.getDefaultIdentityProvider())
                    .setRedirectURI(identityProviderExtractor.getRedirectUri(req));
        } else {
            requestBuilder = requestBuilder.setRedirectURI(buildRedirectURI(req));
        }
        OAuthClientRequest oauthRequest = requestBuilder
                .buildQueryMessage();
        return oauthRequest.getLocationUri();
    }

    /**
     * Attempts to find a user in the Entando database for the username found in the accesstoken. If none is found,
     * it constructs an in-memory UserDetails object and populates its attributes from attribytes found in
     * the token.
     * @param accessToken
     * @return
     * @throws ApsSystemException
     */
    public UserDetails getOidcUser(String accessToken) throws ApsSystemException {
        String[] split_string = accessToken.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64 base64Url = new Base64(true);
        logger.info("---------------------TOKEN-----------------------");
        logger.info(new String(base64Url.decode(split_string[0])));
        logger.info("--------------------------------------------");
        String body = new String(base64Url.decode(base64EncodedBody));
        logger.info(body);
        logger.info("--------------------------------------------");
        JSONObject obj = new JSONObject(body);
        String username = null;
        if (obj.has(USERNAME_PARAM_NAME)) {
            username = obj.getString(USERNAME_PARAM_NAME);
            logger.info("USERNAME -> " + username);
        } else {
            username = "tempUser";
            logger.info("\"" + USERNAME_PARAM_NAME + "\" ATTRIBUTE NOT FOUND");
            logger.info("DEFAULT USERNAME -> " + username);
        }
        if (StringUtils.isEmpty(username)) {
            username = "tempUser";
            logger.info("\"" + USERNAME_PARAM_NAME + "\" ATTRIBUTE EMPTY OR NOT FOUND");
            logger.info("DEFAULT USERNAME -> " + username);
        }
        UserDetails user = authenticationProviderManager.getUser(username);
        if (null == user) {
            logger.info("CREATING UserDetails object for  " + username);
            user = new User();
            ((User) user).setUsername(username);
            ((User) user).setPassword(RESERVED_PASSWORD);
            UserProfile profile = new UserProfile();
            ((User) user).setProfile(profile);
            copyAttributes(obj, profile);
        }else{
            logger.info("WARNING!!! UserDetails object found for  " + username);
        }
        return user;
    }

    private void copyAttributes(JSONObject obj, UserProfile profile) {
        copyAttribute(obj, "given_name", profile, "fname");
        copyAttribute(obj, "family_name", profile, "lname");
        copyAttribute(obj, "email", profile, "email");
    }

    private void copyAttribute(JSONObject obj, String sourceAttributeName, UserProfile profile, String targetAttributeName) {
        String value="";
        if(obj.has(sourceAttributeName)) {
            value = obj.getString(sourceAttributeName);
        }
        MonoTextAttribute attribute = new MonoTextAttribute();
        attribute.setName(targetAttributeName);
        attribute.setText(value);
        profile.addAttribute(attribute);
    }

    @Override
    public void init() throws Exception {

    }

    public boolean isOidcActive() {
        return this.oidcConfiguration.isOidcActive();
    }
    public OAuthClientRequest buildOauthRequest(HttpServletRequest request, String code) throws OAuthSystemException {
        return OAuthClientRequest
                .tokenLocation(this.oidcConfiguration.getOidcTokenLocation())
                //.tokenProvider(OAuthProviderType.MICROSOFT)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(this.oidcConfiguration.getOidcClientId())
                //.setClientSecret("your-facebook-application-client-secret")
                .setRedirectURI(buildRedirectURI(request))
                .setCode(code)
                .setParameter("response_mode", "form_post")
                .buildBodyMessage();

    }
    private String buildRedirectURI(HttpServletRequest req) {
        StringBuffer redirectURI = req.getRequestURL();
        if (req.getQueryString() != null) {
            redirectURI = redirectURI.append("?").append(req.getQueryString());
        }
        this.logger.info("Setting redirectURI to " + redirectURI);
        return redirectURI.toString();
    }
}
