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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.model;

import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;

/**
 *
 * @author paddeo
 */
public class DatatableWidgetConfigDto {

    private final String PREFIX_FIELD = "field_";
    private final String PREFIX_VISIBLE = "visible_";
    private final String PREFIX_OVERRIDE = "override_";
    private final String PREFIX_POSITION = "position_";

    private int id;
    private List<Map<String, String>> informationOnline = new ArrayList<>();
    private List<Map<String, String>> informationDraft = new ArrayList<>();
    private String widgetType;
    private String pageCode;
    private Integer framePosOnline;
    private Integer framePosDraft;
    private String containerId;
    private String processId;
    private String knowledgeSourceId;
    private String dataType;
    private long dataUxId;

    public DatatableWidgetConfigDto(BpmWidgetInfo info) {
        this.id = info.getId();
        this.widgetType = info.getWidgetType();
        this.pageCode = info.getPageCode();
        this.framePosOnline = info.getFramePosOnline();
        this.framePosDraft = info.getFramePosDraft();
        if (info.getConfigDraft() != null) {
            Map<String, Map<String, String>> fieldsMap = extractInfo(info.getConfigDraft());
            this.informationDraft.addAll(fieldsMap.values());
        }
        if (info.getConfigOnline() != null) {
            Map<String, Map<String, String>> fieldsMap = extractInfo(info.getConfigOnline());
            this.informationOnline.addAll(fieldsMap.values());
        }
    }

    public DatatableWidgetConfigDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Map<String, String>> getInformationOnline() {
        return informationOnline;
    }

    public void setInformationOnline(List<Map<String, String>> informationOnline) {
        this.informationOnline = informationOnline;
    }

    public List<Map<String, String>> getInformationDraft() {
        return informationDraft;
    }

    public void setInformationDraft(List<Map<String, String>> informationDraft) {
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public long getDataUxId() {
        return dataUxId;
    }

    public void setDataUxId(long dataUxId) {
        this.dataUxId = dataUxId;
    }

    private Map<String, Map<String, String>> extractInfo(ApsProperties config) {
        this.containerId = config.getProperty("containerId");
        this.processId = config.getProperty("processId");
        this.knowledgeSourceId = config.getProperty("kieSourceId");

        Map<String, Map<String, String>> fieldsMap = new HashMap<>();
        for (Object fieldPropObj : config.keySet()) {
            String fieldProp = (String) fieldPropObj;
            Map<String, String> fieldMap = null;
            String key = fieldProp.substring(fieldProp.indexOf("_") != -1 ? fieldProp.indexOf("_") + 1 : 0).trim();
            if ((fieldMap = fieldsMap.get(key)) == null) {
                fieldMap = new HashMap<>();
            }
            if (fieldProp.startsWith(PREFIX_FIELD)) {
                fieldMap.put("field", key);
                fieldsMap.put(key, fieldMap);
            }
            if (fieldProp.startsWith(PREFIX_VISIBLE)) {
                fieldMap.put("visible", config.getProperty(fieldProp));
                fieldsMap.put(key, fieldMap);
            }
            if (fieldProp.startsWith(PREFIX_OVERRIDE)) {
                fieldMap.put("override", config.getProperty(fieldProp));
                fieldsMap.put(key, fieldMap);
            }
            if (fieldProp.startsWith(PREFIX_POSITION)) {
                fieldMap.put("position", config.getProperty(fieldProp));
                fieldsMap.put(key, fieldMap);
            }
        }
        return fieldsMap;
    }

    @Override
    public String toString() {
        return "DatatableWidgetConfigDto: {" + "id=" + id + ", informationOnline=" + informationOnline + ", informationDraft=" + informationDraft + ", widgetType=" + widgetType + ", pageCode=" + pageCode + ", framePosOnline=" + framePosOnline + ", framePosDraft=" + framePosDraft + ", containerId=" + containerId + ", processId=" + processId + ", knowledgeSourceId=" + knowledgeSourceId + ", dataType=" + dataType + ", dataUxId=" + dataUxId + '}';
    }

}
