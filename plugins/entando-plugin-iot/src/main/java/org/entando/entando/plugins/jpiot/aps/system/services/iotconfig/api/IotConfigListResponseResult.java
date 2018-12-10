/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;

@XmlSeeAlso({JAXBIotConfig.class})
public class IotConfigListResponseResult extends AbstractApiResponseResult {
    
    @XmlElement(name = "items", required = false)
    public ListResponse<JAXBIotConfig> getResult() {
        if (this.getMainResult() instanceof Collection) {
            List<JAXBIotConfig> iotConfigs = new ArrayList<JAXBIotConfig>();
            iotConfigs.addAll((Collection<JAXBIotConfig>) this.getMainResult());
            ListResponse<JAXBIotConfig> entity = new ListResponse<JAXBIotConfig>(iotConfigs) {};
            return entity;
        }
        return null;
    }

}