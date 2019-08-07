package org.entando.entando.plugins.jacms.web.resource.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ImageAssetDto extends AssetDto {
    private List<ImageMetadataDto> versions;

    @Builder
    public ImageAssetDto(String name, String description, Date createdAt, Date updatedAt, @Singular List<ImageMetadataDto> versions){
        super(name, description, createdAt, updatedAt);
        this.versions = versions;
    }
}
