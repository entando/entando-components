package org.entando.entando.plugins.jacms.aps.system.services.contentsettings;

import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContentSettingsServiceUnitTest {

    private ContentSettingsService contentSettingsService;

    private List<String> cropRatios;
    private Map<String,List<String>> metadata;

    @Before
    public void setup() {
        cropRatios =  Arrays.stream(new String[] { "4:3", "16:9" } ).collect(Collectors.toList());
        metadata = new HashMap<>();
        metadata.put("my_key", Arrays.stream(new String[]{ "mapping_1", "mapping_2"}).collect(Collectors.toList()));


        contentSettingsService = new ContentSettingsService();
    }

    @Test
    public void testCropRatioValidation() {
        contentSettingsService.validateCropRatio(cropRatios, "4:4");
        contentSettingsService.validateCropRatio(cropRatios, "1000:1");
    }

    @Test(expected = ValidationGenericException.class)
    public void testCropRatioInvalidFormat1() {
        contentSettingsService.validateCropRatio(cropRatios, "alfa:3");
    }

    @Test(expected = ValidationGenericException.class)
    public void testCropRatioInvalidFormat2() {
        contentSettingsService.validateCropRatio(cropRatios, "4;3");
    }

    @Test(expected = ValidationConflictException.class)
    public void testCropRatioConflict() {
        contentSettingsService.validateCropRatio(cropRatios, "4:3");
    }

    @Test
    public void testMetadataValidation() {
        contentSettingsService.validateMetadata(metadata, "new_key", "new_mapping1,new_mapping2");
        contentSettingsService.validateMetadata(metadata, "new_key2", "new_mapping3, new_mapping4");
    }

    @Test(expected = ValidationGenericException.class)
    public void testMetadataInvalidFormat1() {
        contentSettingsService.validateMetadata(metadata, "new_key", "NEW_mapping1,new_mapping2");
    }

    @Test(expected = ValidationGenericException.class)
    public void testMetadataInvalidFormat2() {
        contentSettingsService.validateMetadata(metadata, "NEW_key", "new_mapping1,new_mapping2");
    }

    @Test(expected = ValidationGenericException.class)
    public void testMetadataInvalidFormat3() {
        contentSettingsService.validateMetadata(metadata, "new_key", "new-mapping1,new_mapping2");
    }

    @Test(expected = ValidationGenericException.class)
    public void testMetadataInvalidFormat4() {
        contentSettingsService.validateMetadata(metadata, "new-key", "new_mapping1,new_mapping2");
    }

    @Test(expected = ValidationGenericException.class)
    public void testMetadataInvalidFormat5() {
        contentSettingsService.validateMetadata(metadata, "new_key", "new-mapping!,new_mapping2");
    }

    @Test(expected = ValidationConflictException.class)
    public void testMetadataConflict() {
        contentSettingsService.validateMetadata(metadata, "my_key", "new_mapping1,new_mapping2");
    }
}
