package org.entando.entando.plugins.jpversioning.web.resource.model;

import java.util.Date;
import java.util.List;

public class ImageResourceDTO extends ResourceDTO {
    public static final String RESOURCE_TYPE = "image";

    private List<ImageVersionDTO> versions;

    public ImageResourceDTO(String id, String name, String description, Date createdAt, Date updatedAt,
            List<ImageVersionDTO> versions, String group, List<String> categories, String owner, String folderPath) {
        super(id, RESOURCE_TYPE, name, description, createdAt, updatedAt, group, categories, owner, folderPath);
        this.versions = versions;
    }

    public List<ImageVersionDTO> getVersions() {
        return versions;
    }

    public void setVersions(
            List<ImageVersionDTO> versions) {
        this.versions = versions;
    }
}
