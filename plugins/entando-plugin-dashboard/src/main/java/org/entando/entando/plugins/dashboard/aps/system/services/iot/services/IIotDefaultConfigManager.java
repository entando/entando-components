package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IIotDefaultConfigManager {

  public IotDefaultConfig getIotTag() throws ApsSystemException;
  
  public IotDefaultConfig updateIotTag(IotDefaultConfig iotTag) throws ApsSystemException;
  
}
