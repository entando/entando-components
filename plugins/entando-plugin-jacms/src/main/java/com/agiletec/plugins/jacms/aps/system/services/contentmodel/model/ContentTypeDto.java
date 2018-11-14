package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;

/**
 * ContentType
 */
@Validated
public class ContentTypeDto {
    @JsonProperty("attributes")
    @Valid
    private List<AttributeDto> attributes = null;

    @JsonProperty("code")
    private String code = null;

    /**
     * Gets or Sets defaultContentModel
     */
    public enum DefaultContentModelEnum {
        FULL("Full"),
        LISTS("Lists");

        private String value;

        DefaultContentModelEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DefaultContentModelEnum fromValue(String text) {
            for (DefaultContentModelEnum b : DefaultContentModelEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("defaultContentModel")
    private DefaultContentModelEnum defaultContentModel = null;

    @JsonProperty("defaultContentModelList")
    private DefaultContentModelEnum defaultContentModelList = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

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

    /**
     * Get attributes
     *
     * @return attributes
     **/
    @ApiModelProperty(value = "")
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

    public ContentTypeDto defaultContentModel(DefaultContentModelEnum defaultContentModel) {
        this.defaultContentModel = defaultContentModel;
        return this;
    }

    /**
     * Get defaultContentModel
     *
     * @return defaultContentModel
     **/
    @ApiModelProperty(value = "")
    public DefaultContentModelEnum getDefaultContentModel() {
        return defaultContentModel;
    }

    public void setDefaultContentModel(DefaultContentModelEnum defaultContentModel) {
        this.defaultContentModel = defaultContentModel;
    }

    public ContentTypeDto defaultContentModelList(DefaultContentModelEnum defaultContentModelList) {
        this.defaultContentModelList = defaultContentModelList;
        return this;
    }

    /**
     * Get defaultContentModelList
     *
     * @return defaultContentModelList
     **/
    @ApiModelProperty(value = "")


    public DefaultContentModelEnum getDefaultContentModelList() {
        return defaultContentModelList;
    }

    public void setDefaultContentModelList(DefaultContentModelEnum defaultContentModelList) {
        this.defaultContentModelList = defaultContentModelList;
    }

    public ContentTypeDto id(Long id) {
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

    public ContentTypeDto name(String name) {
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
        StringBuilder sb = new StringBuilder();
        sb.append("class ContentType {\n");

        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    defaultContentModel: ").append(toIndentedString(defaultContentModel)).append("\n");
        sb.append("    defaultContentModelList: ").append(toIndentedString(defaultContentModelList)).append("\n");
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
