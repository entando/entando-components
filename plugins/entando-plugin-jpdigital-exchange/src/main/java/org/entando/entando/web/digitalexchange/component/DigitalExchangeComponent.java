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

import java.util.Date;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class DigitalExchangeComponent {

    private String id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SystemConstants.API_DATE_FORMAT)
    private Date lastUpdate;
    private String version;
    private String type;
    private String description;
    private String image;
    private double rating;
    private boolean installed;
    private String digitalExchangeName;

    // NOTE: the id can be removed when we will return the installation link
    // following the HATEOAS principle
    private String digitalExchangeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    /**
     * The name of the Digital Exchange providing the component.
     */
    public String getDigitalExchangeName() {
        return digitalExchangeName;
    }

    public void setDigitalExchangeName(String digitalExchangeName) {
        this.digitalExchangeName = digitalExchangeName;
    }

    public String getDigitalExchangeId() {
        return digitalExchangeId;
    }

    public void setDigitalExchangeId(String digitalExchangeId) {
        this.digitalExchangeId = digitalExchangeId;
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
                .toString();
    }
}
