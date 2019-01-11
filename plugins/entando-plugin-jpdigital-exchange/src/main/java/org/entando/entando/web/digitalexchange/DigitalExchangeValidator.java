/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.digitalexchange;

import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class DigitalExchangeValidator {

    public static final String ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND = "1";
    public static final String ERRCODE_DIGITAL_EXCHANGE_ALREADY_EXISTS = "2";
    public static final String ERRCODE_URI_ID_MISMATCH = "3";
    public static final String ERRCODE_DIGITAL_EXCHANGE_INVALID_URL = "4";
    public static final String ERRCODE_DIGITAL_EXCHANGE_NAME_TAKEN = "5";

    public void validateBodyId(String id, DigitalExchange digitalExchange, Errors errors) {
        if (!digitalExchange.getId().equals(id)) {
            errors.rejectValue("id", ERRCODE_URI_ID_MISMATCH, new Object[]{id, digitalExchange.getId()}, "digitalExchange.id.mismatch");
        }
    }
}
