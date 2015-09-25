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
package org.entando.entando.plugins.jpmailgun.apsadmin.mailgun.domain;

import org.entando.entando.plugins.jpmailgun.apsadmin.mailgun.MailgunAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alberto Piras
 */
public class MailgunDomain extends MailgunAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(MailgunDomain.class);

	/**
	 * Delete a domain.
	 * @return String of status
	 */
	public String deleteDomain() {
		try {
			this.getMailgunManager().deleteDomain(_domainId);
		} catch (Exception e) {
			_logger.error("Error on deleteDomain method", e);
			return "error";
		}
		return SUCCESS;
	}
	
	/**
	 * Create a Domain
	 * @return String of status
	 */
	public String createDomain() {
		try {
			this.getMailgunManager().addDomain(_domainId, _domainPassSMTP);
		} catch (Exception e) {
			_logger.error("Error on createDomain method", e);
			return "errorDomain";
		}
		return SUCCESS;
	}
	
	public String getDomainId() {
		return _domainId;
	}
	public void setDomainId(String _domainId) {
		this._domainId = _domainId;
	}

	public String getDomainPassSMTP() {
		return _domainPassSMTP;
	}
	public void setDomainPassSMTP(String _domainPassSMTP) {
		this._domainPassSMTP = _domainPassSMTP;
	}
	
	public String _domainPassSMTP;
	public String _domainId;
	
}
