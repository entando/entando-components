/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.digitalexchange.rating;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.entando.entando.web.common.model.RestError;

public class DEComponentRatingResult {

    private boolean ratingUnsupported;
    private DERatingsSummary ratingsSummary;
    private List<RestError> errors = new ArrayList<>();

    public void setRatingUnsupported() {
        this.ratingUnsupported = true;
    }

    public void setRatingsSummary(DERatingsSummary ratingsSummary) {
        this.ratingsSummary = ratingsSummary;
    }

    public boolean isRatingSupported() {
        return !ratingUnsupported;
    }

    public DERatingsSummary getRatingsSummary() {
        return ratingsSummary;
    }

    public List<RestError> getErrors() {
        return errors;
    }

    public void setErrors(List<RestError> errors) {
        this.errors = errors;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
}
