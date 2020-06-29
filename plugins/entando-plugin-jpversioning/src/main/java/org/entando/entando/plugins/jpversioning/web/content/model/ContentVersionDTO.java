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
package org.entando.entando.plugins.jpversioning.web.content.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import java.util.Date;
import java.util.Objects;
import org.entando.entando.web.common.json.JsonDateDeserializer;
import org.entando.entando.web.common.json.JsonDateSerializer;

@ApiModel("ContentVersion")
public class ContentVersionDTO {

    private long id;
    private String contentId;
    private String contentType;
    private String description;
    private String status;
    private String version;
    private int onlineVersion;
    private boolean approved;
    private String username;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date versionDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(Date versionDate) {
        this.versionDate = versionDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getOnlineVersion() {
        return onlineVersion;
    }

    public void setOnlineVersion(int onlineVersion) {
        this.onlineVersion = onlineVersion;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ContentVersionDTO{" +
                "id=" + id +
                ", contentId='" + contentId + '\'' +
                ", contentType='" + contentType + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", versionDate=" + versionDate +
                ", version='" + version + '\'' +
                ", onlineVersion=" + onlineVersion +
                ", approved=" + approved +
                ", username='" + username + '\'' +
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
        ContentVersionDTO that = (ContentVersionDTO) o;
        return id == that.id &&
                onlineVersion == that.onlineVersion &&
                approved == that.approved &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(description, that.description) &&
                Objects.equals(status, that.status) &&
                Objects.equals(versionDate, that.versionDate) &&
                Objects.equals(version, that.version) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(id, contentId, contentType, description, status, versionDate, version, onlineVersion, approved,
                        username);
    }
}
