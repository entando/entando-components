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

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

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
    private Long dueDate;

    private String country;
    private Long dateofbirth;
    private Long clientid;
    private String creditscore;
    private String ssn;
    private String relationship;
    private String status;


    public JAXBProcessInstancePlus(KieProcessInstance process) {
        super(process);

        String pnameStr = (String) process.getProcess_instance_variables().get("pname");
        String surnameStr = (String) process.getProcess_instance_variables().get("surname");

        if (StringUtils.isBlank(pnameStr)) {
            pnameStr= "";
        }
        if (StringUtils.isBlank(surnameStr)) {
            surnameStr= "";
        }

        this.setCompany((String) process.getProcess_instance_variables().get("name"));
        this.setType((String) process.getProcess_instance_variables().get("type"));
        this.setBic((String) process.getProcess_instance_variables().get("bic"));
        this.setProcessStatus((String) process.getProcess_instance_variables().get("status"));
        this.setDueDate(new Date(process.getStartDate() + (60 * 60 * 24 * 14 * 1000)).getTime());
        this.setCountry((String) process.getProcess_instance_variables().get("country"));
        // email
        this.setEmail((String) process.getProcess_instance_variables().get("email"));
        // partyname
        this.setPartyName(pnameStr + " " + surnameStr);
        // date of birth
        this.setDateofbirth((Long) process.getProcess_instance_variables().get("dateofbirth"));
        // clientId
        if (process.getProcess_instance_variables().get("clientid") instanceof Integer) {
            this.setClientid(Long.valueOf((Integer)process.getProcess_instance_variables().get("clientid")));
        }
        if (process.getProcess_instance_variables().get("clientid") instanceof Long) {
            this.setClientid((Long) process.getProcess_instance_variables().get("clientid"));
        }
        // credit score
        this.setCreditscore((String) process.getProcess_instance_variables().get("creditscore"));
        // phone
        this.setPhone((String) process.getProcess_instance_variables().get("phonenumber"));
        // ssn
        this.setSsn((String) process.getProcess_instance_variables().get("ssn"));
        // relationship
        this.setRelationship((String) process.getProcess_instance_variables().get("relationship"));
        // status
        this.setStatus((String) process.getProcess_instance_variables().get("status"));
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

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(Long dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public Long getClientid() {
        return clientid;
    }

    public void setClientid(Long clientid) {
        this.clientid = clientid;
    }

    public String getCreditscore() {
        return creditscore;
    }

    public void setCreditscore(String creditscore) {
        this.creditscore = creditscore;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
