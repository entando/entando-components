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
package org.entando.entando.plugins.jpoauthclient.aps.system.services.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthServiceProvider;
import net.oauth.ParameterStyle;
import net.oauth.server.HttpRequestMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.provider.json.JSONProvider;
import org.entando.entando.aps.system.services.api.server.ApiRestStatusServer;
import org.entando.entando.plugins.jpoauthclient.aps.oauth.client.OAuthClient;
import org.entando.entando.plugins.jpoauthclient.aps.servlet.Callback;
import org.entando.entando.plugins.jpoauthclient.aps.system.ConsumerSystemConstants;
import org.entando.entando.plugins.jpoauthclient.aps.system.CookieMap;
import org.entando.entando.plugins.jpoauthclient.aps.system.OAuthClientException;
import org.entando.entando.plugins.jpoauthclient.aps.system.RedirectException;
import org.entando.entando.plugins.jpoauthclient.aps.system.httpclient.OAuthHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import org.apache.commons.io.IOUtils;

/**
 * @author E.Santoboni
 */
public class ProviderConnectionManager 
        extends AbstractService implements IProviderConnectionManager, ServletContextAware {

	private static final Logger _logger =  LoggerFactory.getLogger(ProviderConnectionManager.class);

	@Override
    public void init() throws Exception {
        this.loadDefaultConsumer();
        _logger.debug("{} ready", this.getClass().getName());
    }
	
    @Override
    protected void release() {
		this._consumers.clear();
        super.release();
    }
	
	protected void loadDefaultConsumer() throws Exception {
		InputStream is = null;
		try {
			is = this.getServletContext().getResourceAsStream("/WEB-INF/plugins/jpoauthclient/oauth_consumer.properties");
			Properties properties = new Properties();
            properties.load(is);
			String base = this.getConfigManager().getParam(ConsumerSystemConstants.ENTANDO_SERVICE_PROVIDER_BASE_URL_PARAM_NAME);
			URL baseURL = (base == null) ? null : new URL(base);
			if (null == baseURL) return;
			String requestTokenURL = this.getURL(baseURL, properties, ConsumerSystemConstants.ENTANDO_SERVICE_PROVIDER_REQ_TOKEN_URL_PARAM_NAME);
			String userAuthorizationURL = this.getURL(baseURL, properties, ConsumerSystemConstants.ENTANDO_SERVICE_PROVIDER_AUTH_URL_PARAM_NAME);
			String accessTokenURL = this.getURL(baseURL, properties, ConsumerSystemConstants.ENTANDO_SERVICE_PROVIDER_ACCESS_TOKEN_URL_PARAM_NAME);
			if (null == requestTokenURL || null == userAuthorizationURL || null == accessTokenURL) return;
			OAuthServiceProvider serviceProvider = new OAuthServiceProvider(requestTokenURL, userAuthorizationURL, accessTokenURL);
			OAuthConsumer consumer = new OAuthConsumer(this.getConfigManager().getParam(ConsumerSystemConstants.CONSUMER_CALLBACK_URL_PARAM_NAME), 
					this.getConfigManager().getParam(ConsumerSystemConstants.CONSUMER_KEY_PARAM_NAME), 
					this.getConfigManager().getParam(ConsumerSystemConstants.CONSUMER_SECRET_PARAM_NAME), serviceProvider);
			consumer.setProperty("name", ConsumerSystemConstants.DEFAULT_CONSUMER_NAME);
			if (baseURL != null) {
				consumer.setProperty("serviceProvider.baseURL", baseURL);
			}
			this._consumers.put(ConsumerSystemConstants.DEFAULT_CONSUMER_NAME, consumer);
		} catch (Throwable t) {
			_logger.error("Error extracting default consumer", t);
		} finally {
			if (null != is) {
				is.close();
			}
		}
    }
	
    private String getURL(URL baseUrl, Properties properties, String pathParamName) throws MalformedURLException {
        String path = properties.getProperty(pathParamName);
		if (null == baseUrl || null == path) return null;
        return (new URL(baseUrl, path)).toExternalForm();
    }
	
	@Override
	public InputStream invokeApiMethod(ApiMethodRequestBean bean, HttpServletRequest request, 
			HttpServletResponse response, boolean handleException) throws RedirectException, Exception {
		return this.invokeApiMethod(bean, request, response, handleException, true);
	}
	
	@Override
	public InputStream invokeApiMethod(ApiMethodRequestBean bean, HttpServletRequest request, 
			HttpServletResponse response, boolean handleException, boolean redirectOAuthErrorPage) throws RedirectException, Exception {
		InputStream stream = null;
        try {
			this.checkConsumer(bean);
            Boolean free = this.checkMethod(bean);
			if (null == free) return null;
			ApiMethod.HttpMethod method = bean.getHttpMethod();
            if (free) {
				if (method.equals(ApiMethod.HttpMethod.GET) || method.equals(ApiMethod.HttpMethod.DELETE)) {
					stream = this.invokeFreeGetDeleteApi(bean, handleException);
				} else {
					stream = this.invokeFreePostPutApi(bean, handleException);
				}
            } else {
				if (method.equals(ApiMethod.HttpMethod.GET) || method.equals(ApiMethod.HttpMethod.DELETE)) {
					stream = this.invokeProtectedGetDeleteApi(bean, request, response, handleException, redirectOAuthErrorPage);
				} else {
					stream = this.invokeProtectedPostPutApi(bean, request, response, handleException, redirectOAuthErrorPage);
				}
            }
        } catch (OAuthClientException ume) {
            if (handleException) {
                throw ume;
            }
        } catch (RedirectException re) {
            if (handleException) {
                throw re;
            }
        } catch (Exception t) {
            if (handleException) {
            	_logger.error("Error invoking api method", t);
                throw t;
            }
        }
        return stream;
	}
	
	@Override
	public Object extractApiMethodResult(ApiMethodRequestBean bean, Class expectedType, 
            HttpServletRequest request, HttpServletResponse response, boolean handleException) throws RedirectException, Exception {
		return this.extractApiMethodResult(bean, expectedType, request, response, handleException, true);
	}
	
	@Override
	public Object extractApiMethodResult(ApiMethodRequestBean bean, Class expectedType, 
            HttpServletRequest request, HttpServletResponse response, boolean handleException, boolean redirectOAuthErrorPage) throws RedirectException, Exception {
		Object bodyObject = null;
        try {
            InputStream stream = this.invokeApiMethod(bean, request, response, handleException, redirectOAuthErrorPage);
			if (null == stream) return null;
            bodyObject = this.unmarshallResponse(stream, expectedType, bean.getMediaType());
        } catch (OAuthClientException ume) {
            if (handleException) {
                throw ume;
            }
        } catch (RedirectException re) {
            if (handleException) {
                throw re;
            }
        } catch (Exception t) {
            if (handleException) {
            	_logger.error("Error extracting result", t);
                throw t;
            }
        }
        return bodyObject;
	}
	
	private void checkConsumer(ApiMethodRequestBean bean) throws OAuthClientException, ApsSystemException {
		try {
			if (null != bean.getConsumerName()) {
				if (null == this.getConsumer(bean.getConsumerName())) {
					throw new OAuthClientException("Null consumer : " + bean.getConsumerName(), OAuthClientException.ErrorCode.NULL_CONSUMER);
				} else return;
			} else if (null != bean.getRequestUrl()) {
				Iterator<OAuthConsumer> consumers = this._consumers.values().iterator();
				while (consumers.hasNext()) {
					OAuthConsumer oauthConsumer = consumers.next();
					String baseUrl = ((URL) oauthConsumer.getProperty("serviceProvider.baseURL")).toExternalForm();
					if (bean.getRequestUrl().startsWith(baseUrl)) {
						String name = (String) oauthConsumer.getProperty("name");
						bean.setConsumerName(name);
					}
				}
				if (null == bean.getConsumerName()) {
					throw new OAuthClientException("Null consumer for url : " + bean.getRequestUrl(), OAuthClientException.ErrorCode.NULL_CONSUMER);
				}
			} else {
				throw new OAuthClientException("Null consumer", OAuthClientException.ErrorCode.NULL_CONSUMER);
			}
		} catch (OAuthClientException t) {
            throw t;
        } catch (Throwable t) {
        	_logger.error("Error executing Check consumer", t);
			String message = "Error executing Check consumer";
			throw new ApsSystemException(message, t);
        }
	}
	
    private Boolean checkMethod(ApiMethodRequestBean bean) throws OAuthClientException, ApsSystemException {
        Boolean free = null;
        String url = null;
		try {
            OAuthConsumer consumer = this.getConsumer(bean.getConsumerName());
            if (null == consumer) {
				throw new OAuthClientException("Null consumer : " + bean.getConsumerName(), OAuthClientException.ErrorCode.NULL_CONSUMER);
			}
            URL baseURL = (URL) consumer.getProperty("serviceProvider.baseURL");
            url = baseURL.toString();
            if (!url.endsWith("/")) url += "/";
            url += "api/apistatus/";
			if (bean.getNamespace() != null && bean.getNamespace().trim().length() > 0) {
				url += bean.getNamespace() + "/";
			}
			url += bean.getResourceName() + "/" + bean.getHttpMethod().toString() + "/.json";
			String methodResult = this.invokeGetDeleteMethod(url, true, null);
            InputStream stream = new ByteArrayInputStream(methodResult.getBytes());//method.getResponseBodyAsStream();
            StringApiResponse response = (StringApiResponse) this.unmarshallResponse(stream, 
                    StringApiResponse.class, MediaType.APPLICATION_JSON_TYPE);
            String result = response.getResult();
			if (null != result && result.equalsIgnoreCase(ApiRestStatusServer.ApiStatus.INACTIVE.toString())) {
				throw new OAuthClientException(url, bean.getHttpMethod());
			}
            free = (null != result && result.equalsIgnoreCase(ApiRestStatusServer.ApiStatus.FREE.toString()));
        } catch (OAuthClientException t) {
            throw t;
        } catch (Throwable t) {
        	_logger.error("Error executing Check for method for uri '{}' - method '{}'",url, bean.getHttpMethod(), t);
			String message = "Error executing Check for method for uri '" + url + "' - method " + bean.getHttpMethod();
			throw new ApsSystemException(message, t);
        }
        return free;
    }
    
    private Object unmarshallResponse(InputStream stream, Class expectedType, MediaType responseType) {
        Object bodyObject = null;
        try {
            if (responseType.equals(MediaType.APPLICATION_JSON_TYPE)) {
                JSONProvider jsonProvider = new JSONProvider();
                bodyObject = jsonProvider.readFrom(expectedType, expectedType.getGenericSuperclass(), 
                        expectedType.getAnnotations(), MediaType.APPLICATION_JSON_TYPE, null, stream);
            } else {
                JAXBContext context = JAXBContext.newInstance(expectedType);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                bodyObject = (Object) unmarshaller.unmarshal(stream);
            }
        } catch (Throwable t) {
        	_logger.error("Error unmarshalling response result", t);
        }
        return bodyObject;
    }
    
    private InputStream invokeFreeGetDeleteApi(ApiMethodRequestBean bean, boolean handleException) throws Exception {
        InputStream stream = null;
        try {
            String url = this.buildApiMethodUri(bean);
			boolean isGetMethod = ApiMethod.HttpMethod.GET.equals(bean.getHttpMethod());
            String methodResult = this.invokeGetDeleteMethod(url, isGetMethod, bean.getRequestParameters());
            stream = new ByteArrayInputStream(methodResult.getBytes());
        } catch (Throwable t) {
        	_logger.error("Error invoking Free Api", t);
            if (handleException) {
                throw new Exception("Error invoking Free Api", t);
            }
        }
        return stream;
    }
    
	private InputStream invokeFreePostPutApi(ApiMethodRequestBean bean, boolean handleException) throws Exception {
		InputStream stream = null;
		try {
			String url = this.buildApiMethodUri(bean);
			boolean isPostMethod = ApiMethod.HttpMethod.POST.equals(bean.getHttpMethod());
			MediaType bodyMediaType = (null != bean.getRequestBodyMediaType()) ? bean.getRequestBodyMediaType() : MediaType.APPLICATION_JSON_TYPE;
			String methodResult = this.invokePostPutMethod(url, isPostMethod, bean.getRequestBody(), bean.getRequestParameters(), bodyMediaType);
			stream = new ByteArrayInputStream(methodResult.getBytes());
		} catch (Throwable t) {
			_logger.error("Error invoking Free Api", t);
			if (handleException) {
				throw new Exception("Error invoking Free Api", t);
			}
		}
		return stream;
	}
	
	private String buildApiMethodUri(ApiMethodRequestBean bean) throws Throwable {
		OAuthConsumer consumer = this.getConsumer(bean.getConsumerName());
		if (null == consumer) return null;
		URL baseURL = (URL) consumer.getProperty("serviceProvider.baseURL");
		String url = baseURL.toString();
		String path = bean.getPath();
		if (!url.endsWith("/") && !path.startsWith("/")) url += "/";
		url += path;
		return url;
	}
    
    private String invokeGetDeleteMethod(String url, boolean isGet, Properties parameters) throws Throwable {
        String result = null;
        HttpMethodBase method = null;
		InputStream inputStream = null;
        try {
            HttpClient client = new HttpClient();
			if (isGet) {
				method = new GetMethod(url);
			} else {
				method = new DeleteMethod(url);
			}
            this.addQueryString(method, parameters);
            client.executeMethod(method);
			inputStream = method.getResponseBodyAsStream();
			result = IOUtils.toString(inputStream, "UTF-8");
            //byte[] responseBody = method.getResponseBody();
            //result = new String(responseBody);
        } catch (Throwable t) {
        	_logger.error("Error invoking Get or Delete Method", t);
        } finally {
			if (null != inputStream) {
				inputStream.close();
			}
            if (null != method) {
                method.releaseConnection();
            }
        }
        return result;
    }
    
    private String invokePostPutMethod(String url, boolean isPost, String body, Properties parameters, MediaType mediaType) {
        String result = null;
		EntityEnclosingMethod method = null;
        try {
            HttpClient client = new HttpClient();
			if (isPost) {
				method = new PostMethod(url);
			} else {
				method = new PutMethod(url);
			}
            this.addQueryString(method, parameters);
			method.setRequestBody(body);
			method.setRequestHeader("Content-type", mediaType.toString() + "; charset=UTF-8");
            client.executeMethod(method);
            byte[] responseBody = method.getResponseBody();
            result = new String(responseBody);
        } catch (Throwable t) {
        	_logger.error("Error invoking Post or put Method", t);
        } finally {
            if (null != method) {
                method.releaseConnection();
            }
        }
        return result;
    }
	
	private void addQueryString(HttpMethodBase method, Properties parameters) throws Throwable {
		if (null == parameters) return;
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		Iterator<Object> keyIter = parameters.keySet().iterator();
		while (keyIter.hasNext()) {
			Object key = keyIter.next();
			if (!first) builder.append("&");
			builder.append(key.toString()).append("=").append(parameters.getProperty(key.toString()));
			if (first) first = false;
		}
		method.setQueryString(URIUtil.encodeQuery(builder.toString()));
	}
    
    private InputStream invokeProtectedGetDeleteApi(ApiMethodRequestBean bean, HttpServletRequest request, 
            HttpServletResponse response, boolean handleException, boolean redirectOAuthErrorPage) throws RedirectException, Exception {
        InputStream stream = null;
        OAuthConsumer consumer = null;
        try {
			consumer = this.getConsumer(bean.getConsumerName());
            OAuthAccessor accessor = this.getAccessor(bean, request, response, consumer);
            Collection<OAuth.Parameter> parameters = HttpRequestMessage.getParameters(request);
			if (null != bean.getRequestParameters()) {
                Iterator<Object> iter = bean.getRequestParameters().keySet().iterator();
                while (iter.hasNext()) {
                    String paramKey = iter.next().toString();
                    parameters.add(new OAuth.Parameter(paramKey, bean.getRequestParameters().getProperty(paramKey)));
                }
            }
            URL baseURL = (URL) accessor.consumer.getProperty("serviceProvider.baseURL");
			String url = new URL(baseURL, bean.getPath()).toExternalForm();
			OAuthMessage oauthRequest = accessor.newRequestMessage(String.valueOf(bean.getHttpMethod()), url, parameters);
			OAuthClient client = new OAuthClient(new OAuthHttpClient());
			OAuthMessage oauthResponse = client.invoke(oauthRequest, ParameterStyle.AUTHORIZATION_HEADER, redirectOAuthErrorPage);
			stream = oauthResponse.getBodyAsStream();
        } catch (RedirectException re) {
            if (handleException) {
                this.handleException(bean, re, request, response, consumer, true);
                throw re;
            }
        } catch (OAuthProblemException oae) {
            if (handleException) {
                this.handleException(bean, oae, request, response, consumer, redirectOAuthErrorPage);
				throw oae;
            }
        } catch (Throwable t) {
        	_logger.error("Error invoking api method", t);
            if (handleException) {
                throw new Exception("Error invoking api method", t);
            }
        }
        return stream;
    }
	
    private InputStream invokeProtectedPostPutApi(ApiMethodRequestBean bean, HttpServletRequest request, 
            HttpServletResponse response, boolean handleException, boolean redirectOAuthErrorPage) throws RedirectException, Exception {
        InputStream stream = null;
        OAuthConsumer consumer = null;
        try {
			consumer = this.getConsumer(bean.getConsumerName());
            OAuthAccessor accessor = this.getAccessor(bean, request, response, consumer);
            Collection<OAuth.Parameter> parameters = HttpRequestMessage.getParameters(request);
			if (null != bean.getRequestParameters()) {
                Iterator<Object> iter = bean.getRequestParameters().keySet().iterator();
                while (iter.hasNext()) {
                    String paramKey = iter.next().toString();
                    parameters.add(new OAuth.Parameter(paramKey, bean.getRequestParameters().getProperty(paramKey)));
                }
            }
            URL baseURL = (URL) accessor.consumer.getProperty("serviceProvider.baseURL");
			String url = new URL(baseURL, bean.getPath()).toExternalForm();
			InputStream streamRequest = new ByteArrayInputStream(bean.getRequestBody().getBytes("UTF-8"));
			OAuthMessage oauthRequest = accessor.newRequestMessage(String.valueOf(bean.getHttpMethod()), url, parameters, streamRequest);
			OAuthClient client = new OAuthClient(new OAuthHttpClient());
			OAuthMessage oauthResponse = client.invoke(oauthRequest, ParameterStyle.AUTHORIZATION_HEADER, redirectOAuthErrorPage);
			stream = oauthResponse.getBodyAsStream();
        } catch (RedirectException re) {
            if (handleException) {
                this.handleException(bean, re, request, response, consumer, true);
                throw re;
            }
        } catch (OAuthProblemException oae) {
            if (handleException) {
                this.handleException(bean, oae, request, response, consumer, redirectOAuthErrorPage);
				throw oae;
            }
        } catch (Throwable t) {
        	_logger.error("Error invoking api method", t);
            if (handleException) {
                throw new Exception("Error invoking api method", t);
            }
        }
        return stream;
    }
	
	@Override
    public OAuthConsumer getConsumer(String name) throws IOException {
        return this._consumers.get(name);
    }
	
    /**
     * Get the access token and token secret for the given consumer. Get them
     * from cookies if possible; otherwise obtain them from the service
     * provider. In the latter case, throw RedirectException.
     * @throws IOException 
     * @throws URISyntaxException 
     */
    private OAuthAccessor getAccessor(ApiMethodRequestBean bean, HttpServletRequest request,
            HttpServletResponse response, OAuthConsumer consumer) throws OAuthException, IOException, URISyntaxException {
        OAuthAccessor accessor = null;
        CookieMap cookies = new CookieMap(request, response);
        accessor = this.newAccessor(consumer, cookies);
        if (accessor.accessToken == null) {
            this.getAccessToken(bean, request, cookies, accessor);
        }
        return accessor;
    }
	
    /**
     * Construct an accessor from cookies. The resulting accessor won't
     * necessarily have any tokens.
     */
    @Override
    public OAuthAccessor newAccessor(OAuthConsumer consumer, CookieMap cookies) throws OAuthException {
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        String consumerName = (String) consumer.getProperty("name");
        accessor.requestToken = cookies.get(consumerName + ".requestToken");
        accessor.accessToken = cookies.get(consumerName + ".accessToken");
        accessor.tokenSecret = cookies.get(consumerName + ".tokenSecret");
        return accessor;
    }
	
    /** Remove all the cookies that contain accessors' data. */
    @Override
    public void removeAccessors(CookieMap cookies) {
        List<String> names = new ArrayList<String>(cookies.keySet());
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (name.endsWith(".requestToken") || name.endsWith(".accessToken")
                    || name.endsWith(".tokenSecret")) {
                cookies.remove(name);
            }
        }
    }
	
    /**
     * Get a fresh access token from the service provider.
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws RedirectException to obtain authorization
     */
	private void getAccessToken(ApiMethodRequestBean bean, HttpServletRequest request, CookieMap cookies, 
			OAuthAccessor accessor) throws OAuthException, IOException, URISyntaxException {
        final String consumerName = (String) accessor.consumer.getProperty("name");
        final String callbackURL = getCallbackURL(bean, request);
        List<OAuth.Parameter> parameters = OAuth.newList(OAuth.OAUTH_CALLBACK, callbackURL);
        // Google needs to know what you intend to do with the access token:
        Object scope = accessor.consumer.getProperty("request.scope");
        if (scope != null) {
            parameters.add(new OAuth.Parameter("scope", scope.toString()));
        }
        OAuthClient client = new OAuthClient(new OAuthHttpClient());
        OAuthMessage response = client.getRequestTokenResponse(accessor, null, parameters);
		cookies.put(consumerName + ".requestToken", accessor.requestToken);
		cookies.put(consumerName + ".tokenSecret", accessor.tokenSecret);
		String authorizationURL = accessor.consumer.serviceProvider.userAuthorizationURL;
        if (authorizationURL.startsWith("/")) {
            authorizationURL = (new URL(new URL(request.getRequestURL().toString()), 
                    request.getContextPath() + authorizationURL)).toString();
        }
        authorizationURL = OAuth.addParameters(authorizationURL, 
                OAuth.OAUTH_TOKEN, accessor.requestToken);
        if (response.getParameter(OAuth.OAUTH_CALLBACK_CONFIRMED) == null) {
            authorizationURL = OAuth.addParameters(authorizationURL, 
                    OAuth.OAUTH_CALLBACK, callbackURL);
        }
        throw new RedirectException(authorizationURL);
    }
	
    private String getCallbackURL(ApiMethodRequestBean bean, HttpServletRequest request) throws IOException {
        URL base = new URL(new URL(request.getRequestURL().toString()), request.getContextPath() + Callback.PATH);
		String returnTo = (null != bean && null != bean.getRedirectUrl()) ? bean.getRedirectUrl() : this.getRequestPath(request);
        return OAuth.addParameters(base.toExternalForm(), "consumer", bean.getConsumerName(), "returnTo", returnTo);
    }
    
    /** Reconstruct the requested URL path, complete with query string (if any). */
    private String getRequestPath(HttpServletRequest request) throws MalformedURLException {
        StringBuilder path = new StringBuilder(request.getRequestURL());
        String queryString = request.getQueryString();//url.getQuery();
        if (queryString != null) {
            path.append("?").append(queryString);
        }
        String pathToReturn = path.toString();
		return pathToReturn;
    }
	
	@Override
    public void handleException(Exception e, HttpServletRequest request,
            HttpServletResponse response, OAuthConsumer consumer) throws IOException, ServletException {
		this.handleException(null, e, request, response, consumer, true);
	}
    
    /**
     * Handle an exception that occurred while processing an HTTP request.
     * Depending on the exception, either send a response, redirect the client
     * or propagate an exception.
     */
    public void handleException(ApiMethodRequestBean bean, Exception e, HttpServletRequest request,
            HttpServletResponse response, OAuthConsumer consumer, boolean redirectOAuthErrorPage)
            throws IOException, ServletException {
        if (e instanceof RedirectException) {
            RedirectException redirect = (RedirectException) e;
            String targetURL = redirect.getTargetURL();
            if (targetURL != null) {
                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                response.setHeader("Location", targetURL);
            }
        } else if (e instanceof OAuthProblemException) {
            OAuthProblemException p = (OAuthProblemException) e;
            String problem = p.getProblem();
            List<String> recoverableProblems = Arrays.asList(RECOVERABLE_PROBLEMS);
            if (consumer != null && recoverableProblems.contains(problem)) {
                try {
                    CookieMap cookies = new CookieMap(request, response);
                    OAuthAccessor accessor = this.newAccessor(consumer, cookies);
                    this.getAccessToken(bean, request, cookies, accessor);
                } catch (Exception e2) {
                    handleException(bean, e2, request, response, null, redirectOAuthErrorPage);
                }
            } else if (redirectOAuthErrorPage) {
                try {
                    StringWriter s = new StringWriter();
                    PrintWriter pw = new PrintWriter(s);
                    e.printStackTrace(pw);
                    pw.flush();
                    p.setParameter("stack trace", s.toString());
                } catch (Exception rats) {
                    //nothing to catch
                }
                response.setStatus(p.getHttpStatusCode());
                response.resetBuffer();
                request.setAttribute("OAuthProblemException", p);
                request.getRequestDispatcher("/WEB-INF/plugins/jpoauthclient/OAuthProblemException.jsp").forward(request, response);
            }
        } else if (e instanceof IOException) {
            throw (IOException) e;
        } else if (e instanceof ServletException) {
            throw (ServletException) e;
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            throw new ServletException(e);
        }
    }
    
    protected ServletContext getServletContext() {
        return _servletContext;
    }
	@Override
    public void setServletContext(ServletContext servletContext) {
        this._servletContext = servletContext;
    }
    
	protected ConfigInterface getConfigManager() {
        return _configManager;
    }
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }
	
    private ServletContext _servletContext;
    
    private ConfigInterface _configManager;
	
	private Map<String, OAuthConsumer> _consumers = new HashMap<String, OAuthConsumer>();
	
	private static final String[] RECOVERABLE_PROBLEMS = 
            new String[]{"token_revoked", "token_expired", "permission_unknown"};
    
}
