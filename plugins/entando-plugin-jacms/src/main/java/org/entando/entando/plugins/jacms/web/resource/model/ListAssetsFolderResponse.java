package org.entando.entando.plugins.jacms.web.resource.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListAssetsFolderResponse {
    private List<AssetDto> assets;
    private String folderPath;
    private List<String> subfolders;
}
