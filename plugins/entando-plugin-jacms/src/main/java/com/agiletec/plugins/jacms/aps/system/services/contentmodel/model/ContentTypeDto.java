/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import io.swagger.annotations.*;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Validated
@ApiModel("ContentType")
public class ContentTypeDto extends EntityTypeFullDto {

    private String defaultContentModel;

    private String defaultContentModelList;

    private String viewPage;

    public ContentTypeDto() {
        super();
    }

    public ContentTypeDto(Content src, List<AttributeRole> roles) {
        super(src, roles);

        setDefaultContentModel(src.getDefaultModel());
        setDefaultContentModelList(src.getListModel());
        setViewPage(src.getViewPage());
    }

    public ContentTypeDto code(String code) {
        this.setCode(code);
        return this;
    }

    @ApiModelProperty
    public String getDefaultContentModel() {
        return defaultContentModel;
    }

    public void setDefaultContentModel(String defaultContentModel) {
        this.defaultContentModel = defaultContentModel;
    }

    public ContentTypeDto defaultContentModelList(String defaultContentModelList) {
        this.defaultContentModelList = defaultContentModelList;
        return this;
    }

    @ApiModelProperty
    public String getDefaultContentModelList() {
        return defaultContentModelList;
    }

    public void setDefaultContentModelList(String defaultContentModelList) {
        this.defaultContentModelList = defaultContentModelList;
    }

    @ApiModelProperty
    public String getViewPage() {
        return viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
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
        return Objects.equals(getAttributes(), contentType.getAttributes())
                && Objects.equals(getCode(), contentType.getCode())
                && Objects.equals(this.defaultContentModel, contentType.defaultContentModel)
                && Objects.equals(this.defaultContentModelList, contentType.defaultContentModelList)
                && Objects.equals(getName(), contentType.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttributes(), getCode(), defaultContentModel, defaultContentModelList, getName());
    }

    @Override
    public String toString() {

        return "class ContentType {\n"
                + "    attributes: " + toIndentedString(getAttributes()) + "\n"
                + "    code: " + toIndentedString(getCode()) + "\n"
                + "    defaultContentModel: " + toIndentedString(defaultContentModel) + "\n"
                + "    defaultContentModelList: " + toIndentedString(defaultContentModelList) + "\n"
                + "    name: " + toIndentedString(getName()) + "\n"
                + "}";
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
