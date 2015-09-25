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
package com.agiletec.plugins.jpmail.aps.services.mail.util;

import java.util.StringTokenizer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Utility class containing methods to validate email addresses.
 * 
 * @version 1.0
 * @author E.Santoboni
 *
 */
public class EmailAddressValidator {
	
	/**
	 * Validate the given email address.
	 * @param emailAddress The email address.
	 * @return true if the given string is a valid email address, false otherwise.
	 */
	public static boolean isValidEmailAddress(String emailAddress) {
		if ( emailAddress == null )
			return false;
		int atIndex = emailAddress.indexOf("@");
		if ( atIndex < 0 )
			return false;
		if ( emailAddress.lastIndexOf(".") <= atIndex+1 )
			return false;
		if ( lastEmailFieldTwoCharsOrMore(emailAddress) == false )
			return false;
		try	{
			new InternetAddress(emailAddress);
			return true;
		} catch (AddressException ae) {
			return false;
		}
	}
	
	/**
	 * Returns true if the last email field (i.e., the country code, or something
	 * like .com, .biz, .cc, etc.) is two chars or more in length, which it really
	 * must be to be legal.
	 */
	private static boolean lastEmailFieldTwoCharsOrMore(String emailAddress) {
		if (emailAddress == null) return false;
		StringTokenizer st = new StringTokenizer(emailAddress,".");
		String lastToken = null;
		while ( st.hasMoreTokens() ) {
			lastToken = st.nextToken();
		}
		if ( lastToken.length() >= 2 ) {
			return true;
		} else {
			return false;
		}
	}
	
}
