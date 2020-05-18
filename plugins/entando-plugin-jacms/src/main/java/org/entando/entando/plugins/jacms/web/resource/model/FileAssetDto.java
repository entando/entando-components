package org.entando.entando.plugins.jacms.web.resource.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileAssetDto extends AssetDto {
    public static final String RESOURCE_TYPE = "file";

    private String size;
    private String path;

    @Builder
    public FileAssetDto(String id, String name, String description, Date createdAt, Date updatedAt,
                         String group, @Singular List<String> categories, String size, String path, String owner, String folderPath) {
        super(id, RESOURCE_TYPE, name, description, createdAt, updatedAt, group, categories, owner, folderPath);
        this.size = size;
        this.path = path;
    }
}
