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
package com.agiletec.plugins.jpmail.aps.services.mail;

import java.util.Properties;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Basic interface for the manager providing email sending functions.
 * 
 * @version 1.0
 * @author E.Mezzano
 *
 */
public interface IMailManager {
	
	/**
	 * Returns the configuration of the mail service.
	 * @return The configuration of the mail service.
	 * @throws ApsSystemException In case of exception reading the configuration.
	 */
	public MailConfig getMailConfig() throws ApsSystemException;
	
	/**
	 * Updates the configuration of the mail service.
	 * @param config The configuration of the mail service.
	 * @throws ApsSystemException In case of exception updating the configuration.
	 */
	public void updateMailConfig(MailConfig config) throws ApsSystemException;
	
	/**
	 * Send an e-mail with 'text/plain' contentType and 'charset=utf-8' encoding.
	 * @param text The e-mail text.
	 * @param subject The e-mail subject.
	 * @param recipientsTo The e-mail main destination addresses.
	 * @param recipientsCc The e-mail 'carbon-copy' destination addresses.
	 * @param recipientsBcc The e-mail 'blind carbon-copy' destination addresses.
	 * @param senderCode The sender code, as configured in the service configuration.
	 * @return The send result.
	 * @throws ApsSystemException In case of errors.
	 */
	public boolean sendMail(String text, String subject, String[] recipientsTo, 
			String[] recipientsCc, String[] recipientsBcc, String senderCode) throws ApsSystemException;
	
	/**
	 * Send an e-mail with the given contentType and 'charset=utf-8' encoding.
	 * @param text The e-mail text.
	 * @param subject The e-mail subject.
	 * @param recipientsTo The e-mail main destination addresses.
	 * @param recipientsCc The e-mail 'carbon-copy' destination addresses.
	 * @param recipientsBcc The e-mail 'blind carbon-copy' destination addresses.
	 * @param senderCode The sender code, as configured in the service configuration.
	 * @param contentType The mail content type, usually one between {@link #CONTENTTYPE_TEXT_PLAIN} and {@link #CONTENTTYPE_TEXT_HTML}.
	 * @return The send result.
	 * @throws ApsSystemException In case of errors.
	 */
	public boolean sendMail(String text, String subject, String[] recipientsTo, String[] recipientsCc, 
			String[] recipientsBcc, String senderCode, String contentType) throws ApsSystemException;
	
	/**
	 * Send an e-mail with attachments with the given contentType and 'charset=utf-8' encoding.
	 * @param text The e-mail text.
	 * @param subject The e-mail subject.
	 * @param contentType The mail content type, usually one between {@link #CONTENTTYPE_TEXT_PLAIN} and {@link #CONTENTTYPE_TEXT_HTML}.
	 * @param attachmentFiles The attachments mapped as fileName/filePath.
	 * @param recipientsTo The e-mail main destination addresses.
	 * @param recipientsCc The e-mail 'carbon-copy' destination addresses.
	 * @param recipientsBcc The e-mail 'blind carbon-copy' destination addresses.
	 * @param senderCode The sender code, as configured in the service configuration.
	 * @return The send result.
	 * @throws ApsSystemException In case of errors.
	 */
	public boolean sendMail(String text, String subject, String contentType, Properties attachmentFiles, String[] recipientsTo, 
			String[] recipientsCc, String[] recipientsBcc, String senderCode) throws ApsSystemException;
	
	/**
	 * Send a e-mail with both simple and html text and, if necessary, attachments.
	 * @param simpleText The text of mail part in text/plain contentType.
	 * @param htmlText The text of mail part in text/html contentType.
	 * @param subject The e-mail subject.
	 * @param attachmentFiles The attachments mapped as fileName/filePath.
	 * @param recipientsTo The e-mail main destination addresses.
	 * @param recipientsCc The e-mail 'carbon-copy' destination addresses.
	 * @param recipientsBcc The e-mail 'blind carbon-copy' destination addresses.
	 * @param senderCode The sender code, as configured in the service configuration.
	 * @return The send result.
	 * @throws ApsSystemException In case of errors.
	 */
	public boolean sendMixedMail(String simpleText, String htmlText, String subject, Properties attachmentFiles, 
			String[] recipientsTo, String[] recipientsCc, String[] recipientsBcc, String senderCode) throws ApsSystemException;
	
	/**
	 *
	 * @param mailConfig
	 * @return True if smtp server is working
	 * @throws ApsSystemException
	 */
	public boolean smtpServerTest(MailConfig mailConfig);

	/***
	 * Send and e-mail without checking if mail service is active
	 * @param text The text of mail part in text/plain contentType.
	 * @param subject The e-mail subject.
	 * @param recipientsTo The e-mail main destination addresses.
	 * @param senderCode
	 * @return
	 * @throws ApsSystemException 
	 */
	public boolean sendMailForTest(String text, String subject, String[] recipientsTo, String senderCode) throws ApsSystemException;
	/**
	 * The text/plain contentType.
	 */
	public static final String CONTENTTYPE_TEXT_PLAIN = "text/plain";
	
	/**
	 * The text/html contentType.
	 */
	public static final String CONTENTTYPE_TEXT_HTML = "text/html";
	
}
