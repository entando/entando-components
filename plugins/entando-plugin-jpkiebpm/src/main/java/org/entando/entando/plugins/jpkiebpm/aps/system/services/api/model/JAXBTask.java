/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model;

import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "task")
@XmlType(propOrder = {"id", "activated", "created", "desc", "name", "owner", "parentId", "priority",
        "processDefinitionId", "processInstanceId", "skipable", "status", "subject", "containerId", "visible", "override"})
public class JAXBTask implements Comparable<JAXBTask> {

    public JAXBTask() {
    }

    public JAXBTask(KieTask task) {
        this.setActivated(task.getActivated());
        this.setCreated(task.getCreated());
        this.setDesc(task.getDesc());
        this.setId(task.getId());
        this.setName(task.getName());
        this.setOwner(task.getOwner());
        this.setParentId(task.getParentId());
        this.setPriority(task.getPriority());
        this.setProcessDefinitionId(task.getProcessDefinitionId());
        this.setProcessInstanceId(task.getProcessInstanceId());
        this.setSkipable(task.getSkipable());
        this.setStatus(task.getStatus());
        this.setSubject(task.getSubject());
        this.setContainerId(task.getContainerId());
    }

    @XmlElement(name = "activated")
    public Long getActivated() {
        return _activated;
    }

    public void setActivated(Long _activated) {
        this._activated = _activated;
    }

    @XmlElement(name = "created")
    public Long getCreated() {
        return _created;
    }

    public void setCreated(Long _created) {
        this._created = _created;
    }

    @XmlElement(name = "desc")
    public String getDesc() {
        return _desc;
    }

    public void setDesc(String _desc) {
        this._desc = _desc;
    }

    @XmlElement(name = "id")
    public Long getId() {
        return _id;
    }

    public void setId(Long _id) {
        this._id = _id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    @XmlElement(name = "owner")
    public String getOwner() {
        return _owner;
    }

    public void setOwner(String _owner) {
        this._owner = _owner;
    }

    @XmlElement(name = "parentId")
    public Long getParentId() {
        return _parentId;
    }

    public void setParentId(Long _parentId) {
        this._parentId = _parentId;
    }

    @XmlElement(name = "priority")
    public Long getPriority() {
        return _priority;
    }

    public void setPriority(Long _priority) {
        this._priority = _priority;
    }

    @XmlElement(name = "processDefinitionId")
    public String getProcessDefinitionId() {
        return _processDefinitionId;
    }

    public void setProcessDefinitionId(String _processDefinitionId) {
        this._processDefinitionId = _processDefinitionId;
    }

    @XmlElement(name = "processInstanceId")
    public Long getProcessInstanceId() {
        return _processInstanceId;
    }

    public void setProcessInstanceId(Long _processInstanceId) {
        this._processInstanceId = _processInstanceId;
    }

    @XmlElement(name = "skipable")
    public Boolean getSkipable() {
        return _skipable;
    }

    public void setSkipable(Boolean _skipable) {
        this._skipable = _skipable;
    }

    @XmlElement(name = "status")
    public String getStatus() {
        return _status;
    }

    public void setStatus(String _status) {
        this._status = _status;
    }

    @XmlElement(name = "subject")
    public String getSubject() {
        return _subject;
    }

    public void setSubject(String _subject) {
        this._subject = _subject;
    }

    @Override
    public int compareTo(JAXBTask o) {
        return (int) (this.getId() - o.getId());
    }

    @XmlElement(name = "containerId")
    public String getContainerId() {
        return _containerId;
    }

    public void setContainerId(String _containerId) {
        this._containerId = _containerId;
    }

    @XmlElement(name = "visible")
    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @XmlElement(name = "override")
    public String getOverride() {
        return override;
    }

    public void setOverride(String override) {
        this.override = override;
    }

    private Long _activated;
    private Long _created;
    private String _desc;
    private Long _id;
    private String _name;
    private String _owner;
    private Long _parentId;
    private Long _priority;
    private String _processDefinitionId;
    private Long _processInstanceId;
    private Boolean _skipable;
    private String _status;
    private String _subject;
    private String _containerId;
    private Boolean visible;
    private String override;

}
