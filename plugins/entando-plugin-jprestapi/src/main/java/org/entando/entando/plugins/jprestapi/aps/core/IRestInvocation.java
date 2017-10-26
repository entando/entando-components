/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jprestapi.aps.core;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 *
 * @author entando
 */
public interface IRestInvocation {

  /**
   * Get the returned response
   * @return 
   */
  public HttpResponse getResponse();
  
  /**
   * Get the 
   * @return 
   */
  public HttpRequestBase getRequest();
  
  /**
   * Get the returned response
   * @return 
   */
  public void setResponse(HttpResponse response);
  
  /**
   * Get the 
   * @return 
   */
  public void setRequest(HttpRequestBase request);
  
}
