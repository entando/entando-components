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
package org.entando.entando.plugins.jpwebmail.aps.system.services.webmail.parse;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.parse.WebMailConfigDOM;
import org.entando.entando.plugins.jpwebmail.aps.ApsPluginBaseTestCase;

public class TestWebMailConfigDOM extends ApsPluginBaseTestCase {
	
	public void testGetItems() throws ApsSystemException {
		WebMailConfigDOM configDOM = new WebMailConfigDOM();
		WebMailConfig bean = configDOM.extractConfig(XML);
		assertEquals("smtpUsername", bean.getSmtpUserName());
		assertEquals("SMTP.EMAIL.COM", bean.getSmtpHost());
		assertEquals("smtpPassword", bean.getSmtpPassword());
		assertEquals("/cert/path/", bean.getCertificatePath());
		assertTrue(bean.isCertificateLazyCheck());
		assertTrue(bean.isDebug());
	}
	
	private String XML = 
		"<webMailConfig>" +		
		"	<certificates>" +	
		"		<enable>true</enable>" +	
		"		<lazyCheck>true</lazyCheck>" +	
		"		<certPath>/cert/path/</certPath>" +	
		"		<debugOnConsole>true</debugOnConsole>" +	
		"	</certificates>" +	
		"	<smtp debug=\"true\" >" +
		"		<host>SMTP.EMAIL.COM</host>" +
		"		<user>smtpUsername</user>" +
		"		<password>smtpPassword</password>" +
		"	</smtp>" +
		"	<imap>" +
		"		<host>imap.gmail.com</host>" +
		"		<protocol>imaps</protocol>" +
		"		<port>993</port>" +
		"	</imap>" +
		"</webMailConfig>";
	
}