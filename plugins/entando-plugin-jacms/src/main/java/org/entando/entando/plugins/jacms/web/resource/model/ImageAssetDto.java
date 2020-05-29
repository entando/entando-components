package org.entando.entando.plugins.jacms.web.resource.model;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ImageAssetDto extends AssetDto {
    public static final String RESOURCE_TYPE = "image";

    private List<ImageMetadataDto> versions;

    private Map<String, String> metadata;

    @Builder
    public ImageAssetDto(String id, String name, String description, Date createdAt, Date updatedAt,
             @Singular List<ImageMetadataDto> versions, String group, @Singular List<String> categories,
            Map<String,String> metadata, String owner, String folderPath) {
        super(id, RESOURCE_TYPE, name, description, createdAt, updatedAt, group, categories, owner, folderPath);
        this.versions = versions;
        this.metadata = metadata;
    }
}
