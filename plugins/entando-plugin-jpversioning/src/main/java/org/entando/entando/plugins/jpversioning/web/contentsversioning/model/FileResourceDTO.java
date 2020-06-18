package org.entando.entando.plugins.jpversioning.web.contentsversioning.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import org.entando.entando.plugins.jacms.web.resource.model.AssetDto;

public class FileResourceDTO extends ResourceDTO {
    public static final String RESOURCE_TYPE = "file";

    private String size;
    private String path;

    @Builder
    public FileResourceDTO(String id, String name, String description, Date createdAt, Date updatedAt,
                         String group, @Singular List<String> categories, String size, String path, String owner, String folderPath) {
        super(id, RESOURCE_TYPE, name, description, createdAt, updatedAt, group, categories, owner, folderPath);
        this.size = size;
        this.path = path;
    }
}
