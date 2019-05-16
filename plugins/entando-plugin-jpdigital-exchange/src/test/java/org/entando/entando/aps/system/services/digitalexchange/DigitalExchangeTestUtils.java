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
package org.entando.entando.aps.system.services.digitalexchange;

import org.apache.logging.log4j.util.Strings;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DigitalExchangeTestUtils {

    public static final String DE_1_ID = "DE_1";
    public static final String DE_1_NAME = "DigitalExchange 1";

    public static final String DE_2_ID = "DE_2";
    public static final String DE_2_NAME = "DigitalExchange 2";

    public static final String NEW_DE_NAME = "New DigitalExchange";

    public static final String INEXISTENT_DE_ID = "Inexistent_DE";

    public static final String DE_URL = "http://www.entando.com/";

    private static String DE_PUBLIC_KEY;

    private static String DE_PRIVATE_KEY;

    private DigitalExchangeTestUtils() {
    }

    public static DigitalExchange getDE1() {
        return getDigitalExchange(DE_1_ID, DE_1_NAME);
    }

    public static DigitalExchange getDE2() {
        return getDigitalExchange(DE_2_ID, DE_2_NAME);
    }

    public static DigitalExchange getNewDE() {
        return getDigitalExchange(NEW_DE_NAME);
    }

    public static DigitalExchange getInexistentDE() {
        return getDigitalExchange(INEXISTENT_DE_ID, INEXISTENT_DE_ID);
    }

    public static DigitalExchange getDEWithoutPublicKey(String name) {
         DigitalExchange deWithoutPublicKey = getDigitalExchange(null, name);
         deWithoutPublicKey.setPublicKey(null);
         return deWithoutPublicKey;
    }

    public static DigitalExchange getDEWithoutPublicKey(String id, String name) {
        DigitalExchange deWithoutPublicKey = getDigitalExchange(id, name);
        deWithoutPublicKey.setPublicKey(null);
        return deWithoutPublicKey;
    }

    public static String getTestPublicKey() {
        if (Strings.isEmpty(DE_PUBLIC_KEY)) {
            try {
                Path publicKeyPath = Paths.get(DigitalExchangeTestUtils.class.getResource("/de_test_public_key.txt").toURI());
                DE_PUBLIC_KEY = new String(Files.readAllBytes(publicKeyPath));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return DE_PUBLIC_KEY;
    }

    public static String getTestPrivateKey() {
        if (Strings.isEmpty(DE_PRIVATE_KEY)) {
            try {
                Path publicKeyPath = Paths.get(DigitalExchangeTestUtils.class.getResource("/de_test_private_key.txt").toURI());
                DE_PRIVATE_KEY = new String(Files.readAllBytes(publicKeyPath));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return DE_PRIVATE_KEY;
    }

    public static DigitalExchange getDigitalExchange(String name) {
        return getDigitalExchange(null, name);
    }
    
    public static DigitalExchange getDigitalExchange(String id, String name) {
        DigitalExchange digitalExchange = new DigitalExchange();
        digitalExchange.setId(id);
        digitalExchange.setName(name);
        digitalExchange.setUrl(DE_URL);
        digitalExchange.setPublicKey(getTestPublicKey());
        digitalExchange.setActive(true);
        return digitalExchange;
    }
}
