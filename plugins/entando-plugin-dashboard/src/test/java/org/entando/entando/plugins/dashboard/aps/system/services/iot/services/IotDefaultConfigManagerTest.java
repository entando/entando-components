package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.parser.IotDefaultConfigDOM;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class IotDefaultConfigManagerTest {
  
  @Test
  public void testCreateConfigXml() throws ApsSystemException {
    IotDefaultConfig iotTag = new IotDefaultConfig();
    iotTag.setDashboardId(123L);
    iotTag.setDatasourceCode("123L");
    IotDefaultConfigDOM iotTagDOM = new IotDefaultConfigDOM();
    String configXml = iotTagDOM.createConfigXml(iotTag);
    assertTrue(StringUtils.isNotBlank(configXml));
  }
  
  @Test
  public void testExtractConfigXml() throws ApsSystemException {
    IotDefaultConfig iotTag = new IotDefaultConfig();
    iotTag.setDashboardId(123L);
    iotTag.setDatasourceCode("123L");
    IotDefaultConfigDOM iotTagDOM = new IotDefaultConfigDOM();
    String configXml = iotTagDOM.createConfigXml(iotTag);
    IotDefaultConfig iotDefaultConfig = iotTagDOM.extractConfig(configXml);
    assertEquals(123L, (long) iotDefaultConfig.getDashboardId());
    assertEquals("123L", iotDefaultConfig.getDatasourceCode());
  }

  
}
