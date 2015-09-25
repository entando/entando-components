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

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Map;
import java.util.Set;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.logs.*;

/**
 * @author Alberto Piras
 */
public interface IMailgunManager {
  
  /**
   *send a test mail
   * @param from this param must have a valid domain
   * @param to
   * @return the Object Response of sending an email
   * @throws com.agiletec.aps.system.exception.ApsSystemException
   */
  public MailgunMailResponse	sendTestMail(String from, String to)throws  ApsSystemException;
  
  /**
   * Sends a Simple Message to a recipient,  tracking default is true 
   * @param from a valid email address registered on mailgun
   * @param subject the subject of the email
   * @param text the body of the email
   * @param to the recipient
   * @return {@link MailgunMailResponse}
   * @throws ApsSystemException if an error occurs
   */
  public MailgunMailResponse sendMessage(String from, String subject, String text, String to) throws ApsSystemException;
  
    /**
   * Sends a Simple Message to a recipient,  tracking default is true 
   * @param from a valid email address registered on mailgun
   * @param subject the subject of the email
   * @param text the body of the email
   * @param to the recipient
   * @param trackingValues map to override tracking options  
   * @return {@link MailgunMailResponse}
   * @throws ApsSystemException if an error occurs
   */
  public MailgunMailResponse sendMessage(String from, String subject, String text, String to, Map<String, Object> trackingValues) throws ApsSystemException;
  
  
  
  
  /**
   * Sends a Message to different emails, tracking default is true 
   * @param from a valid email address registered on mailgun
   * @param subject the subject of the email
   * @param text the body of the email
   * @param list a list of address. Accepts simple addresses like: name@domain.com and pretty addresses like: Jack jackbower@domain.com;
   * @return {@link MailgunMailResponse}
   * @throws com.agiletec.aps.system.exception.ApsSystemException
   */
  public MailgunMailResponse sendSimpleMultipleMessage(String from, String subject, String text, Set<String> list) throws ApsSystemException;
  
  
  
    /**
   * Sends a Message to different emails, tracking default is true 
   * @param from a valid email address registered on mailgun
   * @param subject the subject of the email
   * @param text the body of the email
   * @param list a list of address. Accepts simple addresses like: name@domain.com and pretty addresses like: Jack jackbower@domain.com;
   * @param trackingValues map to override tracking options  
   * @return {@link MailgunMailResponse}
   * @throws com.agiletec.aps.system.exception.ApsSystemException
   */
  public MailgunMailResponse sendSimpleMultipleMessage(String from, String subject, String text, Set<String> list, Map<String, Object> trackingValues) throws ApsSystemException;
  
  
  
  /**
   **Sends a Message to different emails, tracking default is true 
   * @param from mail from
   * @param subject subject of the email
   * @param body content of the email
   * @param recipientVariables  Map<String, Map<String, Object>>  that contains variables of each recipient
   * @return MailgunMailResponse
   * @throws ApsSystemException
   */
  public MailgunMailResponse sendBatchWithRecipientVariables(String from, String subject, String body, Map<String, Map<String, Object>> recipientVariables) throws ApsSystemException;
  
   /**
   **Sends a Message to different emails, tracking default is true 
   * @param from mail from
   * @param subject subject of the email
   * @param body content of the email
   * @param recipientVariables  Map<String, Map<String, Object>>  that contains variables of each recipient
   * @param trackingValues map to override tracking options  
   * @return MailgunMailResponse
   * @throws ApsSystemException
   */
  public MailgunMailResponse sendBatchWithRecipientVariables(String from, String subject, String body, Map<String, Map<String, Object>> recipientVariables, Map<String, Object> trackingValues) throws ApsSystemException;
  
  /**
   * gets Logs of an element by its ID
   * @param id element's identifier
   * @return the Object Log
   * @throws com.agiletec.aps.system.exception.ApsSystemException
   */
  public MailgunLog	getLogById(String id)throws ApsSystemException;
  
  /**
   * returns the configuration
   * @return the configuration of jpmailgun
   */
  public MailgunConfig getConfig();
  
  /**
   * update the configuration
   * @param newConfig
   * @throws ApsSystemException
   */
  public void updateConfig(MailgunConfig newConfig) throws ApsSystemException;
}
