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

import com.agiletec.aps.system.services.pagemodel.PageModel;
import java.util.Arrays;
import java.util.Objects;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DigitalExchangePageModel extends PageModel {

    private String digitalExchange;

    @XmlTransient
    public String getDigitalExchange() {
        return digitalExchange;
    }

    public void setDigitalExchange(String digitalExchange) {
        this.digitalExchange = digitalExchange;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", getCode())
                .append("description", getDescription())
                .append("configuration", getConfiguration())
                .append("mainFrame", getMainFrame())
                .append("pluginCode", getPluginCode())
                .append("template", getTemplate())
                .append("digitalExchange", digitalExchange)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DigitalExchangePageModel pageModel = (DigitalExchangePageModel) o;
        return getMainFrame() == pageModel.getMainFrame()
                && Objects.equals(getCode(), pageModel.getCode())
                && Objects.equals(getDescription(), pageModel.getDescription())
                && Arrays.equals(getConfiguration(), pageModel.getConfiguration())
                && Objects.equals(getPluginCode(), pageModel.getPluginCode())
                && Objects.equals(getTemplate(), pageModel.getTemplate())
                && Objects.equals(digitalExchange, pageModel.digitalExchange);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getCode(), getDescription(), getMainFrame(), getPluginCode(), getTemplate(), digitalExchange);
        result = 31 * result + Arrays.hashCode(getConfiguration());
        return result;
    }
}
