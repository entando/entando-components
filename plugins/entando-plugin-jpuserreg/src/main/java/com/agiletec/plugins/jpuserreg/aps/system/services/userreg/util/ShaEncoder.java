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