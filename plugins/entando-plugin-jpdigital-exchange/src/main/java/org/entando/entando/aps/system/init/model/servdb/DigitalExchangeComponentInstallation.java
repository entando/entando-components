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

    public static final String COL_ID = "id";
    public static final String COL_DIGITAL_EXCHANGE = "digital_exchange";
    public static final String COL_COMPONENT = "component";
    public static final String COL_PROGRESS = "progress";
    public static final String COL_STATUS = "status";
    public static final String COL_ERROR_MESSAGE = "error_message";

    @DatabaseField(columnName = COL_ID, dataType = DataType.STRING, canBeNull = false, id = true, width = 20)
    private String id;

    @DatabaseField(columnName = COL_DIGITAL_EXCHANGE, dataType = DataType.STRING, canBeNull = false)
    private String digitalExchange;

    @DatabaseField(columnName = COL_COMPONENT, dataType = DataType.STRING, canBeNull = false)
    private String component;

    @DatabaseField(columnName = COL_PROGRESS, dataType = DataType.INTEGER, canBeNull = false)
    private int progress;

    @DatabaseField(columnName = COL_STATUS, dataType = DataType.STRING, canBeNull = false)
    private String status;

    @DatabaseField(columnName = COL_ERROR_MESSAGE, dataType = DataType.STRING)
    private String errorMessage;
}
