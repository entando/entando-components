/*
 * The MIT License
 *
 * Copyright 2019 Entando Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;


@XmlRootElement(name = "taskList")

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
