package org.entando.entando.plugins.jacms.web.resource.model;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ImageAssetDto extends AssetDto {
    private List<ImageMetadataDto> versions;

    private Map<String, String> metadata;

    @Builder
    public ImageAssetDto(String name, String description, Date createdAt, Date updatedAt,
             @Singular List<ImageMetadataDto> versions, String group, @Singular List<String> categories, Map<String,String> metadata) {
        super(name, description, createdAt, updatedAt, group, categories);
        this.versions = versions;
        this.metadata = metadata;
    }
}
