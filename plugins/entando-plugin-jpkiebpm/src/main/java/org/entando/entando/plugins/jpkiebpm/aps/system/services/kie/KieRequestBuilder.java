/*
* The MIT License
*
* Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.entando.entando.plugins.jprestapi.aps.core.RequestBuilder;
import org.entando.entando.plugins.jprestapi.aps.core.helper.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Entando
 */
public class KieRequestBuilder extends RequestBuilder {
    private static final Logger _logger = LoggerFactory.getLogger(KieRequestBuilder.class);
    
    /**
     *  Used for Request Parameters that occur more than once:
     *  Key: parameter name
     *  Value: comma separated list of values
     */
    private Map<String,String> recurringParameters;

    public KieRequestBuilder(KieClient client) {
        this._configClient = client;
    }

    @Override
    protected void setupRequest(HttpRequestBase verb) throws Throwable {
        // process evaluation URL
        RequestHelper.addBaseUrl(verb, _configClient.getBaseUrl());
        // headers are set on single mothod basis
    }

    @Override
    protected DefaultHttpClient setupClient() {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        return httpclient;
    }

    @Override
    protected DefaultHttpClient setupAuthenticationClient() {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        // basic authentication
        String username = _configClient.getCredentials().getUsername();
        String password = _configClient.getCredentials().getPassword();
        UsernamePasswordCredentials authCredentials = new UsernamePasswordCredentials(username, password);

        // TODO restrict host and scope!
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
        httpclient.getCredentialsProvider().setCredentials(authScope, authCredentials);
        return httpclient;
    }

    @Override
    protected void setupOverallConnectionTimeout(DefaultHttpClient client) {
        if (null != _configClient.getTimeoutMsec()) {
            client.getParams().setIntParameter(PARAM_TIMEOUT,
                    _configClient.getTimeoutMsec());
        }
    }
    
    @Override
    protected void injectParameters(HttpRequestBase verb) throws Throwable {
    		super.injectParameters(verb);
    		if (recurringParameters != null && recurringParameters.keySet().size() > 0) {
	    		for (String parameterName : recurringParameters.keySet()) {
	    			String[] repeatingValues = recurringParameters.get(parameterName).split(",");
	    			for (String parameterValue : repeatingValues) {
	    				Map<String,String> parameterMap = new HashMap<String,String>();
	    				parameterMap.put(parameterName, parameterValue);
	    				RequestHelper.appendParameters(verb, parameterMap);
	    			}
	    		}
	        if (_debug) {
	            _logger.info("after appending recurring parameters: {}",verb.getURI());
	        }
    		} else {
    			_logger.debug("No recurring parameters found...");
    		}
    }


    public Map<String, String> getRecurringParameters() {
		return recurringParameters;
	}

	public void setRecurringParameters(Map<String, String> recurringParameters) {
		this.recurringParameters = recurringParameters;
	}

	private final KieClient _configClient;
}
