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
package org.entando.entando.web.digitalexchange.component;

import com.agiletec.aps.system.SystemConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

@Validated
public class DigitalExchangeComponent {

    @NotNull
    @Size(min = 1, max = 30)
    private String id;

    @NotNull
    @Size(min = 1, max = 30)
    private String name;

    @NotNull
    @Size(min = 1, max = 30)
    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SystemConstants.API_DATE_FORMAT)
    private Date lastUpdate;

    private String version;
    private String description;
    private String image;
    private double rating;
    private boolean installed;
    private String digitalExchangeName;

    // NOTE: the id can be removed when we will return the installation link
    // following the HATEOAS principle
    private String digitalExchangeId;

    private String signature;

    public String getId() {
        return id;
    }

    public DigitalExchangeComponent setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DigitalExchangeComponent setName(String name) {
        this.name = name;
        return this;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public DigitalExchangeComponent setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public DigitalExchangeComponent setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getType() {
        return type;
    }

    public DigitalExchangeComponent setType(String type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DigitalExchangeComponent setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public DigitalExchangeComponent setImage(String image) {
        this.image = image;
        return this;
    }

    public double getRating() {
        return rating;
    }

    public DigitalExchangeComponent setRating(double rating) {
        this.rating = rating;
        return this;
    }

    public boolean isInstalled() {
        return installed;
    }

    public DigitalExchangeComponent setInstalled(boolean installed) {
        this.installed = installed;
        return this;
    }

    /**
     * The name of the Digital Exchange providing the component.
     */
    public String getDigitalExchangeName() {
        return digitalExchangeName;
    }

    public DigitalExchangeComponent setDigitalExchangeName(String digitalExchangeName) {
        this.digitalExchangeName = digitalExchangeName;
        return this;
    }

    public String getDigitalExchangeId() {
        return digitalExchangeId;
    }

    public DigitalExchangeComponent setDigitalExchangeId(String digitalExchangeId) {
        this.digitalExchangeId = digitalExchangeId;
        return this;
    }

    /**
     * RSA signature of the installation package encoded using Base64.
     */
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object other) {
        return reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("lastUpdate", lastUpdate)
                .append("version", version)
                .append("type", type)
                .append("description", description)
                .append("image", image)
                .append("rating", rating)
                .append("installed", installed)
                .append("digitalExchangeName", digitalExchangeName)
                .append("digitalExchangeId", digitalExchangeId)
                .append("signature", signature)
                .toString();
    }
}
