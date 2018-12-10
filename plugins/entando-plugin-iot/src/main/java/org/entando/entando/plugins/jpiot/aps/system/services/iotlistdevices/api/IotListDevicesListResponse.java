/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


@XmlRootElement(name = "response")
public class IotListDevicesListResponse extends AbstractApiResponse {
    
    @Override
    @XmlElement(name = "result", required = true)
    public IotListDevicesListResponseResult getResult() {
        return (IotListDevicesListResponseResult) super.getResult();
    }
    
    @Override
    protected AbstractApiResponseResult createResponseResultInstance() {
        return new IotListDevicesListResponseResult();
    }
    
}