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
package org.entando.entando.web.plugins.jacms.contentmodel.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class ContentModelRequest {

    @Min(value = 0)
    private Long id;

    @Size(min = 3, max = 3, message = "string.size.invalid")
    @NotBlank(message = "contentmodel.contentType.notBlank")
    private String contentType;

    @Size(max = 50, message = "string.size.invalid")
    @NotBlank(message = "contentmodel.descr.notBlank")
    private String descr;

    private String contentShape;
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
