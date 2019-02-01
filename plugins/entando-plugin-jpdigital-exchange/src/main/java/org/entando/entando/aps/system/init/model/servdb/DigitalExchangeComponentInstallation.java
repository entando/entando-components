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
import java.util.Date;

@DatabaseTable(tableName = DigitalExchangeComponentInstallation.TABLE_NAME)
public class DigitalExchangeComponentInstallation {

    public static final String TABLE_NAME = "digital_exchange_installation";

    public static final String COL_ID = "id";
    public static final String COL_DIGITAL_EXCHANGE_ID = "digital_exchange_id";
    public static final String COL_DIGITAL_EXCHANGE_URL = "digital_exchange_url";
    public static final String COL_COMPONENT_ID = "component_id";
    public static final String COL_COMPONENT_NAME = "component_name";
    public static final String COL_COMPONENT_VERSION = "component_version";
    public static final String COL_STARTED_AT = "started_at";
    public static final String COL_ENDED_AT = "ended_at";
    public static final String COL_STARTED_BY = "started_by";
    public static final String COL_PROGRESS = "progress";
    public static final String COL_STATUS = "status";
    public static final String COL_ERROR_MESSAGE = "error_message";
    public static final String COL_JOB_TYPE = "job_type";

    @DatabaseField(columnName = COL_ID, dataType = DataType.STRING, canBeNull = false, id = true, width = 20)
    private String id;

    @DatabaseField(columnName = COL_DIGITAL_EXCHANGE_ID, dataType = DataType.STRING, canBeNull = false)
    private String digitalExchangeId;

    /**
     * For auditing purpose, because the DE id is auto-generated and became
     * useless if the DE is removed from the configuration.
     */
    @DatabaseField(columnName = COL_DIGITAL_EXCHANGE_URL, dataType = DataType.STRING, canBeNull = false)
    private String digitalExchangeUrl;

    @DatabaseField(columnName = COL_COMPONENT_ID, dataType = DataType.STRING, canBeNull = false)
    private String componentId;

    @DatabaseField(columnName = COL_COMPONENT_NAME, dataType = DataType.STRING, canBeNull = true)
    private String componentName;

    @DatabaseField(columnName = COL_COMPONENT_VERSION, dataType = DataType.STRING, canBeNull = true)
    private String version;

    @DatabaseField(columnName = COL_STARTED_AT, dataType = DataType.DATE, canBeNull = false)
    private Date started;

    @DatabaseField(columnName = COL_ENDED_AT, dataType = DataType.DATE, canBeNull = true)
    private Date ended;

    /**
     * The username of the user that started the installation job.
     */
    @DatabaseField(columnName = COL_STARTED_BY, dataType = DataType.STRING, canBeNull = false)
    private String user;

    /**
     * A value between 0 and 1.
     */
    @DatabaseField(columnName = COL_PROGRESS, dataType = DataType.DOUBLE, canBeNull = false)
    private double progress;

    @DatabaseField(columnName = COL_STATUS, dataType = DataType.STRING, canBeNull = false)
    private String status;

    @DatabaseField(columnName = COL_ERROR_MESSAGE, dataType = DataType.STRING)
    private String errorMessage;

    @DatabaseField(columnName = COL_JOB_TYPE, dataType = DataType.STRING, canBeNull = false)
    private String jobType;
}
