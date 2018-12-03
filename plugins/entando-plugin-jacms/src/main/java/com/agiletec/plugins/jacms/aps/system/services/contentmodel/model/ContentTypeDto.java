package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.DefaultContentModel;
import org.entando.entando.plugins.jacms.aps.system.persistence.Identifiable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.*;

@Validated
public class ContentTypeDto implements Identifiable<Long> {

    @JsonProperty("id")
    @Min(value = 0, message = "field.min")
    @NotNull(message = "field.notNull")
    private Long id;

    @JsonProperty("code")
    @Size(min = 3, max = 3, message = "string.size.invalid")
    @NotNull(message = "field.notNull")
    private String code;

    @JsonProperty("defaultContentModel")
    @NotNull(message = "field.notNull")
    private DefaultContentModel defaultContentModel;

    @JsonProperty("defaultContentModelList")
    @NotNull(message = "field.notNull")
    private DefaultContentModel defaultContentModelList;

    @JsonProperty("name")
    @Size(min = 3, max = 30, message = "string.size.invalid")
    @NotNull(message = "field.notNull")
    private String name;

    @JsonProperty("attributes")
    @Valid
    private List<AttributeDto> attributes;

    public ContentTypeDto attributes(List<AttributeDto> attributes) {
        this.attributes = attributes;
        return this;
    }

    public ContentTypeDto addAttributesItem(AttributeDto attributesItem) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<AttributeDto>();
        }
        this.attributes.add(attributesItem);
        return this;
    }

    @ApiModelProperty
    @Valid
    public List<AttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDto> attributes) {
        this.attributes = attributes;
    }

    public ContentTypeDto code(String code) {
        this.code = code;
        return this;
    }

    @ApiModelProperty
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ContentTypeDto defaultContentModel(DefaultContentModel defaultContentModel) {
        this.defaultContentModel = defaultContentModel;
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

    public ContentTypeDto id(Long id) {
        this.id = id;
        return this;
    }

    @ApiModelProperty
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public ContentTypeDto name(String name) {
        this.name = name;
        return this;
    }

    @ApiModelProperty
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
        return Objects.equals(this.attributes, contentType.attributes) &&
                Objects.equals(this.code, contentType.code) &&
                Objects.equals(this.defaultContentModel, contentType.defaultContentModel) &&
                Objects.equals(this.defaultContentModelList, contentType.defaultContentModelList) &&
                Objects.equals(this.id, contentType.id) &&
                Objects.equals(this.name, contentType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, code, defaultContentModel, defaultContentModelList, id, name);
    }

    @Override
    public String toString() {

        return "class ContentType {\n" +
                "    attributes: " + toIndentedString(attributes) + "\n" +
                "    code: " + toIndentedString(code) + "\n" +
                "    defaultContentModel: " + toIndentedString(defaultContentModel) + "\n" +
                "    defaultContentModelList: " + toIndentedString(defaultContentModelList) + "\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
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
