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
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SignatureUtil {

    private static final String ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int KEY_SIZE = 2048;
    private static final String BEGIN_RSA_PUBLIC_KEY = "-----BEGIN RSA PUBLIC KEY-----\n";
    private static final String END_RSA_PUBLIC_KEY = "-----END RSA PUBLIC KEY-----";
    private static final String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n";
    private static final String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";

    private SignatureUtil() {
        // Utility class. Not to be instantiated.
    }

    /**
     * Generate a public/private {@link KeyPair} object
     * @return the generated {@link KeyPair}
     */
    public static KeyPair createKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(KEY_SIZE);
            return keyGen.generateKeyPair();
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Convert a {@link PublicKey} object to its PEM representation
     * @param publicKey the {@link PublicKey} to convert
     * @return the public key in PEM format
     */
    public static String publicKeyToPEM(PublicKey publicKey) {

        return BEGIN_RSA_PUBLIC_KEY +
                getBase64Key(publicKey) +
                END_RSA_PUBLIC_KEY;
    }

    /**
     * Convert a PEM formatted public key to a {@link PublicKey} object
     * @param pemPublicKey the PEM formatted public key
     * @return {@link PublicKey} object
     */
    public static PublicKey publicKeyFromPEM(String pemPublicKey) {
        String base64Key = pemPublicKey
                .replace(BEGIN_RSA_PUBLIC_KEY, "")
                .replace(END_RSA_PUBLIC_KEY, "")
                .replace("\n", "")
                .trim();

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

    public static String privateKeyToPEM(PrivateKey privateKey) {

        return BEGIN_RSA_PRIVATE_KEY +
                getBase64Key(privateKey) +
                END_RSA_PRIVATE_KEY;

    }

    public static PrivateKey privateKeyFromPEM(String pemPrivateKey) {
        String base64Key = pemPrivateKey
                .replace(BEGIN_RSA_PRIVATE_KEY, "")
                .replace(END_RSA_PRIVATE_KEY, "")
                .replace("\n", "")
                .trim();

        byte[] bytes = Base64.getDecoder().decode(base64Key);

        try {
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
            return kf.generatePrivate(privateKeySpec);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }

    }

    private static String getBase64Key(Key key) {
        StringBuilder sb = new StringBuilder();
        String base64 = Base64.getEncoder().encodeToString(key.getEncoded());
        int lineLength = 64;
        for (int i = 0;; i += lineLength) {
            if (i >= base64.length()) {
                break;
            }
            int chars = Math.min(base64.length(), i + lineLength);
            sb.append(base64.substring(i, chars)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Generate the encoded signature of the data using the private key
     * @param in the data to use to generate the signature
     * @param privateKey the key to use to encode the signature
     * @return the encoded signature
     */
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

    /**
     * Verifies if the alleged signature is the actual signature of the specified input stream
     * generated by the private key corresponding to the public key.
     * @param in the data input stream
     * @param publicKey the public key
     * @param base64Signature the private key encoded signature
     * @return true if the signature of the input stream correspond to the decoded signature
     */
    public static boolean verifySignature(InputStream in, PublicKey publicKey, String base64Signature) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64Signature);
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
