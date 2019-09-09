package org.entando.entando.plugins.jacms.web.contentsettings.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter@Setter
public class ContentSettingsCropRatioRequest {

        @NotEmpty
    private String ratio;
}
