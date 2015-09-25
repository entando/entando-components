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
package org.entando.entando.plugins.jpoauthclient.aps.internalservlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import net.oauth.OAuthProblemException;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.plugins.jpoauthclient.aps.system.ConsumerSystemConstants;
import org.entando.entando.plugins.jpoauthclient.aps.system.OAuthClientException;
import org.entando.entando.plugins.jpoauthclient.aps.system.RedirectException;
import org.entando.entando.plugins.jpoauthclient.aps.system.services.client.ApiMethodRequestBean;
import org.entando.entando.plugins.jpoauthclient.aps.system.services.client.IProviderConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public class OAuthClientAction extends BaseAction implements ServletResponseAware {
	
	private static final Logger _logger = LoggerFactory.getLogger(OAuthClientAction.class);
	
	@Override
	public String execute() throws Exception {
		this.removeSessionParameters(true);
		return super.execute();
	}
	
	public String viewTextResult() {
		this.removeSessionParameters(false);
		return SUCCESS;
	}
	
	public String call() {
		IProviderConnectionManager manager = this.getProviderConnectionManager();
		try {
			ApiMethodRequestBean bean = this.createRequestBean();
			InputStream is = manager.invokeApiMethod(bean, this.getRequest(), this.getServletResponse(), true, false);
			if (null == is) {
				this.addActionError(this.getText("jpoauthclient.error.response.null"));
				return INPUT;
			}
			this.removeSessionParameters(true);
			this.setResponseBody(this.convertStreamToString(is));
		} catch (OAuthClientException ume) {
            this.addActionError(ume.getMessage());
			return INPUT;
        } catch (RedirectException re) {
			this.insertSessionParameters();
			String url = this.getServletResponse().encodeRedirectURL(re.getTargetURL());
			try {
				this.getServletResponse().sendRedirect(url);
			} catch (Throwable t) {
				_logger.error("Error redirect to {}", url, t);
				this.addActionError("Error redirect to " + url);
				return INPUT;
			}
			return null;
        } catch (OAuthProblemException oae) {
			this.addActionError(oae.getMessage());
			return INPUT;
		} catch (Throwable t) {
			_logger.error("Error executing REST Service call", t);
			return FAILURE;
		}
		if (null != this.getPlainTextResult() && this.getPlainTextResult()) {
			String responseBody = this.getResponseBody();
			this.insertSessionParam(this.getRequest().getSession(), RESPONSE_BODY_SESSION_PARAM, responseBody);
			if (this.getResponseContentType().equals(MediaType.APPLICATION_JSON_TYPE.toString())) {
				return "jsonPlainTextResult";
			} else {
				return "xmlPlainTextResult";
			}
		}
		return SUCCESS;
	}
	
	private void insertSessionParameters() {
		HttpSession session = this.getRequest().getSession();
		//this.valueSessionParam(session, RESOURCE_URI_SESSION_PARAM, this.getResourceUri());
		this.insertSessionParam(session, RESOURCE_NAME_SESSION_PARAM, this.getResourceName());
		this.insertSessionParam(session, RESOURCE_NAMESPACE_SESSION_PARAM, this.getNamespace());
		this.insertSessionParam(session, QUERY_STRING_SESSION_PARAM, this.getQueryString());
		this.insertSessionParam(session, LANG_CODE_SESSION_PARAM, this.getLangCode());
		this.insertSessionParam(session, RESPONSE_CONTENT_TYPE_SESSION_PARAM, this.getResponseContentType());
		this.insertSessionParam(session, METHOD_SESSION_PARAM, this.getMethod());
		this.insertSessionParam(session, REQUEST_CONTENT_TYPE_SESSION_PARAM, this.getRequestContentType());
		this.insertSessionParam(session, REQUEST_BODY_SESSION_PARAM, this.getRequestBody());
		this.insertSessionParam(session, PLAIN_TEXT_RESULT_SESSION_PARAM, this.getPlainTextResult());
		this.insertSessionParam(session, RESPONSE_BODY_SESSION_PARAM, this.getResponseBody());
	}
	
	private void insertSessionParam(HttpSession session, String name, Object value) {
		if (null != value && 
				(!(value instanceof String) || (value instanceof String && !((String) value).isEmpty()))) {
			session.setAttribute(name, value);
		}
	}
	
	private void removeSessionParameters(boolean removeResult) {
		HttpSession session = this.getRequest().getSession();
		//session.removeAttribute(RESOURCE_URI_SESSION_PARAM);
		session.removeAttribute(RESOURCE_NAME_SESSION_PARAM);
		session.removeAttribute(RESOURCE_NAMESPACE_SESSION_PARAM);
		session.removeAttribute(QUERY_STRING_SESSION_PARAM);
		session.removeAttribute(LANG_CODE_SESSION_PARAM);
		session.removeAttribute(RESPONSE_CONTENT_TYPE_SESSION_PARAM);
		session.removeAttribute(METHOD_SESSION_PARAM);
		session.removeAttribute(REQUEST_CONTENT_TYPE_SESSION_PARAM);
		session.removeAttribute(REQUEST_BODY_SESSION_PARAM);
		session.removeAttribute(PLAIN_TEXT_RESULT_SESSION_PARAM);
		if (removeResult) {
			session.removeAttribute(RESPONSE_BODY_SESSION_PARAM);
		}
	}
	
	private ApiMethodRequestBean createRequestBean() throws Throwable {
		ApiMethodRequestBean bean = null;
		try {
			//URL url = new URL(this.getResourceUri());
			ApiMethod.HttpMethod method = Enum.valueOf(ApiMethod.HttpMethod.class, this.getMethod().toUpperCase());
			
			//bean = new ApiMethodRequestBean(url, method);
			String namespace = (null != this.getNamespace() && this.getNamespace().trim().length() > 0) 
					? this.getNamespace() : null;
			Properties properties = this.extractRequestParameters(this.getQueryString());
			bean = new ApiMethodRequestBean(this.getConsumerName(), method, 
					namespace, this.getResourceName(), properties);
			//bean.setConsumerName(this.getConsumerName());
			if (MediaType.APPLICATION_JSON_TYPE.toString().equals(this.getResponseContentType())) {
				bean.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			} else {
				bean.setMediaType(MediaType.APPLICATION_XML_TYPE);
			}
			if (method.equals(ApiMethod.HttpMethod.POST) || method.equals(ApiMethod.HttpMethod.PUT)) {
				if (MediaType.APPLICATION_JSON_TYPE.toString().equals(this.getRequestContentType())) {
					bean.setRequestBody(this.getRequestBody(), MediaType.APPLICATION_JSON_TYPE);
				} else {
					bean.setRequestBody(this.getRequestBody(), MediaType.APPLICATION_XML_TYPE);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error creating request bean", t);
			throw new ApsSystemException("Error creating request bean", t);
		}
		return bean;
	}
	
	public String getConsumerName() {
		return ConsumerSystemConstants.DEFAULT_CONSUMER_NAME;
	}
	
	private Properties extractRequestParameters(String queryString) {
		if (null == queryString || queryString.trim().length() == 0) {
			return null;
		}
		queryString = queryString.trim();
		Properties requestParameters = new Properties();
		if (queryString.indexOf("&") > -1) {
			String[] querySections = queryString.split("&");
			for (int i = 0; i < querySections.length; i++) {
				String querySection = querySections[i];
				String[] elements = querySection.split("=");
				if (elements.length == 2) {
					requestParameters.put(elements[0], elements[1]);
				}
			}
		} else {
			String[] elements = queryString.split("=");
			if (elements.length == 2) {
				requestParameters.put(elements[0], elements[1]);
			}
		}
		return requestParameters;
	}
	
	public ApiMethod.HttpMethod[] getAllowedMethods() {
		return ApiMethod.HttpMethod.values();
	}
	
	public MediaType[] getAllowedMediaTypes() {
		MediaType[] types = new MediaType[2];
		types[0] = MediaType.APPLICATION_XML_TYPE;
		types[1] = MediaType.APPLICATION_JSON_TYPE;
		return types;
	}
	
	public boolean isValidMethod() {
		ApiMethod.HttpMethod[] methods = this.getAllowedMethods();
		for (int i = 0; i < methods.length; i++) {
			ApiMethod.HttpMethod httpMethod = methods[i];
			if (httpMethod.toString().equals(this.getMethod())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidMediaType(String type) {
		MediaType[] types = this.getAllowedMediaTypes();
		for (int i = 0; i < types.length; i++) {
			MediaType mediaType = types[i];
			if (mediaType.toString().equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	public String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			throw new RuntimeException(this.getText("jpoauthclient.error.response.null"));
		}
	}
	
	private String getSessionParam(String name) {
		return (String) this.getRequest().getSession().getAttribute(name);
	}
	/*
	public boolean isValidEntandoResourceUri() {
		try {
			URL url = new URL(this.getResourceUri());
			return ApiMethodRequestBean.isValidEntandoResourceUri(url);
	*/
			/*
			String[] sections = url.getPath().split("/");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < sections.length; i++) {
				String section = sections[i];
				if (section != null && section.trim().length() > 0) {
					list.add(section.trim());
				}
			}
			if (list.size() < 4 || list.size() > 6) {
				return false;
			}
			if (list.size() == 4) {
				if (!list.get(0).equals("api") || !list.get(1).equals("rs")) {
					return false;
				}
				return true;
			} else if (list.size() == 5) {
				if (list.get(0).equals("api") && list.get(1).equals("rs")) {
					return true;
				} else if (list.get(1).equals("api") && list.get(2).equals("rs")) {
					return true;
				}
				return false;
			} else if (list.size() == 6) {
				if (list.get(1).equals("api") && list.get(2).equals("rs")) {
					return true;
				}
				return false;
			} else {
				return false;
			}
			*/
	/*
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "isValidEntandoResourceUri", "Error validating resource URI");
			return false;
		}
	}
	*/
	/*
	public String getResourceUri() {
		String sessionValue = this.getSessionParam(RESOURCE_URI_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _resourceUri;
	}
	public void setResourceUri(String resourceUri) {
		this._resourceUri = resourceUri;
	}
	*/
	public String getResourceName() {
		String sessionValue = this.getSessionParam(RESOURCE_NAME_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _resourceName;
	}
	public void setResourceName(String resourceName) {
		this._resourceName = resourceName;
	}
	
	public String getNamespace() {
		String sessionValue = this.getSessionParam(RESOURCE_NAMESPACE_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _namespace;
	}
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	
	public String getLangCode() {
		String sessionValue = this.getSessionParam(LANG_CODE_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _langCode;
	}
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	public String getQueryString() {
		String sessionValue = this.getSessionParam(QUERY_STRING_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _queryString;
	}
	public void setQueryString(String queryString) {
		this._queryString = queryString;
	}
	
	public String getResponseContentType() {
		String sessionValue = this.getSessionParam(RESPONSE_CONTENT_TYPE_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _responseContentType;
	}
	public void setResponseContentType(String responseContentType) {
		this._responseContentType = responseContentType;
	}
	
	public String getMethod() {
		String sessionValue = this.getSessionParam(METHOD_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _method;
	}
	public void setMethod(String method) {
		this._method = method;
	}
	
	public String getRequestContentType() {
		String sessionValue = this.getSessionParam(REQUEST_CONTENT_TYPE_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _requestContentType;
	}
	public void setRequestContentType(String requestContentType) {
		this._requestContentType = requestContentType;
	}
	
	public String getRequestBody() {
		String sessionValue = this.getSessionParam(REQUEST_BODY_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _requestBody;
	}
	public void setRequestBody(String requestBody) {
		this._requestBody = requestBody;
	}
	
	public String getServiceResponse() {
		return this.getSessionParam(RESPONSE_BODY_SESSION_PARAM);
	}
	
	public String getResponseBody() {
		return _responseBody;
	}
	public void setResponseBody(String responseBody) {
		this._responseBody = responseBody;
	}
	
	public Boolean getPlainTextResult() {
		Boolean sessionValue = (Boolean) this.getRequest().getSession().getAttribute(PLAIN_TEXT_RESULT_SESSION_PARAM);
		if (null != sessionValue) {
			return sessionValue;
		}
		return _plainTextResult;
	}
	public void setPlainTextResult(Boolean plainTextResult) {
		this._plainTextResult = plainTextResult;
	}
	
	public HttpServletResponse getServletResponse() {
		return _servletResponse;
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this._servletResponse = servletResponse;
	}
	
	public IProviderConnectionManager getProviderConnectionManager() {
		return _providerConnectionManager;
	}
	public void setProviderConnectionManager(IProviderConnectionManager providerConnectionManager) {
		this._providerConnectionManager = providerConnectionManager;
	}
	
	//private String _resourceUri;
	private String _resourceName;
	private String _namespace;
	private String _langCode;
	private String _queryString;
	private String _responseContentType;
	private String _method;
	private String _requestContentType;
	private String _requestBody;
	
	//private String _serviceResponse;
	private String _responseBody;
	
	private Boolean _plainTextResult;
	
	private HttpServletResponse _servletResponse;
	
	private IProviderConnectionManager _providerConnectionManager;
	
	//private String RESOURCE_URI_SESSION_PARAM = "jpoauthclient_resourceUri_sessionParam";
	private String RESOURCE_NAME_SESSION_PARAM = "jpoauthclient_resourceName_sessionParam";
	private String RESOURCE_NAMESPACE_SESSION_PARAM = "jpoauthclient_namespace_sessionParam";
	private String QUERY_STRING_SESSION_PARAM = "jpoauthclient_querystring_sessionParam";
	private String LANG_CODE_SESSION_PARAM = "jpoauthclient_langCode_sessionParam";
	private String RESPONSE_CONTENT_TYPE_SESSION_PARAM = "jpoauthclient_responseContentType_sessionParam";
	private String METHOD_SESSION_PARAM = "jpoauthclient_method_sessionParam";
	private String REQUEST_CONTENT_TYPE_SESSION_PARAM = "jpoauthclient_requestContentType_sessionParam";
	private String REQUEST_BODY_SESSION_PARAM = "jpoauthclient_requestBody_sessionParam";
	private String PLAIN_TEXT_RESULT_SESSION_PARAM = "jpoauthclient_plainTextResult_sessionParam";
	
	private String RESPONSE_BODY_SESSION_PARAM = "jpoauthclient_responseBody_sessionParam";
	
}