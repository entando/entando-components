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
package org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.logs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;


/**EXAMPLE JSON of a message sent to a recipient(made of 2 person)
Result: {
  "items": [
    {
      "tags": [], 
      "timestamp": 1401187859.762482, 
      "envelope": {
        "targets": "marioRossi@gmail.com", 
        "sender": "amministrazione@entadevelop.com", 
        "transport": ""
      }, 
      "recipient-domain": "gmail.com", 
      "event": "accepted", 
      "campaigns": [], 
      "user-variables": {}, 
      "flags": {
        "is-authenticated": true, 
        "is-system-test": false, 
        "is-test-mode": false
      }, 
      "message": {
        "headers": {
          "to": "%recipient%", 
          "message-id": "20140527105059.5638.43312@entadevelop.com", 
          "from": "The EntaTEAM <amministrazione@entadevelop.com>", 
          "subject": "Invite"
        }, 
        "attachments": [], 
        "recipients": [
          "marioRossi@gmail.com", 
          "alessioRossi@gmail.com"
        ], 
        "size": 346
      }, 
      "recipient": "marioRossi@gmail.com", 
      "method": "http"
    }, 
    {
      "tags": [], 
      "timestamp": 1401187859.769499, 
      "envelope": {
        "targets": "alessioRossi@gmail.com", 
        "sender": "amministrazione@entadevelop.com", 
        "transport": ""
      }, 
      "recipient-domain": "gmail.com", 
      "event": "accepted", 
      "campaigns": [], 
      "user-variables": {}, 
      "flags": {
        "is-authenticated": true, 
        "is-system-test": false, 
        "is-test-mode": false
      }, 
      "message": {
        "headers": {
          "to": "%recipient%", 
          "message-id": "20140527105059.5638.43312@entadevelop.com", 
          "from": "The EntaTEAM <amministrazione@entadevelop.com>", 
          "subject": "Invite"
        }, 
        "attachments": [], 
        "recipients": [
          "marioRossi@gmail.com", 
          "alessioRossi@gmail.com"
        ], 
        "size": 346
      }, 
      "recipient": "alessioRossi@gmail.com", 
      "method": "http"
    }, 
    {
      "tags": [], 
      "envelope": {
        "sender": "amministrazione@entadevelop.com", 
        "sending-ip": "184.173.153.213", 
        "targets": "alessioRossi@gmail.com", 
        "transport": "smtp"
      }, 
      "delivery-status": {
        "message": "", 
        "code": 0, 
        "description": null, 
        "session-seconds": 0.9301090240478516
      }, 
      "recipient-domain": "gmail.com", 
      "campaigns": [], 
      "user-variables": {}, 
      "flags": {
        "is-authenticated": true, 
        "is-system-test": false, 
        "is-test-mode": false
      }, 
      "timestamp": 1401187860.769353, 
      "message": {
        "headers": {
          "to": "alessioRossi@gmail.com", 
          "message-id": "20140527105059.5638.43312@entadevelop.com", 
          "from": "The EntaTEAM <amministrazione@entadevelop.com>", 
          "subject": "Invite"
        }, 
        "attachments": [], 
        "recipients": [
          "marioRossi@gmail.com", 
          "alessioRossi@gmail.com"
        ], 
        "size": 523
      }, 
      "recipient": "alessioRossi@gmail.com", 
      "event": "delivered"
    }, 
    {
      "tags": [], 
      "envelope": {
        "sender": "amministrazione@entadevelop.com", 
        "sending-ip": "184.173.153.213", 
        "targets": "marioRossi@gmail.com", 
        "transport": "smtp"
      }, 
      "delivery-status": {
        "message": "", 
        "code": 0, 
        "description": null, 
        "session-seconds": 0.9146449565887451
      }, 
      "recipient-domain": "gmail.com", 
      "campaigns": [], 
      "user-variables": {}, 
      "flags": {
        "is-authenticated": true, 
        "is-system-test": false, 
        "is-test-mode": false
      }, 
      "timestamp": 1401187860.813292, 
      "message": {
        "headers": {
          "to": "marioRossi@gmail.com", 
          "message-id": "20140527105059.5638.43312@entadevelop.com", 
          "from": "The EntaTEAM <amministrazione@entadevelop.com>", 
          "subject": "Invite"
        }, 
        "attachments": [], 
        "recipients": [
          "marioRossi@gmail.com", 
          "alessioRossi@gmail.com"
        ], 
        "size": 509
      }, 
      "recipient": "marioRossi@gmail.com", 
      "event": "delivered"
    }
  ], 
  "paging": {
    "next": "https://api.mailgun.net/v2/entadevelop.com/events/W3siYSI6IHRydWUsICJiIjogIjIwMTQtMDUtMjVUMTE6MDE6MjQuNTUwMjE0KzAwOjAwIn0sIHsiYSI6IHRydWUsICJiIjogIjIwMTQtMDUtMjdUMTA6NTE6MDAuODE0MDAwKzAwOjAwIn0sIFsibCIsICJmIl0sIG51bGwsIHsiYWNjb3VudC5pZCI6ICI1MzY0ZTcwZjVhNzYxODA4MDUyODZjY2QiLCAiZG9tYWluLm5hbWUiOiAiZW50YWRldmVsb3AuY29tIiwgInNldmVyaXR5IjogIk5PVCBpbnRlcm5hbCIsICJtZXNzYWdlLmhlYWRlcnMubWVzc2FnZS1pZCI6ICIyMDE0MDUyNzEwNTA1OS41NjM4LjQzMzEyQGVudGFkZXZlbG9wLmNvbSJ9LCA1MCwgIm1lc3NhZ2Ujb1d5N1NxT0hSeVdDQlBraTJCTzJDUSJd", 
    "previous": "https://api.mailgun.net/v2/entadevelop.com/events/W3siYSI6IHRydWUsICJiIjogIjIwMTQtMDUtMjVUMTE6MDE6MjQuNTUwMjE0KzAwOjAwIn0sIHsiYiI6ICIyMDE0LTA1LTI3VDEwOjUwOjU5Ljc2MTAwMCswMDowMCIsICJlIjogIjIwMTQtMDUtMjVUMTE6MDE6MjQuNTQ5MjE0KzAwOjAwIn0sIFsicCIsICJsIiwgImYiXSwgbnVsbCwgeyJhY2NvdW50LmlkIjogIjUzNjRlNzBmNWE3NjE4MDgwNTI4NmNjZCIsICJkb21haW4ubmFtZSI6ICJlbnRhZGV2ZWxvcC5jb20iLCAic2V2ZXJpdHkiOiAiTk9UIGludGVybmFsIiwgIm1lc3NhZ2UuaGVhZGVycy5tZXNzYWdlLWlkIjogIjIwMTQwNTI3MTA1MDU5LjU2MzguNDMzMTJAZW50YWRldmVsb3AuY29tIn0sIDUwLCAibWVzc2FnZSN2UlFUWHdrOVJncWdpMjI2cUo3VXBBIl0="
  }
}
  **/

