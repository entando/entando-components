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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ApiMethodRequestBean {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiMethodRequestBean.class);

	public ApiMethodRequestBean(URL url, ApiMethod.HttpMethod httpMethod) {
		if (!isValidEntandoResourceUri(url)) {
			throw new RuntimeException("Invalid entando resource uri - " + url.toString());
		}
		this.setRequestUrl(url.toExternalForm());
		try {
			String[] sections = url.getPath().split("/");
			String resourceName = null;
			String namespace = null;
			String langCode = null;
			String responseType = null;
			for (int i = 0; i < sections.length; i++) {
				String section = sections[sections.length - i - 1];
				if (i==0) {
					if (section.indexOf(".") > -1) {
						String[] nameSections = section.split(".");
						resourceName = nameSections[0];
						responseType = nameSections[1];
					} else {
						resourceName = section;
					}
				} else if (i==1 && !sections[sections.length - 3].equalsIgnoreCase("rs")) {
					namespace = section;
				} else if ((i==1 && sections[sections.length - 3].equalsIgnoreCase("rs") && null == namespace) || 
						(i==2 && sections[sections.length - 4].equalsIgnoreCase("rs") && null != namespace)) {
					langCode = section;
				} else break;
			}
			this.setLangCode(langCode);
			this.setResourceName(resourceName);
			this.setNamespace(namespace);
			Properties requestParameters = this.extractRequestParameters(url.getQuery());
			this.setRequestParameters(requestParameters);
			this.setHttpMethod(httpMethod);
			if (null != responseType && responseType.equalsIgnoreCase("xml")) {
				this.setMediaType(MediaType.APPLICATION_XML_TYPE);
			} else {
				this.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			}
		} catch (Throwable t) {
			_logger.error("Error creating request bean", t);
			throw new RuntimeException("Error creating request bean", t);
		}
    }
	
	private Properties extractRequestParameters(String queryString) {
		if (null == queryString || queryString.trim().length() == 0) {
			return null;
		}
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
	
    public ApiMethodRequestBean(String consumerName, 
            ApiMethod.HttpMethod httpMethod, String namespace, String resourceName, Properties requestParameters) {
        this.setConsumerName(consumerName);
        this.setHttpMethod(httpMethod);
        this.setResourceName(resourceName);
        this.setNamespace(namespace);
        this.setRequestParameters(requestParameters);
    }
    
	public static boolean isValidEntandoResourceUri(URL url) {
		try {
			//System.out.println("url.getPath() " + url.getPath());
			String[] sections = url.getPath().split("/");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < sections.length; i++) {
				String section = sections[i];
				if (section != null && section.trim().length() > 0) {
					list.add(section.trim());
				}
			}
			//System.out.println("SEZIONI " + list.size());
			if (list.size() < 4 || list.size() > 6) {
				//System.out.println("SEZIONI invalide " + list.size());
				return false;
			}
			if (list.size() == 4) {
				if (!list.get(0).equals("api") || !list.get(1).equals("rs")) {
					//System.out.println("formato /api/rs/<LANG>/<RESOURCE_NAME> invalido");
					return false;
				}
				return true;
			} else if (list.size() == 5) {
				if (list.get(0).equals("api") && list.get(1).equals("rs")) {
					//System.out.println("formato /api/rs/<LANG>/<NAMESPACE>/<RESOURCE_NAME> valido");
					return true;
				} else if (list.get(1).equals("api") && list.get(2).equals("rs")) {
					//System.out.println("formato /<CONTEXT>/api/rs/<LANG>/<RESOURCE_NAME> valido");
					return true;
				}
				//System.out.println("formato /api/rs/<LANG>/<NAMESPACE>/<RESOURCE_NAME> o /<CONTEXT>/api/rs/<LANG>/<RESOURCE_NAME> invalido");
				return false;
			} else if (list.size() == 6) {
				if (list.get(1).equals("api") && list.get(2).equals("rs")) {
					//System.out.println("formato /<CONTEXT>/api/rs/<LANG>/<NAMESPACE>/<RESOURCE_NAME> valido");
					return true;
				}
				return false;
			} else {
				return false;
			}
		} catch (Throwable t) {
			_logger.error("Error validating resource URI", t);
			return false;
		}
	}
	
    public String getConsumerName() {
        return _consumerName;
    }
    protected void setConsumerName(String consumerName) {
        this._consumerName = consumerName;
    }
	
	public String getRequestUrl() {
		return _requestUrl;
	}
	protected void setRequestUrl(String requestUrl) {
		this._requestUrl = requestUrl;
	}
    
    public ApiMethod.HttpMethod getHttpMethod() {
        return _httpMethod;
    }
    public void setHttpMethod(ApiMethod.HttpMethod httpMethod) {
        this._httpMethod = httpMethod;
    }
    
    public MediaType getMediaType() {
        return _mediaType;
    }
    public void setMediaType(MediaType mediaType) {
        this._mediaType = mediaType;
    }
    
    public String getResourceName() {
        return _resourceName;
    }
    public void setResourceName(String resourceName) {
        this._resourceName = resourceName;
    }
	
	public String getNamespace() {
		return _namespace;
	}
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
    
    public String getLangCode() {
        return _langCode;
    }
    public void setLangCode(String langCode) {
        this._langCode = langCode;
    }
    
    public String getPath() {
        StringBuilder path = new StringBuilder("api/rs/");
        String langCode = (null == this.getLangCode()) ? "en" : this.getLangCode();
		path.append(langCode).append("/");
		if (this.getNamespace() != null && this.getNamespace().trim().length() > 0) {
			path.append(this.getNamespace()).append("/");
		}
        path.append(this.getResourceName()).append(".");
        if (MediaType.APPLICATION_XML_TYPE.equals(this.getMediaType())) {
            path.append("xml");
        } else {
            path.append("json");
        }
        return path.toString();
    }
    
    public Properties getRequestParameters() {
        return _requestParameters;
    }
    public void setRequestParameters(Properties requestParameters) {
        this._requestParameters = requestParameters;
    }
	
	public void setRequestBody(String content, MediaType mediaType) {
		this.setRequestBody(content);
		this.setRequestBodyMediaType(mediaType);
	}
	
	public String getRequestBody() {
		return _requestBody;
	}
	protected void setRequestBody(String requestBody) {
		this._requestBody = requestBody;
	}
	
	public MediaType getRequestBodyMediaType() {
		return _requestBodyMediaType;
	}
	protected void setRequestBodyMediaType(MediaType requestBodyMediaType) {
		this._requestBodyMediaType = requestBodyMediaType;
	}
	/*
    public Class getExpectedType() {
        return _expectedType;
    }
    public void setExpectedType(Class expectedType) {
        this._expectedType = expectedType;
    }
    */
	public String getRedirectUrl() {
		return _redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this._redirectUrl = redirectUrl;
	}
	
	private String _consumerName;
	
	private String _requestUrl;
	private String _resourceName;
	private String _namespace;
    private ApiMethod.HttpMethod _httpMethod;
    private MediaType _mediaType;
    private String _langCode;
    private Properties _requestParameters;
	
	private String _requestBody;
	private MediaType _requestBodyMediaType;
	
	//private Class _expectedType;
	private String _redirectUrl;
    
}