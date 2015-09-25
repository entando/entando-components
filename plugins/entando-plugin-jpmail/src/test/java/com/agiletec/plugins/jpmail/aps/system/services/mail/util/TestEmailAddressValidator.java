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
