/*
 * Copyright 2016-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jprestapi.aps.core;

import java.util.Map;

import org.apache.http.HttpResponse;

/**
 *
 * @author entando
 */
interface IRequestBuilder {

  /**
   * Setup the endpoint to invoke
   *
   * @param endpoint
   * @return
   */
  public RequestBuilder setEndpoint(Endpoint endpoint);

  /**
   * Setup the credentials to use
   *
   * @param credentials
   * @return
   */
  public RequestBuilder setCredentials(IAuthenticationCredentials credentials);

  /**
   * Setup the specific headers to include for the call
   *
   * @param headersMap
   * @return
   */
  public RequestBuilder setHeaders(Map<String, String> headersMap);

  /**
   * Setup optional request parameters
   *
   * @param requestParams
   * @return
   */
  public RequestBuilder setRequestParams(Map<String, String> requestParams);

  /**
   * Setup test mode
   *
   * @param mode if true no requests will be performed.
   * @return
   */
  public RequestBuilder setTestMode(boolean mode);

  /**
   * Setup unmarshalling options for the expected class
   *
   * @param isJson if true, a JSON is expected
   * @param hasRoot if true the object has a root element
   * @return
   */
  public RequestBuilder setUnmarshalOptions(boolean isJson, boolean hasRoot);

  /**
   * Setup verbose debugging options
   *
   * @param debug when true it increases the logging verbosity
   * @return
   */
  public RequestBuilder setDebug(boolean debug);

  /**
   * Setup additional path to the endpoint URL provided. This method can be
   * called multiple times
   *
   * @param path
   * @return
   */
  public RequestBuilder addPath(String path);

  /**
   * Perform the request to the designed enpoint and unmarshalls the result in
   * the expected class.
   *
   * @param expected the expected resut type class
   * @return
   * @throws Throwable
   */
  public Object doRequest(Class<?> expected) throws Throwable;

  /**
   * Perform the request to the designed endpoint
   *
   * @return the String representing the body of the response, if any
   * @throws Throwable
   */
  public String doRequest() throws Throwable;

}
