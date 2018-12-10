/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;

@XmlSeeAlso({JAXBIotListDevices.class})
public class IotListDevicesListResponseResult extends AbstractApiResponseResult {
    
    @XmlElement(name = "items", required = false)
    public ListResponse<JAXBIotListDevices> getResult() {
        if (this.getMainResult() instanceof Collection) {
            List<JAXBIotListDevices> iotListDevicess = new ArrayList<JAXBIotListDevices>();
            iotListDevicess.addAll((Collection<JAXBIotListDevices>) this.getMainResult());
            ListResponse<JAXBIotListDevices> entity = new ListResponse<JAXBIotListDevices>(iotListDevicess) {};
            return entity;
        }
        return null;
    }

}