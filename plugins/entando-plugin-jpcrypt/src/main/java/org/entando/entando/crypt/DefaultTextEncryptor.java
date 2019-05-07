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
package org.entando.entando.crypt;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.Properties;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.encrypt.BouncyCastleAesGcmBytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * TextEncryptor implementation that wraps the standard Spring encryptor
 * generating also the required salt. BouncyCastle provider has been chosen in
 * order to avoid the JCE limited strength issue (see EN-2512).
 */
public class DefaultTextEncryptor implements TextEncryptor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTextEncryptor.class);

    private final String key;

    public DefaultTextEncryptor(Resource resource) {
        this(getKeyFromResource(resource));
    }

    public DefaultTextEncryptor(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key passed to DefaultTextEncryptor");
        }
        this.key = key;
    }

    private static String getKeyFromResource(Resource resource) {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            return properties.getProperty("key");
        } catch (IOException ex) {
            throw new UncheckedIOException("Unable to load properties resource", ex);
        }
    }

    /**
     * Returns a Base64 string composed by the salt followed by the encrypted
     * data.
     */
    @Override
    public String encrypt(String plainText) {

        try {
            // default StringKeyGenerator returns a 8 bytes hex-encoded string
            String salt = KeyGenerators.string().generateKey();

            BytesEncryptor encryptor = getBytesEncryptor(key, salt);

            byte[] encrypted = encryptor.encrypt(plainText.getBytes());

            byte[] saltAndSecret = ArrayUtils.addAll(Hex.decode(salt), encrypted);
            return Base64.getEncoder().encodeToString(saltAndSecret);
        } catch (Throwable t) {
            logger.warn("Unable to encrypt data", t);
        }
        return null;
    }

    /**
     * Returns decrypted text from a Base64 string composed by the salt followed
     * by the encrypted data.
     */
    @Override
    public String decrypt(String base64Data) {

        try {
            byte[] bytes = Base64.getDecoder().decode(base64Data);
            byte[] saltBytes = ArrayUtils.subarray(bytes, 0, 8);
            byte[] encryptedBytes = ArrayUtils.subarray(bytes, 8, bytes.length);

            String salt = new String(Hex.encode(saltBytes));
            BytesEncryptor encryptor = getBytesEncryptor(key, salt);

            return new String(encryptor.decrypt(encryptedBytes));
        } catch (Throwable t) {
            logger.warn("Unable to decrypt data: '" + base64Data + "'", t);
        }
        return base64Data;
    }

    private BytesEncryptor getBytesEncryptor(String key, String salt) {
        return new BouncyCastleAesGcmBytesEncryptor(key, salt);
    }
}
