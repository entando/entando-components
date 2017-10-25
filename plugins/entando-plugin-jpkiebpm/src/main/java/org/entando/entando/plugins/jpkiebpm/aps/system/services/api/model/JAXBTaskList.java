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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "taskList")
@XmlType(propOrder = {"owner", "list", "datatableFieldDefinition", "processId", "containerId"})
public class JAXBTaskList {

    public JAXBTaskList() {
    }

    public JAXBTaskList(String owner, List<JAXBTask> list) {
        this.setOwner(owner);
        this.setList(list);
    }

    @XmlElement(name = "owner")
    public String getOwner() {
        return _owner;
    }

    public void setOwner(String _owner) {
        this._owner = _owner;
    }

    @XmlElement(name = "list")
    public List<JAXBTask> getList() {
        return _list;
    }

    public void setList(List<JAXBTask> _list) {
        this._list = _list;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @XmlElement(name = "datatable-field-definition")
    public DatatableFieldDefinition getDatatableFieldDefinition() {
        if (this.datatableFieldDefinition == null)
            this.datatableFieldDefinition = new DatatableFieldDefinition();

        return datatableFieldDefinition;
    }

    @XmlElement(name = "processId")
    public String getProcessId() {
        return processId;
    }


    @XmlElement(name = "containerId")
    public String getContainerId() {
        return containerId;
    }


    private String _owner;
    private List<JAXBTask> _list;
    private DatatableFieldDefinition datatableFieldDefinition;
    private String processId;
    private String containerId;
}
