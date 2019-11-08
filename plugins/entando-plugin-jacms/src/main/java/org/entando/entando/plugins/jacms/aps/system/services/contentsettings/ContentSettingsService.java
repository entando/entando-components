/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jacms.aps.system.services.contentsettings;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.LastReloadInfo;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsDto;
import org.entando.entando.plugins.jacms.web.contentsettings.model.LastReloadInfoDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.entando.entando.plugins.jacms.web.contentsettings.ContentSettingsController.*;

@Service
public class ContentSettingsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentManager contentManager;

    @Autowired
    private ICmsSearchEngineManager searchEngineManager;

    @Autowired
    private IResourceManager resourceManager;

    @Autowired
    private ConfigInterface configManager;

    public ContentSettingsDto getContentSettings() {
        ContentSettingsDto dto = new ContentSettingsDto();

        dto.setReferencesStatus(getContentManagerStatus());
        dto.setIndexesStatus(getSearcherManagerStatus());

        LastReloadInfo lastReloadInfo = getLastReloadInfo();
        dto.setIndexesLastReloadInfo(lastReloadInfo == null ? null : LastReloadInfoDto.builder()
                .date(lastReloadInfo.getDate())
                .result(lastReloadInfo.getResult())
                .build());

        dto.setMetadata(listMetadata());
        dto.setCropRatios(listCropRatios());
        dto.setEditor(getEditor());

        return dto;
    }

    public Map<String, List<String>> listMetadata() {
        return resourceManager.getMetadataMapping();
    }

    private void saveMetadata(Map<String, List<String>> metadata) {
        try {
            resourceManager.updateMetadataMapping(metadata);
        } catch (ApsSystemException ex) {
            throw new RestServerError("plugins.jacms.contentsettings.error.metadata.save", ex);
        }
    }

    public Map<String, List<String>> addMetadata(String key, String mapping) {
        Map<String, List<String>> metadata = listMetadata();

        validateMetadata(metadata, key, mapping);
        validateMetadataNotExists(metadata, key);

        List<String> newMetadata = new ArrayList<>(Arrays.asList(mapping.trim().split(",")));
        metadata.put(key.trim(), newMetadata);

        saveMetadata(metadata);

        return metadata;
    }

    public Map<String, List<String>> editMetadata(String key, String mapping) {
        Map<String, List<String>> metadata = listMetadata();

        validateMetadata(metadata, key, mapping);
        validateMetadataExists(metadata, key);

        List<String> newMetadata = new ArrayList<>(Arrays.asList(mapping.trim().split(",")));
        metadata.put(key.trim(), newMetadata);

        saveMetadata(metadata);

        return metadata;
    }

    public Map<String, List<String>> removeMetadata(String key)  {
        Map<String, List<String>> metadata = listMetadata();

        validateMetadataExists(metadata, key);

        if (!StringUtils.isBlank(key)) {
            metadata.remove(key.trim());
        }

        saveMetadata(metadata);

        return metadata;
    }

    public void reloadContentsIndex() {
        try {
            searchEngineManager.startReloadContentsReferences();
        } catch (ApsSystemException ex) {
            throw new RestServerError("plugins.jacms.contentsettings.error.reloadReference", ex);
        }

        logger.info("Reload contents index started");
    }

    public void reloadContentsReference() {
        contentManager.reloadEntitiesReferences(null);
        logger.info("Reload contents reference started");
    }

    public int getContentManagerStatus() {
        return contentManager.getStatus();
    }

    public int getSearcherManagerStatus() {
        return searchEngineManager.getStatus();
    }

    public LastReloadInfo getLastReloadInfo() {
        return searchEngineManager.getLastReloadInfo();
    }

    public List<String> listCropRatios() {
        String params = getSystemParams().get(JacmsSystemConstants.CONFIG_PARAM_ASPECT_RATIO);
        if(params == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(Optional.ofNullable(params).orElse("").split(";"))
                .filter(item -> item != null && !item.trim().isEmpty())
                .collect(Collectors.toList());
    }

    public List<String> addCropRatio(String ratio) {
        List<String> cropRatios = listCropRatios();
        validateCropRatio(cropRatios, ratio);
        validateCropRatioNotExists(cropRatios, ratio);

        cropRatios.add(ratio);

        saveCropRatios(cropRatios);

        return listCropRatios();
    }

    public List<String> editCropRatio(String ratio, String newRatio) {
        List<String> cropRatios = listCropRatios();
        validateCropRatio(cropRatios, ratio);
        validateCropRatioExists(cropRatios, ratio);

        cropRatios.remove(ratio);
        cropRatios.add(newRatio);

        saveCropRatios(cropRatios);

        return listCropRatios();
    }

    public List<String> removeCropRatio(String ratio) {
        List<String> cropRatios = listCropRatios();

        validateCropRatioExists(cropRatios, ratio);
        cropRatios.remove(ratio);

        saveCropRatios(cropRatios);

        return listCropRatios();
    }

    private void saveCropRatios(List<String> cropRatios){
        Map<String,String> systemParams = getSystemParams();
        systemParams.put(JacmsSystemConstants.CONFIG_PARAM_ASPECT_RATIO, String.join(";", cropRatios));

        saveSystemParams(systemParams);
    }

    private void saveSystemParams(Map<String,String> systemParams) {
        try {
            String xmlParams = SystemParamsUtils.getNewXmlParams(getConfigParameter(), systemParams);
            configManager.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, xmlParams);
        } catch (Exception ex) {
            throw new RestServerError("plugins.jacms.contentsettings.error.cropRatio.save", ex);
        }
    }

    public ContentSettingsDto.Editor getEditor() {
        String editor = getSystemParams().get("hypertextEditor");

        return ContentSettingsDto.Editor.fromValue(editor);
    }

    public ContentSettingsDto.Editor setEditor(String key) {
        Map<String,String> systemParams = getSystemParams();

        ContentSettingsDto.Editor editor = null;

        try {
            editor = ContentSettingsDto.Editor.fromValue(key);
        } catch (IllegalArgumentException ex) {
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(key, "contentSettings.editor");
            errors.reject(ERRCODE_INVALID_EDITOR, "plugins.jacms.contentsettings.error.invalidEditor");
            throw new ValidationGenericException(errors);
        }

        systemParams.put("hypertextEditor", editor.getKey());
        saveSystemParams(systemParams);

        return getEditor();
    }

    public void validateMetadata(Map<String,List<String>> metadata, String key, String mapping) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(key, "contentSettings.metadata");

        Pattern regex = Pattern.compile("[a-z0-9_]+");
        if (StringUtils.isBlank(key) || StringUtils.isBlank(mapping) || !regex.matcher(key).matches()) {
            errors.reject(ERRCODE_INVALID_METADATA, "plugins.jacms.contentsettings.error.metadata.invalid");
            throw new ValidationGenericException(errors);
        }

        for(String value : mapping.split(",")) {
            if (!regex.matcher(value.trim()).matches()) {
                errors.reject(ERRCODE_INVALID_METADATA, "plugins.jacms.contentsettings.error.metadata.invalid");
                throw new ValidationGenericException(errors);
            }
        }
    }

    public void validateMetadataNotExists(Map<String,List<String>> metadata, String key) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(key, "contentSettings.metadata");

        if (metadata.containsKey(key)) {
            errors.reject(ERRCODE_CONFLICT_METADATA, "plugins.jacms.contentsettings.error.metadata.conflict");
            throw new ValidationConflictException(errors);
        }
    }

    public void validateMetadataExists(Map<String,List<String>> metadata, String key) {
        if (!metadata.containsKey(key)) {
            throw new ResourceNotFoundException(ERRCODE_NOT_FOUND_METADATA, "metadata", key);
        }
    }

    public void validateCropRatio(List<String> cropRatios, String ratio) {
        final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(ratio, "contentSettings.ratio");

        String[] split = Optional.ofNullable(ratio)
                .orElseThrow(() -> {
                    errors.reject(ERRCODE_INVALID_CROP_RATIO, "plugins.jacms.contentsettings.error.cropRatio.invalid");
                    return new ValidationGenericException(errors);
                })
                .split(":");

        if (split.length != 2) {
            errors.reject(ERRCODE_INVALID_CROP_RATIO, "plugins.jacms.contentsettings.error.cropRatio.invalid");
            throw new ValidationGenericException(errors);
        }

        try {
            Integer.valueOf(split[0]);
            Integer.valueOf(split[1]);
        } catch (NumberFormatException e) {
            errors.reject(ERRCODE_INVALID_CROP_RATIO, "plugins.jacms.contentsettings.error.cropRatio.invalid");
            throw new ValidationGenericException(errors);
        }
    }

    public void validateCropRatioNotExists(List<String> cropRatios, String ratio) {
        final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(ratio, "contentSettings.ratio");

        if (cropRatios.contains(ratio)) {
            errors.reject(ERRCODE_CONFLICT_CROP_RATIO, "plugins.jacms.contentsettings.error.cropRatio.conflict");
            throw new ValidationConflictException(errors);
        }
    }

    public void validateCropRatioExists(List<String> cropRatios, String ratio) {
        if (!cropRatios.contains(ratio)) {
            throw new ResourceNotFoundException(ERRCODE_NOT_FOUND_CROP_RATIO, "cropRatio", ratio);
        }
    }

    private Map<String, String> getSystemParams() {
        try {
            return SystemParamsUtils.getParams(getConfigParameter());
        } catch (Exception ex) {
            throw new RestServerError("plugins.jacms.contentsettings.error.systemParams.read", ex);
        }
    }

    private String getConfigParameter() {
        return configManager.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
    }


    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    public void setSearchEngineManager(ICmsSearchEngineManager searchEngineManager) {
        this.searchEngineManager = searchEngineManager;
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }
}
