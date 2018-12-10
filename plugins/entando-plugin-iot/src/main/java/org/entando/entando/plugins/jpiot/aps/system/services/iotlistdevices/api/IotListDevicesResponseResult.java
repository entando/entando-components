/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.api;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


public class IotListDevicesResponseResult extends AbstractApiResponseResult {
    
    @Override
    @XmlElement(name = "iotListDevices", required = false)
    public JAXBIotListDevices getResult() {
        return (JAXBIotListDevices) this.getMainResult();
    }
    
}