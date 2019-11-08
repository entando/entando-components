/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.web.contentsettings.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@ApiModel("ContentSettings")
public class ContentSettingsDto {

    @JsonProperty("indexesLastReload")
    private LastReloadInfoDto indexesLastReloadInfo;

    @JsonProperty("indexesStatus")
    private int indexesStatus;

    @JsonProperty("referencesStatus")
    private int referencesStatus;

    @JsonProperty("metadata")
    private Map<String, List<String>> metadata;

    @JsonProperty("cropRatios")
    private List<String> cropRatios;

    @JsonProperty("editor")
    private Editor editor;


    public int getIndexesStatus() {
        return indexesStatus;
    }

    public void setIndexesStatus(int indexesStatus) {
        this.indexesStatus = indexesStatus;
    }

    public int getReferencesStatus() {
        return referencesStatus;
    }

    public void setReferencesStatus(int referencesStatus) {
        this.referencesStatus = referencesStatus;
    }

    public Map<String, List<String>> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, List<String>> metadata) {
        this.metadata = metadata;
    }

    public List<String> getCropRatios() {
        return cropRatios;
    }

    public void setCropRatios(List<String> cropRatios) {
        this.cropRatios = cropRatios;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public String toString() {

        return "class ContentSettings {\n"
                + "    indexesStatus: " + toIndentedString(indexesStatus) + "\n"
                + "    referencesStatus: " + toIndentedString(referencesStatus) + "\n"
                + "    metadata: " + toIndentedString(metadata) + "\n"
                + "    cropRatios: " + toIndentedString(cropRatios) + "\n"
                + "}";
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    public void setIndexesLastReloadInfo(LastReloadInfoDto indexesLastReloadInfo) {
        this.indexesLastReloadInfo = indexesLastReloadInfo;
    }

    public LastReloadInfoDto getIndexesLastReloadInfo() {
        return indexesLastReloadInfo;
    }

    public enum Editor {
        NONE("None"),
        FCKEDITOR("CKEditor");

        private String label;
        Editor(String label){
            this.label = label;
        }

        @JsonValue
        public Map<String, String> toValue() {
            Map<String,String> map = new HashMap<>();
            map.put("key", getKey());
            map.put("label", label);
            return map;
        }

        public static Editor fromValue(String key) {
            if (key == null || key.trim().isEmpty()){
                return Editor.NONE;
            }

            return Editor.valueOf(key.toUpperCase());
        }

        public String getKey() {
            return toString().toLowerCase();
        }
    }
}
