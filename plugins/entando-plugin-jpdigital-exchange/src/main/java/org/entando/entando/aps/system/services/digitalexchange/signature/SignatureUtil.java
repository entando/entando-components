/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.digitalexchange.signature;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SignatureUtil {

    private static final String ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int KEY_SIZE = 2048;
    private static final String BEGIN_RSA_PUBLIC_KEY = "-----BEGIN RSA PUBLIC KEY-----\n";
    private static final String END_RSA_PUBLIC_KEY = "-----END RSA PUBLIC KEY-----";

    private SignatureUtil() {
        // Utility class. Not to be instantiated.
    }

    public static KeyPair createKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(KEY_SIZE);
            return keyGen.generateKeyPair();
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String publicKeyToPEM(PublicKey publicKey) {

        StringBuilder sb = new StringBuilder();

        sb.append(BEGIN_RSA_PUBLIC_KEY);

        String base64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        int lineLength = 64;
        for (int i = 0;; i += lineLength) {
            if (i >= base64.length()) {
                break;
            }
            int chars = Math.min(base64.length(), i + lineLength);
            sb.append(base64.substring(i, chars)).append("\n");
        }

        sb.append(END_RSA_PUBLIC_KEY);

        return sb.toString();
    }

    public static PublicKey publicKeyFromPEM(String pemPublicKey) {
        String base64Key = pemPublicKey
                .replace(BEGIN_RSA_PUBLIC_KEY, "")
                .replace(END_RSA_PUBLIC_KEY, "")
                .replace("\n", "");

        byte[] bytes = Base64.getDecoder().decode(base64Key);

        try {
            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
            return kf.generatePublic(ks);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static PrivateKey getPrivateKeyFromBytes(byte[] privateKeyBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(privKeySpec);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String signPackage(InputStream in, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            updateSignature(signature, in);
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (GeneralSecurityException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean verifySignature(InputStream in, PublicKey publicKey, String base64Sign) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64Sign);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            updateSignature(signature, in);
            return signature.verify(bytes);
        } catch (GeneralSecurityException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void updateSignature(Signature signature, InputStream in) throws GeneralSecurityException, IOException {

        try (BufferedInputStream bin = new BufferedInputStream(in)) {
            byte[] buffer = new byte[1024];
            while (bin.available() != 0) {
                int len = bin.read(buffer);
                signature.update(buffer, 0, len);
            }
        }
    }
}
