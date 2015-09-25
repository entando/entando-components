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
package org.entando.entando.plugins.jpoauthclient.aps.system;

import net.oauth.OAuthException;

import org.entando.entando.aps.system.services.api.model.ApiMethod;

/**
 * @author E.Santoboni
 */
public class OAuthClientException extends OAuthException {
    
	public OAuthClientException(String message, ErrorCode errorCode) {
		super(message);
		this.setErrorCode(errorCode);
	}
	
    public OAuthClientException(String uri, ApiMethod.HttpMethod method) {
        super("Unknown method for uri '" + uri + "' - method " + method);
		this.setHttpMethod(_httpMethod);
		this.setUri(uri);
		this.setErrorCode(ErrorCode.UNKNOWN_METHOD);
    }
	
	public String getUri() {
		return _uri;
	}
	protected void setUri(String uri) {
		this._uri = uri;
	}
	
    public ApiMethod.HttpMethod getHttpMethod() {
        return _httpMethod;
    }
    protected void setHttpMethod(ApiMethod.HttpMethod httpMethod) {
        this._httpMethod = httpMethod;
    }
	
	public ErrorCode getErrorCode() {
		return _errorCode;
	}
	public void setErrorCode(ErrorCode errorCode) {
		this._errorCode = errorCode;
	}
    
	private String _uri;
	private ApiMethod.HttpMethod _httpMethod;
	
	private ErrorCode _errorCode;
	
	public enum ErrorCode{UNKNOWN_METHOD , NULL_CONSUMER};
    
}
