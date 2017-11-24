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

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessInstance;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author Entando
 */
@XmlRootElement(name = "processInstance")
public class JAXBProcessInstancePlus extends JAXBProcessInstance {

    private String company;
    private String partyName;
    private String processStatus;
    private String type;
    private String bic;
    private String email;
    private String phone;
    private Date dueDate;

    public JAXBProcessInstancePlus(KieProcessInstance process) {
        super(process);
        this.setCompany((String) process.getProcess_instance_variables().get("name"));
        this.setType((String) process.getProcess_instance_variables().get("type"));
        this.setBic((String) process.getProcess_instance_variables().get("bic"));
        this.setProcessStatus((String) process.getProcess_instance_variables().get("status"));
        this.setDueDate(new Date(process.getStartDate() + (60 * 60 * 24 * 14 * 1000)));
        this.setCompany((String) process.getProcess_instance_variables().get("name"));
        this.setCompany((String) process.getProcess_instance_variables().get("name"));
        this.setCompany((String) process.getProcess_instance_variables().get("name"));
    }

    public JAXBProcessInstancePlus() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

}
