/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig;

import  org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.model.IotConfigDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.jpiot.web.iotconfig.model.IotConfigRequest;

public interface IIotConfigService {

    public String BEAN_NAME = "jpiotIotConfigService";

    public PagedMetadata<IotConfigDto> getIotConfigs(RestListRequest requestList);

    public IotConfigDto updateIotConfig(IotConfigRequest iotConfigRequest);

    public IotConfigDto addIotConfig(IotConfigRequest iotConfigRequest);

    public void removeIotConfig(int id);

    public IotConfigDto getIotConfig(int  id);

}

