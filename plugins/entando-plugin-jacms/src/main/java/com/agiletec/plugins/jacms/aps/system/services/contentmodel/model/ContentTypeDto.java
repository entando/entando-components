package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.DefaultContentModel;
import org.entando.entando.plugins.jacms.aps.system.services.IContent;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Validated
@ApiModel("ContentType")
public class ContentTypeDto extends EntityTypeFullDto {

    @JsonProperty("defaultContentModel")
    private DefaultContentModel defaultContentModel;

    @JsonProperty("defaultContentModelList")
    private DefaultContentModel defaultContentModelList;

    public ContentTypeDto() { super();}

    public ContentTypeDto(IContent src, List<AttributeRole> roles) {
        super(src, roles);
    }

    public ContentTypeDto code(String code) {
        this.setCode(code);
        return this;
    }

    @ApiModelProperty
    public DefaultContentModel getDefaultContentModel() {
        return defaultContentModel;
    }

    public void setDefaultContentModel(DefaultContentModel defaultContentModel) {
        this.defaultContentModel = defaultContentModel;
    }

    public ContentTypeDto defaultContentModelList(DefaultContentModel defaultContentModelList) {
        this.defaultContentModelList = defaultContentModelList;
        return this;
    }

    @ApiModelProperty
    public DefaultContentModel getDefaultContentModelList() {
        return defaultContentModelList;
    }

    public void setDefaultContentModelList(DefaultContentModel defaultContentModelList) {
        this.defaultContentModelList = defaultContentModelList;
    }

    public ContentTypeDto name(String name) {
        setName(name);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentTypeDto contentType = (ContentTypeDto) o;
        return Objects.equals(getAttributes(), contentType.getAttributes()) &&
                Objects.equals(getCode(), contentType.getCode()) &&
                Objects.equals(this.defaultContentModel, contentType.defaultContentModel) &&
                Objects.equals(this.defaultContentModelList, contentType.defaultContentModelList) &&
                Objects.equals(getName(), contentType.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttributes(), getCode(), defaultContentModel, defaultContentModelList, getName());
    }

    @Override
    public String toString() {

        return "class ContentType {\n" +
                "    attributes: " + toIndentedString(getAttributes()) + "\n" +
                "    code: " + toIndentedString(getCode()) + "\n" +
                "    defaultContentModel: " + toIndentedString(defaultContentModel) + "\n" +
                "    defaultContentModelList: " + toIndentedString(defaultContentModelList) + "\n" +
                "    name: " + toIndentedString(getName()) + "\n" +
                "}";
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
}
