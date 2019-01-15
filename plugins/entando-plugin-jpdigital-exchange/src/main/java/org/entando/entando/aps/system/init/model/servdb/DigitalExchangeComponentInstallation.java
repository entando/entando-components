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
package org.entando.entando.aps.system.init.model.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = DigitalExchangeComponentInstallation.TABLE_NAME)
public class DigitalExchangeComponentInstallation {

    public static final String TABLE_NAME = "digital_exchange_installation";

    @DatabaseField(columnName = "id", dataType = DataType.STRING, canBeNull = false, id = true, width = 20)
    private String id;

    @DatabaseField(columnName = "digital_exchange", dataType = DataType.STRING, canBeNull = false)
    private String digitalExchange;

    @DatabaseField(columnName = "component", dataType = DataType.STRING, canBeNull = false)
    private String component;

    @DatabaseField(columnName = "progress", dataType = DataType.INTEGER, canBeNull = false)
    private int progress;

    @DatabaseField(columnName = "status", dataType = DataType.STRING, canBeNull = false)
    private String status;

    @DatabaseField(columnName = "error_message", dataType = DataType.STRING)
    private String errorMessage;
}
