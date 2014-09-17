/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmail.aps.system.services.mail.util;

import com.agiletec.plugins.jpmail.aps.ApsPluginBaseTestCase;

import com.agiletec.plugins.jpmail.aps.services.mail.util.EmailAddressValidator;

/**
 * Testing class for Email Address validator
 * @version 1.0
 * @author E.Mezzano
 *
 */
public class TestEmailAddressValidator extends ApsPluginBaseTestCase {
	
	/**
	 * Tests the validation of mail addresses.
	 */
	public void testMail() {
		boolean bool1 = EmailAddressValidator.isValidEmailAddress(mail1);
		boolean bool2 = EmailAddressValidator.isValidEmailAddress(mail2);
		boolean bool3 = EmailAddressValidator.isValidEmailAddress(mail3);
		assertFalse(bool1);
		assertFalse(bool2);
		assertTrue(bool3);
	}
	
	private static final String mail1 = "dfsdfsdfs.sdfsfs.xx";
	private static final String mail2 = "dfsdfsdfs@sdfsfs.x";
	private static final String mail3 = "dfsdfsdfs@sdfsfs.xxx";
	
}
