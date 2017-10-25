package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.task;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class KieTaskDetailApiResponse extends AbstractApiResponse {

    @Override
    @XmlElement(name = "result", required = true)
    public KieTaskDetailApiResponseResult getResult() {
        return (KieTaskDetailApiResponseResult) super.getResult();
    }

    public void setResult(KieTaskDetailApiResponseResult result) {
        super.setResult(result);
    }

    @Override
    protected AbstractApiResponseResult createResponseResultInstance() {
        return new KieTaskDetailApiResponseResult();
    }
}
