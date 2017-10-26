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
package org.entando.entando.plugins.jprestapi.aps.core.helper;

import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Helper class for URI management.
 * We perform only basic checks on input data
 * 
 * @author entando
 */
public class RequestHelper {

  /**
   * Append the given path to the existing base
   *
   * @param address
   * @param path
   * @return
   */
  public static String appendPath(String address, String path) {
    if (null != path) {
      if (address.endsWith("/")
              ^ path.startsWith("/")) {
        address = address.concat(path);
      } else if (address.endsWith("/")
              && path.startsWith("/")) {
        address = address.concat(path.substring(1));
      } else {
        address = address.concat("/" + path);
      }
    }
    return address;
  }

  /**
   * Append the given path to the existing base
   *
   * @param address
   * @param list
   * @return
   */
  public static String appendPath(String address, List<String> list) {
    if (null != address
            && !list.isEmpty()) {
      for (String path : list) {
        address = appendPath(address, path);
      }
    }
    return address;
  }

  /**
   * Append the given path to the existing base
   *
   * @param uri
   * @param path
   * @return
   */
  public static void appendPath(HttpRequestBase verb, List<String> path) throws Throwable {

    if (null != verb) {
      String address = verb.getURI().toString();

      address = RequestHelper.appendPath(address, path);
      URI uri = new URI(address);
      verb.setURI(uri);
    }
  }

  /**
   * Appen query parameters
   *
   * @param verb
   * @param params
   * @throws Throwable
   */
  public static void appendParameters(HttpRequestBase verb, Map<String, String> params) throws Throwable {
    if (null != verb
            && null != params && !params.isEmpty()) {
      String address = verb.getURI().toString();
      boolean isAmpersand = address.contains("?");
      StringBuilder url = new StringBuilder(address);

      for (Map.Entry<String, String> param : params.entrySet()) {
        if (!isAmpersand) {
          url.append("?");
          isAmpersand = true;
        } else {
          url.append("&");
        }
        url.append(URLEncoder.encode(param.getKey(), "UTF-8"));
        url.append("=");
        url.append(URLEncoder.encode(param.getValue(), "UTF-8"));
      }
      URI uri = new URI(url.toString());
      verb.setURI(uri);
    }
  }

  /**
   * Add the desired headers to the upcoming request
   *
   * @param verb
   * @param headers
   */
  public static void addHeaders(HttpRequestBase verb, Map<String, String> headers) {
    if (null != verb
            && null != headers
            && !headers.isEmpty()) {
//      headers.entrySet()
//        .stream().
//        forEach((entry) -> {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        verb.setHeader(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * Prepend the given base to the given HTTP element.
   * @param verb
   * @param base 
   */
  public static void addBaseUrl(HttpRequestBase verb, String base) throws Throwable {
    if (null != verb) {
      String address = verb.getURI().toString();
      
      address = appendPath(base, address);
      URI uri = new URI(address);
      verb.setURI(uri);
    }
  }

}
