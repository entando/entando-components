package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * AttributeRole
 */
@Validated
public class AttributeRoleDto {
    @JsonProperty("attribute")
    private AttributeDto attribute = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    public AttributeRoleDto attribute(AttributeDto attribute) {
        this.attribute = attribute;
        return this;
    }

    /**
     * Get attribute
     *
     * @return attribute
     **/
    @ApiModelProperty(value = "")

    @Valid
    public AttributeDto getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeDto attribute) {
        this.attribute = attribute;
    }

    public AttributeRoleDto id(Long id) {
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

    public AttributeRoleDto name(String name) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeRoleDto attributeRole = (AttributeRoleDto) o;
        return Objects.equals(this.attribute, attributeRole.attribute) &&
                Objects.equals(this.id, attributeRole.id) &&
                Objects.equals(this.name, attributeRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, id, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttributeRole {\n");

        sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
