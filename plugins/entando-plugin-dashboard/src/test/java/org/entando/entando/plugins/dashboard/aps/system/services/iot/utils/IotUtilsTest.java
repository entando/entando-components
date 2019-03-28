package org.entando.entando.plugins.dashboard.aps.system.services.iot.utils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;




public class IotUtilsTest {

  public static final String SITEWHERE_ASSIGNMENT_ID = "24306e0f-8d16-47a5-a6ed-d31d4e12f26a";

  /**
   * public static JsonElement getNestedFieldFromJson(JsonObject jsonObject, String fieldNames)
   *       throws InvalidClassException {
   *     String  fieldName = fieldNames.split("->")[0];
   *
   *     List<String> fildNameList = Arrays.asList(fieldNames.split("->"));
   *
   *     if(fildNameList.size() == 1) {
   *       return jsonObject.get(fieldName);
   *     }
   *     else if (jsonObject.get(fieldName).isJsonObject()) {
   *       fieldNames = StringUtils.join(fildNameList.subList(1,fildNameList.size()),"->");
   *       return getNestedFieldFromJson(jsonObject.getAsJsonObject(fieldName),fieldNames);
   *     }
   *     return null;
   *   }
   */
  
  @Test
  public void testGetNestedFieldsFromJson() throws IOException {
    final File file = ResourceUtils.getFile("classpath:testjson/device.json");
    final FileInputStream fis;
    fis = new FileInputStream(file);
    final String flusso = IOUtils.toString(fis, "UTF-8");
    JsonObject jsonObject = IoTUtils.getObjectFromJson(flusso, new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);
    assertTrue(StringUtils.isNotBlank(IoTUtils.getNestedFieldFromJson(jsonObject,"hardwareId").getAsString()));
    assertTrue(IoTUtils.getNestedFieldFromJson(jsonObject,"specification" + IoTConstants.JSON_FIELD_SEPARATOR + "asset").getAsJsonObject() != null);
  }
  
  @Test
  public void testGetNestedFieldFromJsonArray() throws IOException {
    final File file = ResourceUtils.getFile("classpath:testjson/device-list.json");
    final FileInputStream fis;
    fis = new FileInputStream(file);
    final String flusso = IOUtils.toString(fis, "UTF-8");
    JsonObject jsonObject = IoTUtils.getObjectFromJson(flusso, new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);
    JsonElement res = IoTUtils.getNestedFieldFromJson(jsonObject,
        "results" + IoTConstants.JSON_FIELD_SEPARATOR + "measurementsSummary");
    
    res.getAsJsonArray().forEach(x -> assertTrue(StringUtils.isNotBlank(x.getAsString())));
    
  }
  
}