/**
 *Log of Mailgun's element
 * 
 * @author Alberto Piras
 */
public class MailgunLog {
  
  public MailgunLog(String resp){
   this._completeResponse=resp;
   this._jsonObject= getJSON(resp);
   this._jsonArray = _jsonObject.getJSONArray("items");
  }

  /**
   * Gets recipients of a Mail
   * @return a list of contacts that rapresents  the recipient whose was sent the email
   */
  public Set<String> getRecipients(){
    Set<String> result= new HashSet<String>();
    JSONObject singleObject = new JSONObject(this._jsonArray.get(0).toString());
    JSONObject messageObject = new JSONObject(singleObject.get("message").toString());
    JSONArray recipients= (messageObject.getJSONArray("recipients"));
    for (int i = 0; i < recipients.length(); i++) {
      result.add(recipients.get(i).toString());
		  }
    return result;   
  }
  
  /**
   * @return from of the mail
   */
  public String getFrom(){
    String result= null;
    JSONObject singleObject = new JSONObject(this._jsonArray.get(0).toString());
    JSONObject messageObject = new JSONObject(singleObject.get("message").toString());
    JSONObject headers= new JSONObject(messageObject.get("headers").toString());
    return headers.get("from").toString();
    
  }
  
  /**
   * Gets a list of email in the current stauts
   * @param status 
   * @return A list of mails by they status
   */
  public Set<String> getListByStatus(String status){
    Set<String> result= new HashSet<String>();
    for (int i = 0; i < this._jsonArray.length(); i++) {
      JSONObject singleObject = new JSONObject(this._jsonArray.get(i).toString());
      if(status.equals(singleObject.get("event").toString())){
        result.add(singleObject.get("recipient").toString());
      }
    }
    return result;
  }
  
  /**
   *  Verifies if a message was sent to all recipients
   * @return true if message was sent to all, false otherwise
   */
  public boolean isDeliveredToAll(){
    if(getListByStatus(MailgunStatusConstants.DELIVERED).size()==getRecipients().size())return true;
    else return false;
  }
  
  public JSONObject getJSON(String txt){
    JSONObject object = new JSONObject(txt);
    return object;
  }
 
  public Set<String> getListAccepted() {
    return getListByStatus(MailgunStatusConstants.ACCEPTED);
  }

  public Set<String> getListRejected() {
    return getListByStatus(MailgunStatusConstants.REJECTED);
  }

  public Set<String> getListDelivered() {
  return getListByStatus(MailgunStatusConstants.DELIVERED);
  }

  public Set<String> getListFailed() {
   return getListByStatus(MailgunStatusConstants.FAILED);
  }

  public Set<String> getListOpened() {
    return getListByStatus(MailgunStatusConstants.OPENED);
  }

  public Set<String> getListClicked() {
   return getListByStatus(MailgunStatusConstants.CLICKED);
  }

  public Set<String> getListUnsubscribed() {
   return getListByStatus(MailgunStatusConstants.UNSUBSCRIBED);
  }

  public Set<String> getListcCmplained() {
    return getListByStatus(MailgunStatusConstants.COMPLAINED);
  }

  public Set<String> getListStored() {
    return getListByStatus(MailgunStatusConstants.STORED);
  }
  
  public String getCompleteResponse() {
    return _completeResponse;
  }

  public void setCompleteResponse(String _completeResponse) {
    this._completeResponse = _completeResponse;
  }
   
  private JSONObject _jsonObject;
  private JSONArray _jsonArray;
  public String _completeResponse;
  public static final String RECIPIENTS = "recipients";	
  
}
