/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import java.util.Date;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.IBpmOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.OverrideList;

public class KieFormOverride {

    private int id;
    private int widgetInfoId;
    private Date date;
    private String field;
    private String containerId;
    private String processId;
    private String sourceId;
    private boolean active;
    private boolean online;

    // this object is de/serialized automatically
    private OverrideList overrides;

    /**
     * Add an overried to the current field
     *
     * @param override
     */
    public void addOverride(IBpmOverride override) {
        if (null == override) {
            return;
        }
        if (null == this.getOverrides()) {
            this.setOverrides(new OverrideList());
        }
        this.getOverrides().addOverride(override);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidgetInfoId() {
        return widgetInfoId;
    }

    public void setWidgetInfoId(int _widgetInfoId) {
        this.widgetInfoId = _widgetInfoId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public OverrideList getOverrides() {
        return overrides;
    }

    public void setOverrides(OverrideList overrides) {
        this.overrides = overrides;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

}
