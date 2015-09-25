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
package org.entando.entando.plugins.jpmailgun.apsadmin.mailgun;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailGunManager;
import org.json.JSONArray;
import org.json.JSONObject;

import com.agiletec.apsadmin.system.BaseAction;
import com.sun.jersey.api.client.ClientResponse;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailgunConfig;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailgunMailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alberto Piras
 */
public class MailgunAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(MailgunAction.class);

	/**
	 * Creates a string contaning the entity of a ClientResponse
	 *
	 * @param response A ClientResponse
	 * @return String Entity
	 * @throws ApsSystemException
	 */
	public String printRequest(ClientResponse response) throws ApsSystemException {
		String body = null;
		try {
			body = IOUtils.toString(response.getEntityInputStream());
		} catch (Throwable t) {
			_logger.error("Error printing Request of ClientResponse", t);
			throw new ApsSystemException("Error printing Request of ClientResponse", t);
		}
		return body;
	}

	/**
	 * Gets a jsonArray from mailgun json response
	 *
	 * @param jsonBody
	 * @return jsonArray
	 */
	public JSONArray getJSONArray(String jsonBody) {
		JSONObject o = new JSONObject(jsonBody);
		JSONArray arrayJSON = o.getJSONArray("items");
		return arrayJSON;
	}

	/**
	 * Transforms the state into string
	 *
	 * @param resp ClientResponse
	 * @return state
	 */
	public String controlStatus(ClientResponse resp) {
		switch (resp.getStatus()) {
			case 200:
				return "success";
			case 400:
				return "badRequest";
			case 401:
				return "nonAuthorized";
			case 402:
				return "requestFailed";
			case 404:
				return "wrongRequest";
			default:
				return "serverError";
		}
	}

	/**
	 * Saves MailgunCOnfiguration
	 *
	 * @return success or error
	 */
	public String saveConfiguration() {
		MailgunConfig a = new MailgunConfig();
		a.setApiKey(_updateAPIKey);
		a.setDomain(_updateDomain);
		try {
			this.getMailgunManager().updateConfig(a);
		} catch (Throwable t) {
			_logger.error("Error updating credentials", t);
			_messageErrorDB = "Error updating credentials";
			return "error";
		}
		return SUCCESS;
	}

	/**
	 * Sends a Test Mail to verify settings
	 *
	 * @return success or error
	 */
	public String sendSimpleTestMail() {
		try {
			MailgunMailResponse response = this.getMailgunManager().sendTestMail(_emailFrom, _emailTo);
			_logger.info("\n\n Result:" + response.getCompleteJSONMessage() + "\n" + response.getId() + "\n" + response.getMessage() + "\n Status: " + response.getStatus());
			_messageSuccess = "Message successfully sent";
		} catch (Throwable t) {
			_messageError = "Insert correct values and verify your connection";
			_logger.error("\n\nError sending message", t);
			return "error";
		}
		return SUCCESS;
	}

	/**
	 * Gets current Domains
	 *
	 * @return
	 * @throws ApsSystemException
	 */
	public List<String> getDomains() throws ApsSystemException {
		List<String> result = new ArrayList<String>();
		try {
			ClientResponse resp = this.getMailgunManager().getDomains();
			JSONArray array = getJSONArray(printRequest(resp));
			for (int i = 0; i < array.length(); i++) {
				JSONObject singleObject = new JSONObject(array.get(i).toString());
				result.add(singleObject.getString("name"));
			}
		} catch (Throwable t) {
			_logger.error("Error getting domains", t);
			throw new ApsSystemException("Error getting domains", t);
		}
		return result;
	}
	
	protected MailGunManager getMailgunManager() {
		return _mailgunManager;
	}
	public void setMailgunManager(MailGunManager mailgunManager) {
		this._mailgunManager = mailgunManager;
	}

	public String getAPI() {
		return this.getMailgunManager().getConfig().getApiKey();
	}

	public String getDomain() {
		return this.getMailgunManager().getConfig().getDomain();
	}

	public String getUpdateAPIKey() {
		return _updateAPIKey;
	}
	public void setUpdateAPIKey(String _updateAPIKey) {
		this._updateAPIKey = _updateAPIKey;
	}

	public String getUpdateDomain() {
		return _updateDomain;
	}
	public void setUpdateDomain(String _updateDomain) {
		this._updateDomain = _updateDomain;
	}

	public String getEmailFrom() {
		return _emailFrom;
	}
	public void setEmailFrom(String _emailFrom) {
		this._emailFrom = _emailFrom;
	}

	public String getEmailTo() {
		return _emailTo;
	}
	public void setEmailTo(String _emailTo) {
		this._emailTo = _emailTo;
	}

	public String getMessageError() {
		return _messageError;
	}
	public void setMessageError(String _messageError) {
		this._messageError = _messageError;
	}

	public String getMessageSuccess() {
		return _messageSuccess;
	}
	public void setMessageSuccess(String _messageSuccess) {
		this._messageSuccess = _messageSuccess;
	}

	public String getMessageErrorDB() {
		return _messageErrorDB;
	}
	public void setMessageErrorDB(String _messageErrorDB) {
		this._messageErrorDB = _messageErrorDB;
	}

	public String _emailTo;
	private MailGunManager _mailgunManager;
	public String _updateDomain;
	public String _emailFrom;
	public String _updateAPIKey;
	public String _messageError;
	public String _messageSuccess;
	public String _messageErrorDB;

}
