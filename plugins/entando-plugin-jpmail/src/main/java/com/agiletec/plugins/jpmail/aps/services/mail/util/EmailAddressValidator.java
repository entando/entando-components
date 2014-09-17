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
