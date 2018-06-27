/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author paddeo
 */
public class DatatableWidgetConfigRequest {

    private int id;
    private List<Map<String, Object>> informationOnline = new ArrayList<>();
    private List<Map<String, Object>> informationDraft = new ArrayList<>();
    private String widgetType;
    private String pageCode;
    private Integer framePosOnline;
    private Integer framePosDraft;
    private String containerId;
    private String processId;
    private String knowledgeSourceId;
    private String group;
    private String lang;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Map<String, Object>> getInformationOnline() {
        return informationOnline;
    }

    public void setInformationOnline(List<Map<String, Object>> informationOnline) {
        this.informationOnline = informationOnline;
    }

    public List<Map<String, Object>> getInformationDraft() {
        return informationDraft;
    }

    public void setInformationDraft(List<Map<String, Object>> informationDraft) {
        this.informationDraft = informationDraft;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public Integer getFramePosOnline() {
        return framePosOnline;
    }

    public void setFramePosOnline(Integer framePosOnline) {
        this.framePosOnline = framePosOnline;
    }

    public Integer getFramePosDraft() {
        return framePosDraft;
    }

    public void setFramePosDraft(Integer framePosDraft) {
        this.framePosDraft = framePosDraft;
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

    public String getKnowledgeSourceId() {
        return knowledgeSourceId;
    }

    public void setKnowledgeSourceId(String knowledgeSourceId) {
        this.knowledgeSourceId = knowledgeSourceId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "DatatableWidgetConfigRequest:{" + "id=" + id + ", informationOnline=" + informationOnline + ", informationDraft=" + informationDraft + ", widgetType=" + widgetType + ", pageCode=" + pageCode + ", framePosOnline=" + framePosOnline + ", framePosDraft=" + framePosDraft + ", containerId=" + containerId + ", processId=" + processId + ", knowledgeSourceId=" + knowledgeSourceId + ", group=" + group + ", lang=" + lang + '}';
    }

}
