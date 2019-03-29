/*
 * Copyright 2019 - Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import java.util.Objects;

public class DERatingsSummary {

    private String componentId;
    private double rating;
    private int numberOfRatings;

    public DERatingsSummary() {
    }

    public DERatingsSummary(String componentId, double rating, int numberOfRatings) {
        this.componentId = componentId;
        this.rating = rating;
        this.numberOfRatings = numberOfRatings;
    }

    /**
     * Necessary for instantiating a new summary from JPQL.
     */
    public DERatingsSummary(String componentId, Double rating, Long numberOfRatings) {
        this(componentId, rating, numberOfRatings.intValue());
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DERatingsSummary that = (DERatingsSummary) o;
        return rating == that.rating
                && numberOfRatings == that.numberOfRatings
                && Objects.equals(componentId, that.componentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentId, rating, numberOfRatings);
    }

    @Override
    public String toString() {
        return "DERatingsSummary{"
                + "componentId='" + componentId + '\''
                + ", rating=" + rating
                + ", numberOfRatings=" + numberOfRatings
                + '}';
    }
}
