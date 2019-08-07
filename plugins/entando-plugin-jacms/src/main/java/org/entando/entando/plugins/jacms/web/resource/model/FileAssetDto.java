package org.entando.entando.plugins.jacms.web.resource.model;

import lombok.Data;

@Data
public class FileAssetDto extends AssetDto {
    private String path;
    private String size;
}
