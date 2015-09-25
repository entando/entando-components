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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.entando.entando.plugins.jpmailgun.aps.system.JpMailgunSystemConstants;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.logs.MailgunLog;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.parse.MailgunConfigDOM;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alberto Piras
 */
public class MailGunManager extends AbstractService implements IMailgunManager {
  
  private static final Logger _logger = LoggerFactory.getLogger(MailGunManager.class);
  
  @Override
  public void init() throws Exception {
    this.loadConfig();
    _logger.debug("{}: initialized ", this.getClass().getName());
  }
  
  /**
   * Loads Mailgun settings from DB
   * @throws ApsSystemException
   */
  protected void loadConfig() throws ApsSystemException {
    try{
      String xmlConfig = this.getConfigManager().getConfigItem(JpMailgunSystemConstants.CONFIG_ITEM_MAILGUN);
      MailgunConfig config = this.parseXML(xmlConfig);
      this.setConfig(config);
    } catch (Throwable t) {
      _logger.error("Error loading Config");
      throw new ApsSystemException("Error loading Config", t);
    }
  }
  
  /**
   * Transforms XML into MailgunConfig Object
   * @param xmlConfig The xml to parse
   * @return the MailgunConfig by the xml stored in DB
   * @throws ApsSystemException
   */
  private MailgunConfig parseXML(String xmlConfig) throws ApsSystemException {
    MailgunConfig config=null;
    try{
      MailgunConfigDOM dom=new MailgunConfigDOM();
      config=dom.extractConfig(xmlConfig);
    } catch (Throwable t) {
      _logger.error("Error parsing XML {}",xmlConfig);
      throw new ApsSystemException("Error parsing XML", t);
    }
    return config;
  }
  
  /**
   * Changes an Object MailgunConfig into XML format
   * @param newConfig The MailgunConfig to convert
   * @return The xml of the MailgunConfig to store in DB
   * @throws ApsSystemException
   */
  private String buildXml(MailgunConfig newConfig) throws ApsSystemException {
    String result=null;
    try{
      MailgunConfigDOM dom=new MailgunConfigDOM();
      result = dom.createConfigXml(newConfig);
    } catch (Throwable t) {
      _logger.error("Error building XML {}",newConfig);
      throw new ApsSystemException("Error building XML", t);
    }
    return result;
  }
  
  /**
   * Updates the Mailgun config
   * @param newConfig The new config to update
   * @throws ApsSystemException
   */
  @Override
  public void updateConfig(MailgunConfig newConfig) throws ApsSystemException {
    try{
      String xml = this.buildXml(newConfig);
      this.getConfigManager().updateConfigItem(JpMailgunSystemConstants.CONFIG_ITEM_MAILGUN, xml);
      this.setConfig(newConfig);
    } catch (Throwable t) {
      _logger.error("Error updating Mailgun Config {}",newConfig);
      throw new ApsSystemException("Error updating Mailgun Config", t);
    }
  }
  
  /**
   * Sends a test email to verify settings' Mailgun
   * @param from this has to be made with a correct domain
   * @param to the recipient
   * @return MailgunMailResponse
   * @throws ApsSystemException
   */
  @Override
  public MailgunMailResponse	sendTestMail(String from, String to)throws  ApsSystemException{
    if (!this.isActive()) {
      _logger.info("The mailgun service is not active!: sendSimpleSingleMessage from:{} to:{} of the following email:{} - {}", from, to);
      return null;
    }
    MailgunMailResponse response = null;
    try {
      Client client = Client.create();
      client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
      WebResource webResource = client.resource(this.getConfig().getAPIMessagesURL());
      MultivaluedMapImpl formData = new MultivaluedMapImpl();
      formData.add("from",from);
      formData.add("to", to);
      formData.add("subject", "Mailgun Settings");
      formData.add("text", "Domain and APIKEY are correctly configured");
      ClientResponse resp= webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
      if(resp!=null)
        response= new MailgunMailResponse(resp);
    } catch (Throwable t) {
      _logger.error("Error sending Test message from:{} to:{}",from, to);
      throw new ApsSystemException("Error sending Test message from", t);
    }
    return response;
  }
  
