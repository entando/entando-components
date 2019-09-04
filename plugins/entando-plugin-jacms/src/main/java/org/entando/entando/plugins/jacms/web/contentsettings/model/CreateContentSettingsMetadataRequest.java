package org.entando.entando.plugins.jacms.web.contentsettings.model;

import javax.validation.constraints.NotNull;

public class CreateContentSettingsMetadataRequest {

    @NotNull
    private String key;

    @NotNull
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
