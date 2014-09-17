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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class ShaEncoder {

	public static String encodeWord(String word) throws NoSuchAlgorithmException {
		if (word != null) {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(word.getBytes());
			byte bytes[] = digest.digest();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				int b = bytes[i] & 0xff;
				if (b < 16) {
					buffer.append("0");
				}
				buffer.append(Integer.toHexString(b));
			}
			word = buffer.toString();
		}
		return word;
	}

	public static String encodeWord(String word, String salt) throws NoSuchAlgorithmException {
		String saltedPass = mergeWordAndSalt(word, salt, false);
		MessageDigest messageDigest = MessageDigest.getInstance("SHA");
		byte[] digest = messageDigest.digest(saltedPass.getBytes());
		return new String(Hex.encodeHex(digest));
	}

    protected static String mergeWordAndSalt(String word, Object salt, boolean strict) {
        if (word == null) {
        	word = "";
        }
        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
            }
        }
        if ((salt == null) || "".equals(salt)) {
            return word;
        } else {
            return word + "{" + salt.toString() + "}";
        }
    }

}