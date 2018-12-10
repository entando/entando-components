/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.api;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


public class IotConfigResponseResult extends AbstractApiResponseResult {
    
    @Override
    @XmlElement(name = "iotConfig", required = false)
    public JAXBIotConfig getResult() {
        return (JAXBIotConfig) this.getMainResult();
    }
    
}