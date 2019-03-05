package org.entando.entando.plugins.oidc.oidc.aps.controller;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.entando.entando.plugins.oidc.oidc.aps.controller.DomainNameIdentityProviderExtractor;
import org.entando.entando.plugins.oidc.oidc.aps.controller.OidcConfiguration;
import org.entando.entando.plugins.oidc.oidc.aps.controller.OidcHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OidcHelperTest {

    private OidcConfiguration cfg;
    @InjectMocks
    OidcHelper oidcUserExtractor;
    @Mock
    IAuthenticationProviderManager authenticationProviderManager;
    OidcConfiguration oidcConfiguration=new OidcConfiguration();
    DomainNameIdentityProviderExtractor identityProviderExtractor=new DomainNameIdentityProviderExtractor();
    @Before
    public void setup(){
        identityProviderExtractor.setOidcConfiguration(oidcConfiguration);
        oidcUserExtractor.identityProviderExtractor=identityProviderExtractor;
        oidcUserExtractor.oidcConfiguration=oidcConfiguration;
    }


    @Test
    public void testHasIdentityProvider() throws ApsSystemException {
        when(authenticationProviderManager.getUser(anyString())).thenReturn(null);
        UserDetails oidcUser = oidcUserExtractor.getOidcUser(SAMPLE_TOKEN);
        assertThat(oidcUser.getUsername(), is(equalTo("entando_test_1")));
        UserProfile userProfile = (UserProfile) oidcUser.getProfile();
        assertThat((String)userProfile.getValue("fname"), is(equalTo("Benjamin")));
        assertThat((String)userProfile.getValue("lname"), is(equalTo("Berg")));
    }
    @Test
    public void testBuildOauthRequestLocationUriWithIdentityProvider() throws Exception{
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://redhat.some.domain.com:9090/somecontext/page?some_param=value"));
        this.oidcConfiguration.setOidcClientId("test.client.id");
        this.oidcConfiguration.setOidcAuthLocation("http://keycloak.some.domain.com:7070/auth/path");
        String clientRequest = oidcUserExtractor.buildOauthRequestString(request);
        assertThat(clientRequest,is(equalTo("http://keycloak.some.domain.com:7070/auth/path?response_type=code&redirect_uri=http%3A%2F%2Fsome.domain.com%3A9090%2Fsomecontext%2Fpage%3Fsome_param%3Dvalue&client_id=test.client.id&response_mode=form_post&kc_idp_hint=redhat")));
    }
    @Test
    public void testBuildOauthRequestLocationUriWithDefaultIdentityProvider() throws Exception{
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://some.domain.com:9090/somecontext/page?some_param=value"));
        this.oidcConfiguration.setOidcClientId("test.client.id");
        this.oidcConfiguration.setOidcAuthLocation("http://keycloak.some.domain.com:7070/auth/path");
        this.oidcConfiguration.setDefaultIdentityProvider("entando");
        String clientRequest = oidcUserExtractor.buildOauthRequestString(request);
        assertThat(clientRequest,is(equalTo("http://keycloak.some.domain.com:7070/auth/path?response_type=code&redirect_uri=http%3A%2F%2Fsome.domain.com%3A9090%2Fsomecontext%2Fpage%3Fsome_param%3Dvalue&client_id=test.client.id&response_mode=form_post&kc_idp_hint=entando")));
    }    @Test
    public void testBuildOauthRequestLocationUriWithNoIdentityProvider() throws Exception{
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://some.domain.com:9090/somecontext/page?some_param=value"));
        this.oidcConfiguration.setOidcClientId("test.client.id");
        this.oidcConfiguration.setOidcAuthLocation("http://keycloak.some.domain.com:7070/auth/path");
        String clientRequest = oidcUserExtractor.buildOauthRequestString(request);
        assertThat(clientRequest,is(equalTo("http://keycloak.some.domain.com:7070/auth/path?response_type=code&redirect_uri=http%3A%2F%2Fsome.domain.com%3A9090%2Fsomecontext%2Fpage%3Fsome_param%3Dvalue&client_id=test.client.id&response_mode=form_post")));
    }
    @Test
    public void testBuildOauthTokenRequest() throws Exception{
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://some.domain.com:9090/somecontext/page?some_param=value"));
        this.oidcConfiguration.setOidcClientId("test.client.id");
        this.oidcConfiguration.setOidcTokenLocation("http://keycloak.some.domain.com:7070/token/path");
        this.oidcConfiguration.setOidcAuthLocation("http://keycloak.some.domain.com:7070/auth/path");
        OAuthClientRequest clientRequest = oidcUserExtractor.buildOauthRequest(request,"some_code");
        assertThat(clientRequest.getLocationUri(),is(equalTo("http://keycloak.some.domain.com:7070/token/path")));
        assertThat(clientRequest.getBody(),is(equalTo("code=some_code&grant_type=authorization_code&redirect_uri=http%3A%2F%2Fsome.domain.com%3A9090%2Fsomecontext%2Fpage%3Fsome_param%3Dvalue&client_id=test.client.id&response_mode=form_post")));
    }
    private static String SAMPLE_TOKEN="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJGaE1Hc2haWFhIem82NG5BeC1ycEl" +
            "6M3VOZXY4YnJVa0xiaXdEU2RuLXZrIn0.eyJqdGkiOiJmMDg4YzRiNi00OGExLTRhNTAtOTY3Yy02N2E4NmE0NDIxZWMiLCJleHAiOjE1N" +
            "DkzNTI5OTEsIm5iZiI6MCwiaWF0IjoxNTQ5MzUyNjkxLCJpc3MiOiJodHRwczovL3N0YWdpbmcuYWNjZXNzLmVudGFuZG8uY29tL2F1dGg" +
            "vcmVhbG1zL2VudGFuZG8iLCJhdWQiOiJjZW50cmFsLmVudGFuZG8iLCJzdWIiOiJiYmM3YmE5Ny00MDY3LTQ0MTktODg2YS1hYTg1ZTI0Y" +
            "zM1MTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjZW50cmFsLmVudGFuZG8iLCJhdXRoX3RpbWUiOjE1NDkzNTI2OTAsInNlc3Npb25fc3R" +
            "hdGUiOiI5NWMyOWRkNy05YjRlLTQwNjEtOTVkMC1hZTRiZjIyZDFmZWIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzO" +
            "i8vc3RhZ2luZy5jZW50cmFsLmVudGFuZG8uY29tIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1bWFfYXV0aG9yaXphdGlvbiJdfSw" +
            "icmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsI" +
            "nZpZXctcHJvZmlsZSJdfX0sImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkJlbmphbWluIEJlcmciLCJwcmVmZXJyZWRfdXNlcm5" +
            "hbWUiOiJlbnRhbmRvX3Rlc3RfMSIsImdpdmVuX25hbWUiOiJCZW5qYW1pbiIsImZhbWlseV9uYW1lIjoiQmVyZyIsImVtYWlsIjoiYmViZ" +
            "XJnQHJlZGhhdC5jb20ifQ.MgTbaI0PzZ7O31AvOQMcjniRd4CWDoOZgQBfL3GGFR8Eeb7SXGcdz-kG12oLh2mLyBPxLpFFVq6_IqbuGK1z" +
            "nnS8zs5Q-DnUoO1g4bkBYqThCJ99nnmdATbEB265SBFbQGM2CLWYJowwOWvW38XomsoIY8UyqjvmKMYnhwbSTAvwefaR_iihZhw1CJBPRv" +
            "yerTMkbiQb6-Lqt5RcOX1NlA4L0AOWwyOsuiZPsHH_Q8zF_Rs0TMEIbaJ0KB_fB3Y1usNR1HOOrwMRTOTlmuN699ao3kxCOWC54avYMpoV" +
            "VIwur7oFfCtjU5yTQOvt7AVrqsHmLot_Gv7ASwzsKLQmuQ";
}
