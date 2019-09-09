package org.entando.entando.plugins.jacms.web.resource.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageMetadataDto {
    private String dimensions;
    private String path;
    private String size;
}
