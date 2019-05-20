package org.entando.entando.plugins.dashboard.aps.system.services.iot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceType;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.JSON_FIELD_SEPARATOR;

public class JsonUtils {

  public static <T> T getObjectFromJson(String jsonString, Type type, Class<T> tClass) {
    T object = new Gson().fromJson(jsonString, type);
    return tClass.cast(object);
  }

  public static <T> T getObjectFromJson(JsonElement json, Type type, Class<T> tClass) {
    String jsonString = new Gson().toJson(json);
    return getObjectFromJson(jsonString, type,tClass);
  }

  public static JsonElement getNestedFieldFromJson(JsonObject jsonObject, String fieldNames){

    String  fieldName = fieldNames.split(JSON_FIELD_SEPARATOR)[0];

    List<String> fildNameList = Arrays.asList(fieldNames.split(JSON_FIELD_SEPARATOR));

    if(fildNameList.size() == 1) {
      return jsonObject.get(fieldName);
    }

    if(jsonObject.get(fieldName) == null) {
      return null;
    }

    else if (jsonObject.get(fieldName).isJsonObject()) {
      fieldNames = StringUtils.join(fildNameList.subList(1,fildNameList.size()), JSON_FIELD_SEPARATOR);
      return getNestedFieldFromJson(jsonObject.getAsJsonObject(fieldName),fieldNames);
    }

    else if(jsonObject.get(fieldName).isJsonPrimitive() && fildNameList.size() > 0) {
      jsonObject = new Gson().fromJson(jsonObject.get(fieldName).getAsString(), JsonObject.class);
      fieldNames = StringUtils.join(fildNameList.subList(1,fildNameList.size()), JSON_FIELD_SEPARATOR);
      return getNestedFieldFromJson(jsonObject,fieldNames);
    }

    else if (jsonObject.get(fieldName).isJsonArray() && fildNameList.size() == 2) {
      JsonArray jsonArray = new JsonArray();
      for (JsonElement json : jsonObject.get(fildNameList.get(0)).getAsJsonArray()) {

        if (json.isJsonObject() && json.getAsJsonObject().get(fildNameList.get(1)) != null) {
          jsonArray.add(json.getAsJsonObject().get(fildNameList.get(1)));
        }
      }
      return jsonArray;
    }

    return null;
  }

  public static JsonElement getNestedFieldFromJson(String jsonObject, String fieldNames) {
    JsonObject json = getObjectFromJson(jsonObject,new TypeToken<JsonObject>(){}.getType(),JsonObject.class);
    return getNestedFieldFromJson(json,fieldNames);
  }

  public static JsonObject getJsonFromArrayWhere(JsonArray array, String matching, String value) {
    for (JsonElement jsonElement :array) {
      if(jsonElement.isJsonObject()) {
        if(jsonElement.getAsJsonObject().get(matching).getAsString().equals(value)) {
          return jsonElement.getAsJsonObject();
        }
      }
    }
    return null;
  }

  public static String getStringFromJsonArrayWhere(JsonArray array, String matching, String value, String fieldToGet) {
    for (JsonElement jsonElement : array) {
      if (jsonElement.isJsonObject()) {
        if (jsonElement.getAsJsonObject().get(matching).getAsString().equals(value)) {
          return jsonElement.getAsJsonObject().get(fieldToGet).getAsString();
        }
      }
    }
    return null;
  }

  public static Map<String, Object> getMapFromJson (JsonObject payload) {
    Gson gson = new Gson();
    return gson.fromJson(gson.toJson(payload), HashMap.class);
  }

  public static HashMap<String,String> getJsonPaths(JsonElement json, HashMap<String,String> out, String actualPath) {
    String escapedDelimiter = "\\.";
    String delimiter = ".";
    if(json.isJsonObject()) {
      JsonObject jsonObj = json.getAsJsonObject();
      for (String key : jsonObj.keySet()) {
        if (jsonObj.get(key).isJsonObject()) {
          actualPath += key + delimiter;
          JsonObject value = jsonObj.get(key).getAsJsonObject();
          getJsonPaths(value, out, actualPath);
        }
        else if (jsonObj.get(key).isJsonArray()) {
          actualPath += key;
          int index = 0;
          for (JsonElement elem : jsonObj.get(key).getAsJsonArray()) {
            getJsonPaths(elem,out,actualPath + "["+ index + "]"+ delimiter);
            ++index;
          }
        }
        else {
          actualPath += key;
          out.put(actualPath, jsonObj.get(key).getClass().getSimpleName());
        }
        if(actualPath.split(escapedDelimiter).length > 1) {
          List<String> x = Arrays.asList(actualPath.split(escapedDelimiter));
          x = x.subList(0, x.size()-1);
          actualPath = x.stream().map(s -> s += delimiter).collect(
              Collectors.joining());
        }else {
          actualPath = "";
        }
      }
    }
    else if(json.isJsonPrimitive()) {
      actualPath += json.getAsString();
      out.put(actualPath, json.getAsJsonPrimitive().getClass().getSimpleName());
    }
    else if (json.isJsonArray()) {
      int index = 0;
      for (JsonElement elem : json.getAsJsonArray()) {
        getJsonPaths(elem,out,actualPath + "["+ index + "]");
        index++;
      }
    }
    return out;
  }

}
