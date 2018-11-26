package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.AttributeType;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;

@Validated
public class AttributeDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    private AttributeType type;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("mandatory")
    private Boolean mandatory;

    @JsonProperty("searchable")
    private Boolean searchable;

    @JsonProperty("filterable")
    private Boolean filterable;

    @JsonProperty("contentType")
    private ContentTypeDto contentType;

    @JsonProperty("attributeRoles")
    @Valid
    private List<AttributeRoleDto> attributeRoles;

    public AttributeDto id(Long id) {
        this.id = id;
        return this;
    }

    @ApiModelProperty()
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttributeDto type(AttributeType type) {
        this.type = type;
        return this;
    }

    @ApiModelProperty()
    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public AttributeDto code(String code) {
        this.code = code;
        return this;
    }

    @ApiModelProperty()
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AttributeDto name(String name) {
        this.name = name;
        return this;
    }

    @ApiModelProperty()
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeDto mandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    @ApiModelProperty()
    public Boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public AttributeDto searchable(Boolean searchable) {
        this.searchable = searchable;
        return this;
    }

    @ApiModelProperty()
    public Boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public AttributeDto filterable(Boolean filterable) {
        this.filterable = filterable;
        return this;
    }

    @ApiModelProperty()
    public Boolean isFilterable() {
        return filterable;
    }

    public void setFilterable(Boolean filterable) {
        this.filterable = filterable;
    }

    public AttributeDto contentType(ContentTypeDto contentType) {
        this.contentType = contentType;
        return this;
    }

    @ApiModelProperty()

    @Valid
    public ContentTypeDto getContentType() {
        return contentType;
    }

    public void setContentType(ContentTypeDto contentType) {
        this.contentType = contentType;
    }

    public AttributeDto roles(List<AttributeRoleDto> attributeRoles) {
        this.attributeRoles = attributeRoles;
        return this;
    }

    public AttributeDto addRolesItem(AttributeRoleDto rolesItem) {
        if (this.attributeRoles == null) {
            this.attributeRoles = new ArrayList<>();
        }
        this.attributeRoles.add(rolesItem);
        return this;
    }

    @ApiModelProperty()

    @Valid
    public List<AttributeRoleDto> getAttributeRoles() {
        return attributeRoles;
    }

    public void setAttributeRoles(List<AttributeRoleDto> attributeRoles) {
        this.attributeRoles = attributeRoles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeDto attribute = (AttributeDto) o;
        return Objects.equals(this.id, attribute.id) &&
                Objects.equals(this.type, attribute.type) &&
                Objects.equals(this.code, attribute.code) &&
                Objects.equals(this.name, attribute.name) &&
                Objects.equals(this.mandatory, attribute.mandatory) &&
                Objects.equals(this.searchable, attribute.searchable) &&
                Objects.equals(this.filterable, attribute.filterable) &&
                Objects.equals(this.contentType, attribute.contentType) &&
                Objects.equals(this.attributeRoles, attribute.attributeRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, code, name, mandatory, searchable, filterable, contentType, attributeRoles);
    }

    @Override
    public String toString() {
        return "class AttributeDto {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    code: " + toIndentedString(code) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    mandatory: " + toIndentedString(mandatory) + "\n" +
                "    searchable: " + toIndentedString(searchable) + "\n" +
                "    filterable: " + toIndentedString(filterable) + "\n" +
                "    contentType: " + toIndentedString(contentType) + "\n" +
                "    attributeRoles: " + toIndentedString(attributeRoles) + "\n" +
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

