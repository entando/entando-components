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
package org.entando.entando.aps.system.services.pagemodel.model;

import java.util.Objects;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DigitalExchangePageModelDto extends PageModelDto {

    private String digitalExchange;

    public DigitalExchangePageModelDto(PageModelDto other) {
        super(other);
    }

    public DigitalExchangePageModelDto(DigitalExchangePageModelDto other) {
        super(other);
        this.digitalExchange = other.digitalExchange;
    }

    public String getDigitalExchange() {
        return digitalExchange;
    }

    public void setDigitalExchange(String digitalExchange) {
        this.digitalExchange = digitalExchange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DigitalExchangePageModelDto that = (DigitalExchangePageModelDto) o;
        return super.equals(o)
                && Objects.equals(digitalExchange, that.digitalExchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getDescr(), getConfiguration(), getMainFrame(), getPluginCode(), getTemplate(), digitalExchange, getReferences());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", getCode())
                .append("descr", getDescr())
                .append("configuration", getConfiguration())
                .append("mainFrame", getMainFrame())
                .append("pluginCode", getPluginCode())
                .append("template", getTemplate())
                .append("digitalExchange", digitalExchange)
                .append("references", getReferences())
                .toString();
    }
}
