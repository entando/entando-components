package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.parser.IotDefaultConfigDOM;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.TagManagerConstants.IOT_CONNECTOR_ITEM;

public class IotDefaultConfigManager extends AbstractService implements IIotDefaultConfigManager {

  private static final Logger _logger = LoggerFactory.getLogger(IotDefaultConfigManager.class);

  @Override
  public void init() throws Exception {
    try {
      this.loadConfigs();
      _logger.debug("{} ready: active {}", this.getClass().getName(), this.getActive());
    } catch (Throwable t) {
      _logger.error("{} Manager: Error on initialization", this.getClass().getName(), t);
      this.setActive(false);
    }
  }

  private void loadConfigs() throws ApsSystemException {
    try {
      ConfigInterface configManager = this.getConfigManager();
      String xml = configManager.getConfigItem(IOT_CONNECTOR_ITEM);
      if (xml == null) {
        throw new ApsSystemException("Configuration item not present: " + IOT_CONNECTOR_ITEM);
      }
      IotDefaultConfigDOM configDOM = new IotDefaultConfigDOM();
      this.setConfig(configDOM.extractConfig(xml));
    } catch (Throwable t) {
      _logger.error("Error in loadConfigs", t);
      throw new ApsSystemException("Error in loadConfigs", t);
    }
  }
  
  @Override
  public IotDefaultConfig getIotTag() throws ApsSystemException {
    try {
      return (IotDefaultConfig) this._config.clone();
    } catch (Throwable t) {
      _logger.error("Error loading IotTag service configuration", t);
      throw new ApsSystemException("Error loading IotTag service configuration", t);
    }
  }

  @Override
  public void updateIotTag(IotDefaultConfig iotTag) throws ApsSystemException {
    try {
      String xml = new IotDefaultConfigDOM().createConfigXml(iotTag);
      this.getConfigManager().updateConfigItem(IOT_CONNECTOR_ITEM, xml);
      this.setConfig(iotTag);
    } catch (Throwable t) {
      _logger.error("Error updating configs", t);
      throw new ApsSystemException("Error updating configs", t);
    }
  }

  public ConfigInterface getConfigManager() {
    return _configManager;
  }

  public void setConfigManager(ConfigInterface _configManager) {
    this._configManager = _configManager;
  }

  public IotDefaultConfig getConfig() {
    return _config;
  }

  public void setConfig(IotDefaultConfig _config) {
    this._config = _config;
  }

  public Boolean getActive() {
    return _active;
  }

  public void setActive(Boolean _active) {
    this._active = _active;
  }

  private ConfigInterface _configManager;
  private IotDefaultConfig _config;
  private Boolean _active;
}
