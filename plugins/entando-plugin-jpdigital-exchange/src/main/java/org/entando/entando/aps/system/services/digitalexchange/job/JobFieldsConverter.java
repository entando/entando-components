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
package org.entando.entando.aps.system.services.digitalexchange.job;

import org.entando.entando.aps.system.jpa.EntityFieldsConverter;

public class JobFieldsConverter extends EntityFieldsConverter<DigitalExchangeJob> {

    @Override
    protected String getFieldName(String attributeName) {
        if ("code".equals(attributeName)) { // code is the default RestListRequest sorting property
            return "id";
        }
        return super.getFieldName(attributeName);
    }

    @Override
    protected Comparable<?> getComparableValue(String attribute, String value) {
        switch (attribute) {
            case "progress":
                return Double.parseDouble(value);
            case "jobType":
                return JobType.valueOf(value);
            case "status":
                return JobStatus.valueOf(value);
            case "started":
            case "ended":
                return parseDate(value);
            default:
                return super.getComparableValue(attribute, value);
        }
    }
}
