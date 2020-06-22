package org.entando.entando.plugins.jpversioning.web.resource.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;

public class FileResourceDTO extends ResourceDTO {
    public static final String RESOURCE_TYPE = "file";

    private String size;
    private String path;

    @Builder
    public FileResourceDTO(String id, String name, String description, Date createdAt, Date updatedAt,
                         String group, List<String> categories, String size, String path, String owner, String folderPath) {
        super(id, RESOURCE_TYPE, name, description, createdAt, updatedAt, group, categories, owner, folderPath);
        this.size = size;
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
