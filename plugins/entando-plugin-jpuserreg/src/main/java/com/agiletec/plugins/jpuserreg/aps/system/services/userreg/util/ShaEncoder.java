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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class ShaEncoder {
	
	public static String encodePassword(String password) throws NoSuchAlgorithmException {
		if (password != null) {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(password.getBytes());
			byte bytes[] = digest.digest();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				int b = bytes[i] & 0xff;
				if (b < 16) {
					buffer.append("0");
				}
				buffer.append(Integer.toHexString(b));
			}
			password = buffer.toString();
		}
		return password;
	}
	
	public static String encodePassword(String password, String salt) throws NoSuchAlgorithmException {
		String saltedPass = mergePasswordAndSalt(password, salt, false);
		MessageDigest messageDigest = MessageDigest.getInstance("SHA");
		byte[] digest = messageDigest.digest(saltedPass.getBytes());
		return new String(Hex.encodeHex(digest));
	}
	
    protected static String mergePasswordAndSalt(String password, Object salt, boolean strict) {
        if (password == null) {
            password = "";
        }
        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
            }
        }
        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }
    
}