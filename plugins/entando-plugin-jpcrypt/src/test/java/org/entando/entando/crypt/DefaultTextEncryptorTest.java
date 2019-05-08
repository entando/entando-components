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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class DefaultTextEncryptorTest {

    @Test
    public void testEncryptAndDecrypt() {
        String key = "test-key";
        String plainText = "secret text";
        DefaultTextEncryptor defaultTextEncryptor = new DefaultTextEncryptor(key);
        String encryptedText = defaultTextEncryptor.encrypt(plainText);
        String decryptedText = defaultTextEncryptor.decrypt(encryptedText);
        assertThat(decryptedText).isEqualTo(plainText);
    }

    @Test
    public void testLoadFromResource() {
        String plainText = "secret text";
        ClassPathResource resource = new ClassPathResource("security.properties");
        DefaultTextEncryptor defaultTextEncryptor = new DefaultTextEncryptor(resource);
        String encryptedText = defaultTextEncryptor.encrypt(plainText);
        String decryptedText = defaultTextEncryptor.decrypt(encryptedText);
        assertThat(decryptedText).isEqualTo(plainText);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        String key = null;
        new DefaultTextEncryptor(key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnexistingResource() {
        ClassPathResource resource = new ClassPathResource("foo");
        new DefaultTextEncryptor(resource);
    }
}
