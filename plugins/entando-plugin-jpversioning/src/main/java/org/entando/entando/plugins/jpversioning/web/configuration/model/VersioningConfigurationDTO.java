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
package org.entando.entando.plugins.jpversioning.web.configuration.model;

import io.swagger.annotations.ApiModel;
import java.util.List;
import java.util.Objects;

@ApiModel("VersioningConfiguration")
public class VersioningConfigurationDTO {

    private Boolean deleteMidVersions;
    private List<String> contentsToIgnore;
    private List<String> contentTypesToIgnore;

    public Boolean getDeleteMidVersions() {
        return deleteMidVersions;
    }

    public void setDeleteMidVersions(Boolean deleteMidVersions) {
        this.deleteMidVersions = deleteMidVersions;
    }

    public List<String> getContentsToIgnore() {
        return contentsToIgnore;
    }

    public void setContentsToIgnore(List<String> contentsToIgnore) {
        this.contentsToIgnore = contentsToIgnore;
    }

    public List<String> getContentTypesToIgnore() {
        return contentTypesToIgnore;
    }

    public void setContentTypesToIgnore(List<String> contentTypesToIgnore) {
        this.contentTypesToIgnore = contentTypesToIgnore;
    }

    @Override
    public String toString() {
        return "ConfigurationVersioningDTO{" +
                "deleteMidVersions=" + deleteMidVersions +
                ", contentsToIgnore=" + contentsToIgnore +
                ", contentTypesToIgnore=" + contentTypesToIgnore +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VersioningConfigurationDTO that = (VersioningConfigurationDTO) o;
        return Objects.equals(deleteMidVersions, that.deleteMidVersions) &&
                Objects.equals(contentsToIgnore, that.contentsToIgnore) &&
                Objects.equals(contentTypesToIgnore, that.contentTypesToIgnore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteMidVersions, contentsToIgnore, contentTypesToIgnore);
    }
}