  /**
   * Sends a Simple Message to a recipient, tracking default is true 
   * @param from a valid email address registered on mailgun
   * @param subject the subject of the email
   * @param text the body of the email
   * @param to the recipient
   * @return {@link MailgunMailResponse}
   * @throws ApsSystemException if an error occurs
   */
  @Override
  public MailgunMailResponse sendMessage(String from, String subject, String text, String to) throws ApsSystemException{
    if (!this.isActive()) {
      _logger.info("The mailgun service is not active!: sendSimpleSingleMessage from:{} to:{} of the following email:{} - {}", from, to, subject, text);
      return null;
    }
    MailgunMailResponse response = null;
    try {
      Client client = Client.create();
      client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
      WebResource webResource =client.resource(this.getConfig().getAPIMessagesURL());
      MultivaluedMapImpl formData = new MultivaluedMapImpl();
      formData.add("from", from);
      formData.add("to", to);
      formData.add("subject", subject);
      formData.add("html", text);
      ClientResponse resp=webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
      response= new MailgunMailResponse(resp);
    } catch (Throwable t) {
      _logger.error("Error sending simple message from:{} to:{} subject:{}",from, to,subject);
      throw new ApsSystemException("Error in send Simple Single Message", t);
    }
    return response;
  }
  
  
  /**
   * Sends a Simple Message to a  recipient, tracking default is true 
   * @param from a valid email address registered on mailgun
   * @param subject the subject of the email
   * @param text the body of the email
   * @param to the recipient
    * @param trackingValues map to override tracking options 
   * @return {@link MailgunMailResponse}
   * @throws ApsSystemException if an error occurs
   */
  @Override
  public MailgunMailResponse sendMessage(String from, String subject, String text, String to, Map<String, Object> trackingValues) throws ApsSystemException{
    if (!this.isActive()) {
      _logger.info("The mailgun service is not active!: sendSimpleSingleMessage from:{} to:{} of the following email:{} - {}", from, to, subject, text);
      return null;
    }
    MailgunMailResponse response = null;
    try {
      Client client = Client.create();
      client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
      WebResource webResource =client.resource(this.getConfig().getAPIMessagesURL());
      MultivaluedMapImpl formData = new MultivaluedMapImpl();
      initializeCustomizedMapValues(formData,trackingValues);
      formData.add("from", from);
      formData.add("to", to);
      formData.add("subject", subject);
      formData.add("html", text);
      ClientResponse resp=webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
      response= new MailgunMailResponse(resp);
    } catch (Throwable t) {
      _logger.error("Error sending simple message from:{} to:{} subject:{}",from, to,subject);
      throw new ApsSystemException("Error in send Simple Single Message", t);
    }
    return response;
  }
  
  
  
  /**
   *Sends a Simple Message to a group of recipients, tracking default is true 
   * @param from mail from
   * @param subject subject of the email
   * @param text content of the email
   * @param list list of emails to send the email
   * @return MailgunMailResponse
   * @throws ApsSystemException
   */
  @Override
  public MailgunMailResponse sendSimpleMultipleMessage(String from, String subject, String text, Set<String> list) throws ApsSystemException {
    if (!this.isActive()) {
      _logger.info("The mailgun service is not active!: sendSimpleSingleMessage from:{} to:{} of the following email:{} - {}", from, subject, text);
      return null;
    }
    MailgunMailResponse response = null;
    try {
      Client client = Client.create();
      client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
      WebResource webResource =client.resource(this.getConfig().getAPIMessagesURL());
      MultivaluedMapImpl formData = new MultivaluedMapImpl();
      formData.add("from", from);
      JSONObject ogg= new JSONObject();
      for(String temp: list){
        ogg.append(temp, "{}");
        formData.add("to", temp);
      }
      formData.add("subject", subject);
      formData.add("html", text);
      formData.add("recipient-variables", ogg.toString());
      ClientResponse resp=webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
      response= new MailgunMailResponse(resp);
    } catch (Throwable t) {
      _logger.error("Error sending message from:{} to:{} subject:{}",from, StringUtils.join(list, ","), subject, t);
      throw new ApsSystemException("Error in sendSimpleMultipleMessage", t);
    }
    return response;
  }
  
  
  /**
   *Sends a Simple Message to a group of recipients, tracking default is true 
   * @param from mail from
   * @param subject subject of the email
   * @param text content of the email
   * @param list list of emails to send the email
   * @param trackingValues map to override tracking options 
   * @return MailgunMailResponse
   * @throws ApsSystemException
   */
  @Override
  public MailgunMailResponse sendSimpleMultipleMessage(String from, String subject, String text, Set<String> list, Map<String, Object> trackingValues) throws ApsSystemException {
    if (!this.isActive()) {
      _logger.info("The mailgun service is not active!: sendSimpleSingleMessage from:{} to:{} of the following email:{} - {}", from, subject, text);
      return null;
    }
    MailgunMailResponse response = null;
    try {
      Client client = Client.create();
      client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
      WebResource webResource =client.resource(this.getConfig().getAPIMessagesURL());
      MultivaluedMapImpl formData = new MultivaluedMapImpl();
      initializeCustomizedMapValues(formData,trackingValues);
      formData.add("from", from);
      JSONObject ogg= new JSONObject();
      for(String temp: list){
        ogg.append(temp, "{}");
        formData.add("to", temp);
      }
      formData.add("subject", subject);
      formData.add("html", text);
      formData.add("recipient-variables", ogg.toString());
      ClientResponse resp=webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
      response= new MailgunMailResponse(resp);
    } catch (Throwable t) {
      _logger.error("Error sending message from:{} to:{} subject:{}",from, StringUtils.join(list, ","), subject, t);
      throw new ApsSystemException("Error in sendSimpleMultipleMessage", t);
    }
    return response;
  }
  
