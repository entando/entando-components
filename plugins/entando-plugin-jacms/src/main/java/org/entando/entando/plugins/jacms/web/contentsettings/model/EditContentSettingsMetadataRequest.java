package org.entando.entando.plugins.jacms.web.contentsettings.model;

import javax.validation.constraints.NotNull;

public class EditContentSettingsMetadataRequest {

    @NotNull
    private String mapping;


    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
