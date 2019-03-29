package org.entando.entando.plugins.oidc.oidc.aps.controller;

import org.entando.entando.plugins.oidc.oidc.aps.controller.DomainNameIdentityProviderExtractor;
import org.entando.entando.plugins.oidc.oidc.aps.controller.OidcConfiguration;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class DomainNameIdentityProviderExtractorTest {

    private OidcConfiguration cfg;

    @Test
    public void testHasIdentityProvider(){
        //Given
        DomainNameIdentityProviderExtractor extractor = new DomainNameIdentityProviderExtractor();
        //Expect
        assertTrue(extractor.hasIdentityProvider(requestWithUrl("https://yada.yada.redhat.central.entando.com/some/page")));
        assertFalse(extractor.hasIdentityProvider(requestWithUrl("https://www.central.entando.com/some/page")));
        assertFalse(extractor.hasIdentityProvider(requestWithUrl("https://central.entando.com/some/page")));
        assertTrue(extractor.hasIdentityProvider(requestWithUrl("https://redhat.central.entando.com/some/page")));
    }
    @Test
    public void testGetIdentityProvider(){
        //Given
        DomainNameIdentityProviderExtractor extractor = new DomainNameIdentityProviderExtractor();
        //Expect
        assertEquals("redhat", extractor.getIdentityProviderName(requestWithUrl("https://yada.yada.redhat.central.entando.com/some/page")));
        assertEquals("redhat", extractor.getIdentityProviderName(requestWithUrl("https://redhat.central.entando.com/some/page")));
        assertEquals("redhat", extractor.getIdentityProviderName(requestWithUrl("https://staging-redhat.central.entando.com/some/page")));
    }

    @Test
    public void testGetRedirectUrl(){
        //Given
        DomainNameIdentityProviderExtractor extractor = new DomainNameIdentityProviderExtractor();
        extractor.setOidcConfiguration(new OidcConfiguration());
        //Expect
        assertEquals("https://yada.yada.central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://yada.yada.redhat.central.entando.com/some/page")));
        assertEquals("https://central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://redhat.central.entando.com/some/page")));
        assertEquals("https://central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://redhat-central.entando.com/some/page")));
        assertEquals("https://central-entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://redhat.central-entando.com/some/page")));
        assertEquals("https://central-entando.com/some/page?param=value", extractor.getRedirectUri(requestWithUrl("https://redhat.central-entando.com/some/page?param=value")));
        assertEquals("https://staging-central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://staging-redhat.central.entando.com/some/page")));
    }
    @Test
    public void testGetRedirectUrlFromConfig(){
        //Given
        DomainNameIdentityProviderExtractor extractor = new DomainNameIdentityProviderExtractor();
        final Map<String,String> env = new HashMap<>();
        cfg = new OidcConfiguration();
        cfg.setOidcRedirectBaseUrl("https://central.entando.com");
        extractor.setOidcConfiguration(cfg);
        //Expect
        assertEquals("https://central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://yada.yada.redhat.central.entando.com/some/page")));
        assertEquals("https://central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://redhat.central.entando.com/some/page")));
        assertEquals("https://central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://redhat-central.entando.com/some/page")));
        assertEquals("https://central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://redhat.central-entando.com/some/page")));
        assertEquals("https://central.entando.com/some/page", extractor.getRedirectUri(requestWithUrl("https://staging-redhat.central.entando.com/some/page")));
    }

    private HttpServletRequest requestWithUrl(final String url)  {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        when(request.getRequestURL()).thenReturn(new StringBuffer(url));

        return new HttpServletRequestWrapper(request);
    }

}