  /**
   * Sends a Message to a group of recipients, tracking default is true 
   * @param from mail from
   * @param subject subject of the email
   * @param body content of the email
   * @param recipientVariables  Map<String, Map<String, Object>>  that contains variables of each recipient
   * @return MailgunMailResponse
   * @throws ApsSystemException
   */
  @SuppressWarnings("rawtypes")
  @Override
  public MailgunMailResponse sendBatchWithRecipientVariables(String from, String subject, String body, Map<String, Map<String, Object>> recipientVariables) throws ApsSystemException {
    if (!this.isActive()) {
      _logger.info("The mailgun service is not active!: sendSimpleSingleMessage from:{} to:{} of the following email:{} - {}",from, subject);
      return null;
    }
    MailgunMailResponse response = null;
    try {
      Client client = Client.create();
      client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
      WebResource webResource =client.resource(this.getConfig().getAPIMessagesURL());
      MultivaluedMapImpl formData = new MultivaluedMapImpl();
      formData.add("from", from);
      Iterator it = recipientVariables.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry pairs = (Map.Entry)it.next();
        formData.add("to",pairs.getKey());
      }
      formData.add("subject", subject);
      formData.add("html", body);
      String recipientVars = new JSONObject(recipientVariables).toString();
      formData.add("recipient-variables", recipientVars);
      ClientResponse resp=webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
      response= new MailgunMailResponse(resp);
      System.out.println("RESPONSE:"+response.getStatus());
    } catch (Throwable t) {
      _logger.error("Error sending message from:{} to:{} subject:{}",from, subject, t);
      throw new ApsSystemException("Error in sendSimpleMultipleMessage", t);
    }
    return response;
  }
  
  
  
  
  /**
   * Sends a Message to a group of recipients, tracking default is true 
   * @param from mail from
   * @param subject subject of the email
   * @param body content of the email
   * @param recipientVariables  Map<String, Map<String, Object>>  that contains variables of each recipient
   * @param trackingValues map to override tracking options  
   * @return MailgunMailResponse
   * @throws ApsSystemException
   */
  @SuppressWarnings("rawtypes")
  @Override
  public MailgunMailResponse sendBatchWithRecipientVariables(String from, String subject, String body, Map<String, Map<String, Object>> recipientVariables, Map<String, Object> trackingValues) throws ApsSystemException {
    if (!this.isActive()) {
      _logger.info("The mailgun service is not active!: sendSimpleSingleMessage from:{} to:{} of the following email:{} - {}",from, subject);
      return null;
    }
    MailgunMailResponse response = null;
    try {
      Client client = Client.create();
      client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
      WebResource webResource =client.resource(this.getConfig().getAPIMessagesURL());
      MultivaluedMapImpl formData = new MultivaluedMapImpl();
      initializeCustomizedMapValues(formData,trackingValues);
      formData.add("from", from);
      Iterator it = recipientVariables.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry pairs = (Map.Entry)it.next();
        formData.add("to",pairs.getKey());
      }
      formData.add("subject", subject);
      formData.add("html", body);
      String recipientVars = new JSONObject(recipientVariables).toString();
      formData.add("recipient-variables", recipientVars);
      ClientResponse resp=webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
      response= new MailgunMailResponse(resp);
      System.out.println("RESPONSE:"+response.getStatus());
    } catch (Throwable t) {
      _logger.error("Error sending message from:{} to:{} subject:{}",from, subject, t);
      throw new ApsSystemException("Error in sendSimpleMultipleMessage", t);
    }
    return response;
  }
  
    private void initializeCustomizedMapValues(MultivaluedMapImpl formData,Map<String, Object> map){
    Iterator iteratorMap = map.entrySet().iterator();
    while (iteratorMap.hasNext()) {
      Map.Entry pairs = (Map.Entry)iteratorMap.next();
      formData.add(pairs.getKey().toString(), pairs.getValue());
    }
  }
  
  /**
   * Gets Logs of an element by its own ID
   * @param id message identifier
   * @return  MailgunMailLog
   * @throws ApsSystemException
   */
  @Override
  public MailgunLog	getLogById(String id) throws ApsSystemException{
    MailgunLog log =null;
    try{
      Client client = new Client();
      client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
      WebResource webResource =client.resource(this.getConfig().getAPIDomainEventsURL());
      MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
      queryParams.add("begin", 50);
      queryParams.add("ascending", "yes");;
      queryParams.add("pretty", "yes");
      queryParams.add("message-id", id);
      ClientResponse resp= webResource.queryParams(queryParams).get(ClientResponse.class);
      log = new MailgunLog(printRequest(resp));
    }catch(Throwable t){
      _logger.error("Error getting a log by Id{}",id);
      throw new ApsSystemException("Error getting a log by Id",t);
    }
    return log;
  }
  
  /**
   * Creates a string contaning the entity of a ClientResponse
   * @param response A ClientResponse
   * @return String Entity
   * @throws ApsSystemException
   */
  public String printRequest(ClientResponse response) throws ApsSystemException{
    String body=null;
    try{
      body=IOUtils.toString(response.getEntityInputStream());
    }catch(Throwable t){
      _logger.error("Error printing Request of ClientResponse");
      throw new ApsSystemException("Error printing Request of ClientResponse",t);
    }
    return body;
  }
  
  /**
   * Verifies a specific message status, TO USE WITH A MESSAGE SENT TO A ONE RECIPIENT
   * @param idMessage message id
   * @param status status to search
   * @return true if that message is in that status, false otherwise
   */
  public  boolean verifyStatusOfAMessage(String idMessage,String status) throws ApsSystemException{
    Client client = new Client();
    client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
    WebResource webResource =client.resource(this.getConfig().getAPIDomainEventsURL());
    MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
    queryParams.add("pretty", "yes");
    queryParams.add("message-id", idMessage);
    queryParams.add("event", status);
    ClientResponse resp=webResource.queryParams(queryParams).get(ClientResponse.class);
    return verifyEventByJSON(resp);
  }
  
  /**
   * Verifies  if JSON contains or not the results (every type of results)  *
   * @param response The response of a query
   * @return false if the response is void(there is not any items ), true otherwise
   */
  public boolean verifyEventByJSON(ClientResponse response) throws ApsSystemException{
    try{
      String body=printRequest(response);
      JSONObject o = new JSONObject(body);
      JSONArray arrayOfTests =o.getJSONArray("items");
      if(arrayOfTests.length()>0) return true;
      else return false;
    }catch(Throwable t){
      _logger.error("Error verifying Event By JSON");
      throw new ApsSystemException("Error verifying Event By JSON",t);
    }
  }
  
  /**
   *Creates a new Domain
   * @param domainName
   * @param password
   * @return ClientResponse
   */
  public  ClientResponse addDomain(String domainName, String password) {
    Client client = new Client();
    client.addFilter(new HTTPBasicAuthFilter("api", this.getConfig().getApiKey()));
    WebResource webResource =client.resource("https://api.mailgun.net/v2/domains");
    MultivaluedMapImpl formData = new MultivaluedMapImpl();
    formData.add("name", domainName);
    formData.add("smtp_password", password);
    return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
  }
  
  /**
   *Gets lasts 10 Domains
   * @return ClientResponse
   */
  public ClientResponse getDomains() {
    Client client = Client.create();
    client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
    WebResource webResource =client.resource("https://api.mailgun.net/v2/domains");
    MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
    queryParams.add("skip", 0);
    queryParams.add("limit", 10);
    return webResource.queryParams(queryParams).get(ClientResponse.class);
  }
  
  /**
   *Removes a Domain
   * @param domainName
   * @return ClientResponse
   */
  public  ClientResponse deleteDomain(String domainName) {
    Client client = Client.create();
    client.addFilter(new HTTPBasicAuthFilter("api",this.getConfig().getApiKey()));
    WebResource webResource =client.resource("https://api.mailgun.net/v2/domains/"+domainName);
    return webResource.delete(ClientResponse.class);
  }
  
  public void setConfig(MailgunConfig config) {
    this._config = config;
  }
  
  /**
   * Gets Mailgun configuration
   * @return MailgunConfig
   */
  @Override
  public MailgunConfig getConfig() {
    try {
      loadConfig();
    } catch (ApsSystemException ex) {
      //TO DO:
      //	java.util.logging.Logger.getLogger(MailGunManager.class.getName()).log(Level.SEVERE, null, ex);
      _logger.error("Exception: "+ex);
    }
    return _config;
  }
  
  /**
   * @return true fi statusof Mailgun is Active, false otherwise
   */
  public boolean isActive() {
    return _active;
  }
  
  private  MailgunConfigDOM getConfigDOM() {
    return _configDOM;
  }
  
  public void setConfigDOM(MailgunConfigDOM _configDOM) {
    this._configDOM = _configDOM;
  }
  
  public ConfigInterface getConfigManager() {
    return _configManager;
  }
  
  public void setConfigManager(ConfigInterface _configManager) {
    this._configManager = _configManager;
  }
  
  public void setActive(boolean active) {
    this._active = active;
  }
  
  private void _logger(String string) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  private MailgunConfigDOM _configDOM;
  private MailgunConfig _config;
  private ConfigInterface _configManager;
  private boolean _active = true;
  


}