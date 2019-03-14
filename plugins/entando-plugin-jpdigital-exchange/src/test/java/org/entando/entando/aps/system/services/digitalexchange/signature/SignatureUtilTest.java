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

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class SignatureUtilTest {

    @Test
    public void testPublicKeyToPEMAndBack() {

        KeyPair keyPair = SignatureUtil.createKeyPair();

        PublicKey publicKey = keyPair.getPublic();

        String pemPublicKey = SignatureUtil.publicKeyToPEM(publicKey);
        PublicKey parsedKey = SignatureUtil.publicKeyFromPEM(pemPublicKey);

        assertThat(publicKey.getEncoded()).isEqualTo(parsedKey.getEncoded());
    }

    @Test
    public void testSignAndVerify() {

        KeyPair keyPair = SignatureUtil.createKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String content = "content";

        String signature = SignatureUtil.signPackage(
                IOUtils.toInputStream(content, StandardCharsets.UTF_8), privateKey);
        assertThat(signature).isNotNull();

        assertThat(SignatureUtil.verifySignature(
                IOUtils.toInputStream(content, StandardCharsets.UTF_8), publicKey, signature))
                .isTrue();
    }

    @Test
    public void testPrivateKeyToBytes() {

        KeyPair keyPair = SignatureUtil.createKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PrivateKey parsedKey = SignatureUtil.getPrivateKeyFromBytes(privateKey.getEncoded());

        assertThat(privateKey.getEncoded()).isEqualTo(parsedKey.getEncoded());
    }
}
