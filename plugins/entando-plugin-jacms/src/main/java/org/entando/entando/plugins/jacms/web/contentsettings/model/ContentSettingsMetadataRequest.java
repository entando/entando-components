package org.entando.entando.plugins.jacms.web.contentsettings.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

public class ContentSettingsMetadataRequest {

    @NotNull
    private String key;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mapping;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
