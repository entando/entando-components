/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
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

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author matteo
 */
@XmlRootElement(name = "process-instanc")
@XmlType(propOrder = {"process-instance-id",
    "process-id",
    "process-name",
    "process-version",
    "process-instance-state",
    "container-id",
    "initiator",
    "start-date",
    "process-instance-desc",
    "correlation-key",
    "parent-instance-id"})
public class JAXBProcessInstance {

    private String processInstanceId;
    private String processId;
    private String processName;
    private String processVersion;
    private String processInstanceState;
    private String containerId;
    private String initiator;
    private Date startDate;
    private String processInstanceDesc;
    private String correlationKey;
    private String parentInstanceId;


    @XmlElement(name = "process-instance-id")
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @XmlElement(name = "process-id")
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @XmlElement(name = "process-name")
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @XmlElement(name = "process-version")
    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    @XmlElement(name = "process-instance-state")
    public String getProcessInstanceState() {
        return processInstanceState;
    }

    public void setProcessInstanceState(String processInstanceState) {
        this.processInstanceState = processInstanceState;
    }

    @XmlElement(name = "container-id")
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @XmlElement(name = "initiator")
    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    @XmlElement(name = "start-date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @XmlElement(name = "process-instance-desc")
    public String getProcessInstanceDesc() {
        return processInstanceDesc;
    }

    public void setProcessInstanceDesc(String processInstanceDesc) {
        this.processInstanceDesc = processInstanceDesc;
    }

    @XmlElement(name = "correlation-key")
    public String getCorrelationKey() {
        return correlationKey;
    }

    public void setCorrelationKey(String correlationKey) {
        this.correlationKey = correlationKey;
    }

    @XmlElement(name = "parent-instance-id")
    public String getParentInstanceId() {
        return parentInstanceId;
    }

    public void setParentInstanceId(String parentInstanceId) {
        this.parentInstanceId = parentInstanceId;
    }


}
