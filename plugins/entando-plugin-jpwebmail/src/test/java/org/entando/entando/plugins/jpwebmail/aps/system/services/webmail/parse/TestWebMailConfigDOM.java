/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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