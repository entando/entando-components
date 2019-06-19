package org.entando.entando.plugins.dashboard.web.iot;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IotDefaultConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IotDefaultConfigManager;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/plugins/dashboard/iotConfigController")
public class IotConfigController {
  
  @Autowired
  IotDefaultConfigManager iotDefaultConfigManager;

  @RestAccessControl(permission = "superuser")
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<IotDefaultConfig>> getDefaultConfig()
      throws ApsSystemException {

    IotDefaultConfig iotTag = iotDefaultConfigManager.getIotTag();
    return new ResponseEntity<>(new SimpleRestResponse(iotTag), HttpStatus.OK);

  }

  @RestAccessControl(permission = "superuser")
  @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleRestResponse<IotDefaultConfig>> updateDefaultConfig(@RequestBody IotDefaultConfig req)
      throws ApsSystemException {
    IotDefaultConfig iotDefaultConfig = iotDefaultConfigManager.updateIotTag(req);
    return new ResponseEntity(new SimpleRestResponse(iotDefaultConfig), HttpStatus.OK);
  }
}
