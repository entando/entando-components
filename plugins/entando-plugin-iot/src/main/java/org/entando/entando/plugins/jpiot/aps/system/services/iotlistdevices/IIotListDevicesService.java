/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices;

import  org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.model.IotListDevicesDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.jpiot.web.iotlistdevices.model.IotListDevicesRequest;

public interface IIotListDevicesService {

    public String BEAN_NAME = "jpiotIotListDevicesService";

    public PagedMetadata<IotListDevicesDto> getIotListDevicess(RestListRequest requestList);

    public IotListDevicesDto updateIotListDevices(IotListDevicesRequest iotListDevicesRequest);

    public IotListDevicesDto addIotListDevices(IotListDevicesRequest iotListDevicesRequest);

    public void removeIotListDevices(int id);

    public IotListDevicesDto getIotListDevices(int  id);

}

