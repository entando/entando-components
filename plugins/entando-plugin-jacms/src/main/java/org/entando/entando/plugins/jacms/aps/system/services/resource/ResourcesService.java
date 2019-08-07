package org.entando.entando.plugins.jacms.aps.system.services.resource;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsException;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.*;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.util.IImageDimensionReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.web.resource.model.FileAssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageAssetDto;
import org.entando.entando.plugins.jacms.web.resource.model.ImageMetadataDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Slf4j
@Service
public class ResourcesService {
    @Autowired
    private IResourceManager resourceManager;

    @Autowired
    private IImageDimensionReader imageDimensionManager;

    public PagedMetadata<ImageAssetDto> listImageAssets(RestListRequest requestList) {
        List<ImageAssetDto> assets = new ArrayList<>();
        try {
            List<String> resourceIds = resourceManager.searchResourcesId(createSearchFilters("Image"), null, null);

            for(String id : resourceIds) {
                ImageResource resource = (ImageResource) resourceManager.loadResource(id);

                ImageAssetDto.ImageAssetDtoBuilder builder = ImageAssetDto.builder()
                        .name(resource.getMasterFileName())
                        .description(resource.getDescription())
                        .createdAt(resource.getCreationDate())
                        .updatedAt(resource.getLastModified())
                        .version(ImageMetadataDto.builder()
                            .path(resource.getImagePath("0"))
                            .size(resource.getDefaultInstance().getFileLength())
                            .build());

                for (ImageResourceDimension dimensions : getImageDimensions()) {
                    ResourceInstance instance = resource.getInstance(dimensions.getIdDim(), null);

                    if (instance == null) {
                        log.warn("ResourceInstance not found for dimensions id {} and image id {}", dimensions.getIdDim(), id);
                        continue;
                    }

                    builder.version(ImageMetadataDto.builder()
                            .path(resource.getImagePath(String.valueOf(dimensions.getIdDim())))
                            .size(instance.getFileLength())
                            .dimensions(String.format("%dx%d px", dimensions.getDimx(), dimensions.getDimy()))
                            .build());
                }

                assets.add(builder.build());
            }

        } catch (ApsException e) {
            throw new RestServerError("Error listing image resources", e);
        }

        SearcherDaoPaginatedResult<ImageAssetDto> paginatedResult = new SearcherDaoPaginatedResult<>(assets.size(), assets);
        PagedMetadata<ImageAssetDto> pagedResults = new PagedMetadata<>(requestList, paginatedResult);
        pagedResults.setBody(assets);

        return pagedResults;
    }

    private List<ImageResourceDimension> getImageDimensions() {
        Map<Integer, ImageResourceDimension> master = imageDimensionManager.getImageDimensions();
        List<ImageResourceDimension> dimensions = new ArrayList<>(master.values());
        BeanComparator comparator = new BeanComparator("dimx");
        Collections.sort(dimensions, comparator);
        return dimensions;
    }

    private FieldSearchFilter[] createSearchFilters(String type) {
        FieldSearchFilter typeCodeFilter = new FieldSearchFilter(IResourceManager.RESOURCE_TYPE_FILTER_KEY, type, false);
        FieldSearchFilter[] filters = {typeCodeFilter};
        /*if (!StringUtils.isBlank(this.getOwnerGroupName())) {
            FieldSearchFilter groupFilter = new FieldSearchFilter(IResourceManager.RESOURCE_MAIN_GROUP_FILTER_KEY, this.getOwnerGroupName(), false);
            filters = ArrayUtils.add(filters, groupFilter);
        }
        if (!StringUtils.isBlank(this.getText())) {
            FieldSearchFilter textFilter = new FieldSearchFilter(IResourceManager.RESOURCE_DESCR_FILTER_KEY, this.getText(), true);
            filters = ArrayUtils.add(filters, textFilter);
        }
        if (!StringUtils.isBlank(this.getFileName())) {
            FieldSearchFilter filenameFilter = new FieldSearchFilter(IResourceManager.RESOURCE_FILENAME_FILTER_KEY, this.getFileName(), true);
            filters = ArrayUtils.add(filters, filenameFilter);
        }
        filters = ArrayUtils.add(filters, this.getOrderFilter());*/
        return filters;
    }

    public PagedMetadata<String> listFileAssets(RestListRequest requestList) {
        return null;
    }

    public void createImageAsset(MultipartFile asset) {
        //TODO handle possible formats from config file
        BaseResourceDataBean resourceFile = new BaseResourceDataBean();

        try {
            resourceFile.setInputStream(asset.getInputStream());
            resourceFile.setFileSize(asset.getBytes().length / 1000);
            resourceFile.setFileName(asset.getOriginalFilename());
            resourceFile.setMimeType(asset.getContentType());
            resourceFile.setDescr(asset.getOriginalFilename());
            resourceFile.setMainGroup("free"); //TODO
            resourceFile.setResourceType("Image");
            resourceFile.setCategories(new ArrayList<>()); //TODO
            resourceFile.setMetadata(new HashMap<>()); //TODO

            resourceManager.addResource(resourceFile);
        } catch (ApsSystemException e) {
            throw new RestServerError("Error saving new image resource", e);
        } catch (IOException e) {
            throw new RestServerError("Error reading image stream", e);
        }
    }

    /*protected Map getImgMetadata(File file) {
        logger.debug("Get image Metadata in Resource Action");
        Map<String, String> meta = new HashMap<>();
        ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            String ignoreKeysConf = resourcePrototype.getMetadataIgnoreKeys();
            String[] ignoreKeys = null;
            if (null != ignoreKeysConf) {
                ignoreKeys = ignoreKeysConf.split(",");
                logger.debug("Metadata ignoreKeys: {}", ignoreKeys);
            } else {
                logger.debug("Metadata ignoreKeys not configured");
            }
            List<String> ignoreKeysList = new ArrayList<String>();
            if (null != ignoreKeys) {
                ignoreKeysList = Arrays.asList(ignoreKeys);
            }
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (!ignoreKeysList.contains(tag.getTagName())) {
                        logger.debug("Add Metadata with key: {}", tag.getTagName());
                        meta.put(tag.getTagName(), tag.getDescription());
                    } else {
                        logger.debug("Skip Metadata key {}", tag.getTagName());
                    }
                }
            }
        } catch (ImageProcessingException ex) {
            logger.error("Error reading metadata");
        } catch (IOException ioex) {
            logger.error("Error reading file");
        }
        return meta;
    }*/

    public FileAssetDto createFileAsset(MultipartFile asset) {
        return null;
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
