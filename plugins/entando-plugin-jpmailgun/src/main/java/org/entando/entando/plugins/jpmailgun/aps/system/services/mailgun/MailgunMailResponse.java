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
import com.sun.jersey.api.client.ClientResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Response of Mailgun methods
 * 
 * @author Alberto Piras
 **/
public class MailgunMailResponse {

	private static final Logger _logger = LoggerFactory.getLogger(MailGunManager.class);

	/**
	 * @param resp ClientResponse
	 * @throws ApsSystemException
	 */
	public MailgunMailResponse(ClientResponse resp) throws ApsSystemException{
		String response=printRequest(resp);
		this._completeJSONMessage = response;
		JSONObject singleObject = new JSONObject(response);
		this.setMessage(getJsonAttribute(singleObject, JSON_MESSAGE));
		String tmpId=getJsonAttribute(singleObject,JSON_ID);
		this.setId(removeCharactersID(tmpId));
		this.setStatus(resp.getStatus());
	}

	private String removeCharactersID(String word){
		String temp= new StringBuffer(word).deleteCharAt(word.length()-1).toString();
		String result= new StringBuffer(temp).deleteCharAt(0).toString();
		return result;
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

	public String getJsonAttribute(JSONObject object,String value){
		return object.getString(value);
	}

	public String getMessage() {
		return _message;
	}

	public void setMessage(String _message) {
		this._message = _message;
	}

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	/**
	 * @return the complete JSON
	 */
	public String getCompleteJSONMessage() {
		return _completeJSONMessage;
	}

	public void setCompleteJSONMessage(String _completeJSONMessage) {
		this._completeJSONMessage = _completeJSONMessage;
	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int _status) {
		this._status = _status;
	}

	private String _id;
	private String _message;
	private String _completeJSONMessage;
	private int _status;

	private static final String JSON_MESSAGE = "message";
	private static final String JSON_ID = "id";
	
	public static final int SUCCESS = 200;
	public static final int BAD_REQUEST = 400;
	public static final int NOT_AUTHORIZED = 401;
	public static final int REQUEST_FAILED = 402;
	public static final int WRONG_REQUEST = 404;
	public static final int SERVER_ERROR_0 = 500;
	public static final int SERVER_ERROR_2 = 502;
	public static final int SERVER_ERROR_3 = 503;
	public static final int SERVER_ERROR_4 = 504;
}


