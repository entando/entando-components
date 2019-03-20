package org.entando.entando.plugins.dashboard.aps.system.services.iot.dto;

public class LogAppender {

  String name;
  String description;
  String createdUsername;
  String createdTime;
  String id;
  String applicationId;
  String pluginTypeName;
  String pluginClassName;
  String jsonConfiguration;
  String applicationToken;
  String tenantId;
  String minLogSchemaVersion;
  String maxLogSchemaVersion;
  String confirmDelivery;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCreatedUsername() {
    return createdUsername;
  }

  public void setCreatedUsername(String createdUsername) {
    this.createdUsername = createdUsername;
  }

  public String getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(String createdTime) {
    this.createdTime = createdTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getPluginTypeName() {
    return pluginTypeName;
  }

  public void setPluginTypeName(String pluginTypeName) {
    this.pluginTypeName = pluginTypeName;
  }

  public String getPluginClassName() {
    return pluginClassName;
  }

  public void setPluginClassName(String pluginClassName) {
    this.pluginClassName = pluginClassName;
  }

  public String getJsonConfiguration() {
    return jsonConfiguration;
  }

  public void setJsonConfiguration(String jsonConfiguration) {
    this.jsonConfiguration = jsonConfiguration;
  }

  public String getApplicationToken() {
    return applicationToken;
  }

  public void setApplicationToken(String applicationToken) {
    this.applicationToken = applicationToken;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getMinLogSchemaVersion() {
    return minLogSchemaVersion;
  }

  public void setMinLogSchemaVersion(String minLogSchemaVersion) {
    this.minLogSchemaVersion = minLogSchemaVersion;
  }

  public String getMaxLogSchemaVersion() {
    return maxLogSchemaVersion;
  }

  public void setMaxLogSchemaVersion(String maxLogSchemaVersion) {
    this.maxLogSchemaVersion = maxLogSchemaVersion;
  }

  public String getConfirmDelivery() {
    return confirmDelivery;
  }

  public void setConfirmDelivery(String confirmDelivery) {
    this.confirmDelivery = confirmDelivery;
  }

  @Override
  public String toString() {
    return "LogAppender{" +
        "name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", createdUsername='" + createdUsername + '\'' +
        ", createdTime='" + createdTime + '\'' +
        ", id='" + id + '\'' +
        ", applicationId='" + applicationId + '\'' +
        ", pluginTypeName='" + pluginTypeName + '\'' +
        ", pluginClassName='" + pluginClassName + '\'' +
        ", jsonConfiguration='" + jsonConfiguration + '\'' +
        ", applicationToken='" + applicationToken + '\'' +
        ", tenantId='" + tenantId + '\'' +
        ", minLogSchemaVersion='" + minLogSchemaVersion + '\'' +
        ", maxLogSchemaVersion='" + maxLogSchemaVersion + '\'' +
        ", confirmDelivery='" + confirmDelivery + '\'' +
        '}';
  }
}
