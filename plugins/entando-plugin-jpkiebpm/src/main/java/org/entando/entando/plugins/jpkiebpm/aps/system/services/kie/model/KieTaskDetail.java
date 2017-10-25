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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Entando
 */
@XmlRootElement(name = "task-instance")
@XmlAccessorType(XmlAccessType.FIELD)
public class KieTaskDetail {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(String activationTime) {
        this.activationTime = activationTime;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }


    public String getSkippable() {
        return skippable;
    }

    public void setSkippable(String skippable) {
        this.skippable = skippable;
    }

    public Long getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(Long workitemId) {
        this.workitemId = workitemId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public KiePotentialOwners getPotentialOwners() {
        System.out.println("getPotentialOwners");
        return potentialOwners;
    }

    public void setPotentialOwners(KiePotentialOwners potentialOwners) {
        System.out.println("setPotentialOwners");
        this.potentialOwners = potentialOwners;
    }

    public KieExcludedOwners getExcludedOwners() {
        return excludedOwners;
    }

    public void setExcludedOwners(KieExcludedOwners excludedOwners) {
        this.excludedOwners = excludedOwners;
    }

    public KieBusinessAdmins getBusinessAdmins() {

        return businessAdmins;
    }

    public void setBusinessAdmins(KieBusinessAdmins businessAdmins) {
        this.businessAdmins = businessAdmins;
    }

    @XmlElement(name = "task-id")
    private Long id;

    @XmlElement(name = "task-priority")
    private Long priority;

    @XmlElement(name = "task-name")
    private String name;

    @XmlElement(name = "task-form")
    private String form;

    @XmlElement(name = "task-status")
    private String status;

    @XmlElement(name = "task-actual-owner")
    private String owner;

    @XmlElement(name = "task-created-by")
    private String createdBy;

    @XmlElement(name = "task-created-on")
    private String createdOn;

    @XmlElement(name = "task-activation-time")
    private String activationTime;

    @XmlElement(name = "task-expiration-time")
    private String expiration;

    @XmlElement(name = "task-skippable")
    private String skippable;

    @XmlElement(name = "task-workitem-id")
    private Long workitemId;

    @XmlElement(name = "task-process-instance-id")
    private Long processInstanceId;

    @XmlElement(name = "task-parent-id")
    private Long parentId;

    @XmlElement(name = "task-container-id")
    private String containerId;

    @XmlElement(name = "potential-owners")
    private KiePotentialOwners potentialOwners;

    @XmlElement(name = "excluded-owners")
    private KieExcludedOwners excludedOwners;

    @XmlElement(name = "business-admins")
    private KieBusinessAdmins businessAdmins;


    @XmlRootElement(name = "potential-owners")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class KiePotentialOwners {

        @XmlElement(name = "task-pot-owners")
        private List<String> potentialOwners = new ArrayList<>();

        public List<String> getPotentialOwners() {
            return potentialOwners;
        }

        public void setPotentialOwners(List<String> potentialOwners) {

            this.potentialOwners = potentialOwners;
        }
    }

    @XmlRootElement(name = "excluded-owners")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class KieExcludedOwners {

        @XmlElement(name = "task-exc-owners")
        private List<String> excludedOwners;

        public List<String> getExcludedOwners() {
            return excludedOwners;
        }

        public void setExcludedOwners(List<String> excludedOwners) {
            this.excludedOwners = excludedOwners;
        }


    }

    @XmlRootElement(name = "business-admins")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class KieBusinessAdmins {

        @XmlElement(name = "task-business-admins")
        private List<String> businessAdmins;

        public List<String> getBusinessAdmins() {
            return businessAdmins;
        }

        public void setBusinessAdmins(List<String> businessAdmins) {
            this.businessAdmins = businessAdmins;
        }


    }
}
