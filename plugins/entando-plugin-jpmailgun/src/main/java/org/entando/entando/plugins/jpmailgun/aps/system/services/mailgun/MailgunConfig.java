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
package org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun;

/**
 * <?xml version="1.0" encoding="UTF-8"?>
 * <mailgun>
 * <apikey></apikey>
 * <domain></domain>
 * </mailgun>
 * 
 */

/**
 * @author Alberto Piras
 */
public class MailgunConfig {
  
  /**
   * Gets the API_KEY
   * @return API_KEY
   */
  public String getApiKey() {
    return _apiKey;
  }

 /**
   * Sets the API_KEY 
   * @param apiKey
   */
  public void setApiKey(String apiKey) {
    this._apiKey = apiKey;
  }

  /**
   * Gets the Domain Name
   * @return Domain Name
   */
  public String getDomain() {
    return _domain;
  }

  /**
   * Sets the Domain Name
   * @param domain 
   */
  public void setDomain(String domain) {
    this._domain = domain;
  }
  
  /**
   * Get messages URL
   * @return https://api.mailgun.net/v2/"+this.getConfig().getDomain()+"/messages
   */
  public String getAPIMessagesURL() {
    return MAILGUN_BASE_URL + this.getDomain() + "/messages";
  }
  
  /**
   * Get events URL
   * @return https://api.mailgun.net/v2/"+this.getConfig().getDomain()+"/events
   */
  public String getAPIDomainEventsURL() {
    return MAILGUN_BASE_URL + this.getDomain() + "/events";
  }
  
  public static final String MAILGUN_BASE_URL = "https://api.mailgun.net/v2/";
  
  private String _apiKey;
  private String _domain;
  
}
