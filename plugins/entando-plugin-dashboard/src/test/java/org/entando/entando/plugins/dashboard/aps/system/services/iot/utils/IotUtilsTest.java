package org.entando.entando.plugins.dashboard.aps.system.services.iot.utils;

import static org.junit.Assert.assertNotNull;
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

  @Test
  public void testGetNestedFieldFromJsonArray2() throws IOException {
    final File file = ResourceUtils.getFile("classpath:testjson/kaaMeasurementSchema.json");
    final FileInputStream fis;
    fis = new FileInputStream(file);
    final String flusso = IOUtils.toString(fis, "UTF-8");
    JsonObject jsonObject = IoTUtils.getObjectFromJson(flusso, new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);
    JsonElement res = IoTUtils.getNestedFieldFromJson(jsonObject,
        "body" + IoTConstants.JSON_FIELD_SEPARATOR + "fields");

    assertNotNull(res);
    System.out.println(res);
  }
  
}
