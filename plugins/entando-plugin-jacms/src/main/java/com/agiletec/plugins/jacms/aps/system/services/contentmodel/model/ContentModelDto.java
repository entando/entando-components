package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel("ContentModel")
public class ContentModelDto {

    @Min(value = 0)
    @Max(value = 2147483647)
    @JsonProperty("id")
    @ApiModelProperty(required = true)
    private Long id;
    
    @Size(min = 3, max = 3, message = "string.size.invalid")
    @JsonProperty("contentType")
    @ApiModelProperty(required = true, value = "it must have exactly 3 characters")
    private String contentType;
    
    @NotNull
    @Size(max = 50, message = "string.size.invalid")
    @JsonProperty("descr")
    @ApiModelProperty(required = true, value = "description")
    private String descr;

    @NotNull
    @JsonProperty("contentShape")
    private String contentShape;
    
    @JsonProperty("stylesheet")
    private String stylesheet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getContentShape() {
        return contentShape;
    }

    public void setContentShape(String contentShape) {
        this.contentShape = contentShape;
    }

    public String getStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }
}
