package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;

/**
 * AttributeDto
 */
@Validated
public class AttributeDto {
    @JsonProperty("id")
    private Long id = null;

    /**
     * Gets or Sets type
     */
    public enum TypeEnum {
        ATTACH("Attach"),

        AUTHOR("Author"),

        BOOLEAN("Boolean"),

        CHECKBOX("CheckBox"),

        COMPOSITE("Composite"),

        COORDS("Coords"),

        DATE("Date"),

        ENUMERATOR("Enumerator"),

        ENUMERATORMAP("EnumeratorMap"),

        HYPERTEXT("Hypertext"),

        IMAGE("Image"),

        LINK("Link"),

        LIST("List"),

        LONGTEXT("Longtext"),

        MONOLIST("Monolist"),

        MONOTEXT("Monotext"),

        NUMBER("Number"),

        TEXT("Text"),

        THREESTATE("ThreeState"),

        TIME("Time"),

        TIMESTAMP("Timestamp");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static TypeEnum fromValue(String text) {
            for (TypeEnum b : TypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("type")
    private TypeEnum type = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("mandatory")
    private Boolean mandatory = null;

    @JsonProperty("searchable")
    private Boolean searchable = null;

    @JsonProperty("filterable")
    private Boolean filterable = null;

    @JsonProperty("contentType")
    private ContentTypeDto contentType = null;

    @JsonProperty("attributeRoles")
    @Valid
    private List<AttributeRoleDto> attributeRoles = null;

    public AttributeDto id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @ApiModelProperty(value = "")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttributeDto type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    @ApiModelProperty(value = "")
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public AttributeDto code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Get code
     *
     * @return code
     **/
    @ApiModelProperty(value = "")
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

    /**
     * Get name
     *
     * @return name
     **/
    @ApiModelProperty(value = "")
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

    /**
     * Get mandatory
     *
     * @return mandatory
     **/
    @ApiModelProperty(value = "")
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

    /**
     * Get searchable
     *
     * @return searchable
     **/
    @ApiModelProperty(value = "")
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

    /**
     * Get filterable
     *
     * @return filterable
     **/
    @ApiModelProperty(value = "")
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

    /**
     * Get contentType
     *
     * @return contentType
     **/
    @ApiModelProperty(value = "")

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
            this.attributeRoles = new ArrayList<AttributeRoleDto>();
        }
        this.attributeRoles.add(rolesItem);
        return this;
    }

    /**
     * Get attributeRoles
     *
     * @return attributeRoles
     **/
    @ApiModelProperty(value = "")

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
        StringBuilder sb = new StringBuilder();
        sb.append("class AttributeDto {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    mandatory: ").append(toIndentedString(mandatory)).append("\n");
        sb.append("    searchable: ").append(toIndentedString(searchable)).append("\n");
        sb.append("    filterable: ").append(toIndentedString(filterable)).append("\n");
        sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
        sb.append("    attributeRoles: ").append(toIndentedString(attributeRoles)).append("\n");
        sb.append("}");
        return sb.toString();
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

