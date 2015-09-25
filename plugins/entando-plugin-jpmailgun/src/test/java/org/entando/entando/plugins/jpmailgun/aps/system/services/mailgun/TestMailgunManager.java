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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.entando.entando.plugins.jpmailgun.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpmailgun.aps.system.JpMailgunSystemConstants;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.logs.MailgunLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMailgunManager extends ApsPluginBaseTestCase {
  
  private static final Logger _logger = LoggerFactory.getLogger(TestMailgunManager.class);
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    this.init();
  }
  
  protected void init() throws Exception {
    try {
      ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
      this._imailgunManager = (IMailgunManager) this.getService(JpMailgunSystemConstants.MAILGUN_MANAGER);
      assertNotNull(configManager);
    } catch (Exception e) {
      throw e;
    }
  }
  
  public void testMailgun() throws Throwable{
    try {
      this.getConfiguration();
//      this.sendingTestMail();
//      this.sendingMail();
//      this.sendingMultipleSimpleMail();
//      this.sendingBatchWithRecipientVariables();
//      this.gettingLog();
    } catch (Throwable t) {
      throw new ApsSystemException("error sending mail",t);
    }
  }
  
  
  public void getConfiguration() throws Throwable{
    try {
      MailgunConfig conf=this._imailgunManager.getConfig();
      assertNotNull(conf);
      _logger.info("\n\n@@@@ API: "+conf.getApiKey());
      _logger.info("\n\n@@@@ Dominio: "+conf.getDomain());
      _logger.info("\n\n test Configuration ok");
    } catch (Throwable t) {
      throw new ApsSystemException("error sending mail",t);
    }
  }
  
  public void sendingTestMail() throws Throwable{
    try {
      MailgunMailResponse resp= this._imailgunManager.sendTestMail(MAIL_TEST_FROM, MAIL_TEST_TO);
      _logger.info("\n\n@@@@ stato: "+resp.getStatus());
      _logger.info("\n\n@@@@ Id: "+resp.getId());
      assertEquals(resp.getStatus(), MailgunMailResponse.SUCCESS);
      _logger.info("\n\n TestMail ok");
    } catch (Throwable t) {
      throw new ApsSystemException("error sending mail",t);
    }
  }
  
  public void sendingMail() throws Throwable{
    try {
      MailgunMailResponse resp= this._imailgunManager.sendMessage(MAIL_TEST_FROM,"test simple email","testo",MAIL_TEST_TO);
// ok- MailgunMailResponse resp= this._imailgunManager.sendMessage("Test "+MAIL_TEST_FROM,"test simple email","testo",MAIL_TEST_TO);
// ok- MailgunMailResponse resp= this._imailgunManager.sendMessage("Test2 "+"<"+MAIL_TEST_FROM+">","test simple email","testo",MAIL_TEST_TO);
      _logger.info("\n\n@@@@ stato: "+resp.getStatus());
       _logger.info("\n\n@@@@ Id: "+resp.getId());
      assertEquals(resp.getStatus(), MailgunMailResponse.SUCCESS);
    } catch (Throwable t) {
      throw new ApsSystemException("error sending mail",t);
    }
  }
  
  public void sendingMultipleSimpleMail() throws Throwable{
    try {
      Set<String> list= new  HashSet<String>();
      list.add(MAIL_TEST_TO);
     MailgunMailResponse resp= this._imailgunManager.sendSimpleMultipleMessage(MAIL_TEST_FROM, "Subject", "Testo", list);
//      MailgunMailResponse resp= this._imailgunManager.sendSimpleMultipleMessage(MAIL_TEST_FROM, "Subject", "Testo", list,trackingMap);
//ok- MailgunMailResponse resp= this._imailgunManager.sendSimpleMultipleMessage("Name "+MAIL_TEST_FROM, "Subject", "Testo", list);
//ok- MailgunMailResponse resp= this._imailgunManager.sendSimpleMultipleMessage("Name2 "+"<"+MAIL_TEST_FROM+">", "Subject", "Testo", list);
      _logger.info("\n\n@@@@ stato: "+resp.getStatus());
      _logger.info("\n\n@@@@ Id: "+resp.getId());
      assertEquals(resp.getStatus(), MailgunMailResponse.SUCCESS);
    } catch (Throwable t) {
      throw new ApsSystemException("error sending mail",t);
    }
  }
  
  public void sendingBatchWithRecipientVariables() throws Throwable{
    try {
      Map<String, Map<String, Object>>  map=new HashMap<String,Map<String, Object>>();
      Map<String, Object> mapTemp= new HashMap<String, Object>();
      mapTemp.put("first","http://www.google.it");
      mapTemp.put("second","http://www.tiscali.it");
      map.put(MAIL_TEST_TO, mapTemp);
      
      //this disable tracking-opens
      Map<String, Object> trackingMap= new HashMap<String, Object>();
      trackingMap.put("o:tracking-opens", false);
      
      MailgunMailResponse resp= this._imailgunManager.sendBatchWithRecipientVariables(MAIL_TEST_FROM, "Subject", "Trial test, <br> Download: <a href='%recipient.first%'>Incentive</a> <br>  <a href='%recipient.second%'>Tiscali</a>", map,trackingMap);
// MailgunMailResponse resp= this._imailgunManager.sendBatchWithRecipientVariables(MAIL_TEST_FROM, "Subject", "Trial test, <br> Download: <a href='%recipient.first%'>Incentive</a> <br> second content= %recipient.second%", map);
//ok- MailgunMailResponse resp= this._imailgunManager.sendBatchWithRecipientVariables("Test "+MAIL_TEST_FROM, "Subject", "Trial test, <br> Download: <a href='%recipient.first%'>Incentive</a> <br> second content= %recipient.second%", map);
//ok- MailgunMailResponse resp= this._imailgunManager.sendBatchWithRecipientVariables("Test2 "+"<"+MAIL_TEST_FROM+">", "Subject", "Trial test, <br> Download: <a href='%recipient.first%'>Incentive</a> <br> second content= %recipient.second%", map);
      _logger.info("\n\n@@@@ stato: "+resp.getStatus());
      _logger.info("\n\n@@@@ Id: "+resp.getId());
      assertEquals(resp.getStatus(), MailgunMailResponse.SUCCESS);
      _logger.info("ASSERT OK");
      
    } catch (Throwable t) {
      throw new ApsSystemException("error sending mail",t);
    }
  }
  
  public void gettingLog() throws Throwable{
    try {
      MailgunLog log= this._imailgunManager.getLogById("INSERT_ID");
      Set<String> accepted= log.getListAccepted();
      _logger.info("List of email accepted of this message:");
      printList(accepted);
      Set<String> delivered= log.getListDelivered();
      _logger.info("List of email delivered of this message:");
      printList(delivered);
      Set<String> recipients= log.getRecipients();
      _logger.info("List of recipients of this message:");
      printList(recipients);
       Set<String> opened= log.getListOpened();
      _logger.info("List whose opened this message:");
      printList(opened);
       Set<String> click= log.getListClicked();
      _logger.info("List whose clicked link in this message:");
      printList(click);
      _logger.info("\n\n IS DELIVERED to all?:"+log.isDeliveredToAll());
    } catch (Throwable t) {
      throw new ApsSystemException("error getting log",t);
    }
  }
 
   public void printList(Set<String> list){
    for(String a:list){
      _logger.info("\n"+a);
    }
  }
  
  @Override
  protected void tearDown() throws Exception {
    // TODO Auto-generated method stub
    super.tearDown();
  }
  
  public MailGunManager getMailgunManager() {
    return _mailgunManager;
  }
  
  public void setMailgunManager(MailGunManager mailgunManager) {
    this._mailgunManager = mailgunManager;
  }
  
  public IMailgunManager getIMailgunManager() {
    return _imailgunManager;
  }
  
  public void seItMailgunManager(IMailgunManager _mailgunManager) {
    this._imailgunManager = _mailgunManager;
  }
  
  protected MailGunManager _mailgunManager;
  protected IMailgunManager _imailgunManager;
  private static final String MAIL_TEST_FROM="MAIL_TEST_FROM";
  private static final String MAIL_TEST_TO="MAIL_TEST_TO" ;
  
  
}


