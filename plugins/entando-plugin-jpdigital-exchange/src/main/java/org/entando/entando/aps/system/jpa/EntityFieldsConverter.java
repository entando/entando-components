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
package org.entando.entando.aps.system.jpa;

import com.agiletec.aps.system.SystemConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntityFieldsConverter<T> {

    /**
     * This method can be overridden to provide an appropriate conversion when
     * the attribute name read from the RestListRequest is different from the
     * corresponding field name in the entity class. By default they are the
     * same.
     */
    protected String getFieldName(String attributeName) {
        return attributeName;
    }

    /**
     * This method can be overridden to provide conversion from String to
     * Integer, Boolean and so on.
     */
    protected Comparable<?> getComparableValue(String attribute, String value) {
        return value;
    }

    protected Date parseDate(String value) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
            return sdf.parse(value);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
