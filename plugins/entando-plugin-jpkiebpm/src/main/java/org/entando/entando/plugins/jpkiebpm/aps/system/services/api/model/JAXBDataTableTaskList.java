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


@XmlRootElement()

@XmlType(propOrder = {"draw", "recordsTotal", "recordsFiltered", "owner", "list", "datatableFieldDefinition", "processId", "containerId", "configId"})
public class JAXBDataTableTaskList /*extends JAXBTaskList */{

    public JAXBDataTableTaskList() {
    }

    public JAXBDataTableTaskList(String owner,Integer draw,Integer recordsTotal,Integer recordsFiltered, List<JAXBTask> tasksList) {
        this.setList(list);
        this.setDraw(draw);
        this.setRecordsFiltered(recordsFiltered);
        this.setRecordsTotal(recordsTotal);
        this.setOwner(owner);

    }
    
    @XmlElement(name = "draw")
    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }
    @XmlElement(name = "list")
    public List<JAXBTask> getList() {
        return list;
    }

    public void setList(List<JAXBTask> list) {
        this.list = list;
    }
    @XmlElement(name = "recordsTotal")
    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
    }
    
    @XmlElement(name = "recordsFiltered")
    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }


    @XmlElement(name = "fieldDefinition")
    public DatatableFieldDefinition getDatatableFieldDefinition() {
        if (this.datatableFieldDefinition == null)
            this.datatableFieldDefinition = new DatatableFieldDefinition();

        return datatableFieldDefinition;
    }
    
    @XmlElement(name = "processId")
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @XmlElement(name = "containerId")
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
    
    @XmlElement(name = "configId")
    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }
    
    @XmlElement(name = "owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    private String owner;
    private Integer draw;
    private List<JAXBTask> list;
    private DatatableFieldDefinition datatableFieldDefinition;
    private Integer recordsTotal;
    private Integer recordsFiltered;
    private String processId;
    private String containerId;
    private String configId;
}
