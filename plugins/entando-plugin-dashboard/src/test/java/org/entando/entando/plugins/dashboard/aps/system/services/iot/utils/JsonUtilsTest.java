package org.entando.entando.plugins.dashboard.aps.system.services.iot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsonUtilsTest {

  @Test
  public void testGetNestedFieldsFromJson() throws IOException {
    final File file = ResourceUtils.getFile("classpath:testjson/device.json");
    final FileInputStream fis;
    fis = new FileInputStream(file);
    final String flusso = IOUtils.toString(fis, "UTF-8");
    JsonObject jsonObject = JsonUtils.getObjectFromJson(flusso, new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);
    assertTrue(StringUtils.isNotBlank(JsonUtils.getNestedFieldFromJson(jsonObject,"hardwareId").getAsString()));
    assertTrue(JsonUtils.getNestedFieldFromJson(jsonObject,"specification" + IoTConstants.JSON_FIELD_SEPARATOR + "asset").getAsJsonObject() != null);
  }
  
  @Test
  public void testGetNestedFieldFromJsonArray() throws IOException {
    final File file = ResourceUtils.getFile("classpath:testjson/device-list.json");
    final FileInputStream fis;
    fis = new FileInputStream(file);
    final String flusso = IOUtils.toString(fis, "UTF-8");
    JsonObject jsonObject = JsonUtils.getObjectFromJson(flusso, new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);
    JsonElement res = JsonUtils.getNestedFieldFromJson(jsonObject,
        "results" + IoTConstants.JSON_FIELD_SEPARATOR + "measurementsSummary");
    
    res.getAsJsonArray().forEach(x -> assertTrue(StringUtils.isNotBlank(x.getAsString())));
  }

  @Test
  public void testGetNestedFieldFromJsonArray2() throws IOException {
    final File file = ResourceUtils.getFile("classpath:testjson/kaaMeasurementSchema.json");
    final FileInputStream fis;
    fis = new FileInputStream(file);
    final String flusso = IOUtils.toString(fis, "UTF-8");
    JsonObject jsonObject = JsonUtils.getObjectFromJson(flusso, new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);
    JsonElement res = JsonUtils.getNestedFieldFromJson(jsonObject,
        "body" + IoTConstants.JSON_FIELD_SEPARATOR + "fields");

    assertNotNull(res);
    System.out.println(res);
  }
  
  
  @Test
  public void testGetMapFromJson() {
    JsonObject json = new JsonObject();
    json.addProperty("x", "y");
    json.addProperty("z", "w");

    Map<String, Object> x = JsonUtils.getMapFromJson(json);

    assertEquals(x.get("x"), "y");
    assertEquals(x.get("z"), "w");
  }
  
  
  @Test
  public void getPathsFromJson() {
    JsonObject json = new JsonObject();
    json.addProperty("a", "aa");
    json.addProperty("b", "bb");
    JsonObject nested = new JsonObject();
    nested.addProperty("cc", "dd");
    json.add("c", nested);

    HashMap<String, String> out = JsonUtils.getJsonPaths(json, new HashMap<String, String>(),"");
    out.forEach((r,c) -> System.out.println(r+c));
  }
  
  @Test
  public void getPathsFromLong() throws IOException {
    String x = getStringFromFile("classpath:testjson/device.json");
    JsonElement json = new Gson().fromJson(x, JsonElement.class);
    HashMap<String, String> out = JsonUtils.getJsonPaths(json, new HashMap<String, String>(),"");
    out.forEach((r,c) -> System.out.println(r+c));
  }

  @Test
  public void getPathsFromList() throws IOException {
    String x = getStringFromFile("classpath:testjson/device-list.json");
    JsonElement json = new Gson().fromJson(x, JsonElement.class);
    HashMap<String, String> out = JsonUtils.getJsonPaths(json, new HashMap<String, String>(),"");
    out.forEach((r,c) -> System.out.println(r+c));
  }


  private String getStringFromFile(String classpath) throws IOException {
    final File file = ResourceUtils.getFile(classpath);
    final FileInputStream fis;
    fis = new FileInputStream(file);
    return IOUtils.toString(fis, "UTF-8");
  }
}
