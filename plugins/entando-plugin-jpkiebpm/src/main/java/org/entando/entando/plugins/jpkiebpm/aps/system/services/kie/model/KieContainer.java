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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Entando
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class KieContainer {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public KieScanner getScanner() {
        return scanner;
    }

    public void setScanner(KieScanner scanner) {
        this.scanner = scanner;
    }

    public List<KieMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<KieMessage> messages) {
        this.messages = messages;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public KieReleaseId getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(KieReleaseId releaseId) {
        this.releaseId = releaseId;
    }

    public KieReleaseId getResolvedReleaseId() {
        return resolvedReleaseId;
    }

    public void setResolvedReleaseId(KieReleaseId resolvedReleaseId) {
        this.resolvedReleaseId = resolvedReleaseId;
    }

    public List<KieConfigItem> getConfigItems() {
        return configItems;
    }

    public void setConfigItems(List<KieConfigItem> configItems) {
        this.configItems = configItems;
    }

    

    private String status;
    private KieScanner scanner;
    private List<KieMessage> messages;

    @XmlElement(name="container-id")
    private String containerId;

    @XmlElement(name="release-id")
    private KieReleaseId releaseId;

    @XmlElement(name="resolved-release-id")
    private KieReleaseId resolvedReleaseId;

    @XmlElement(name="config-items")
    private List<KieConfigItem> configItems;
}
