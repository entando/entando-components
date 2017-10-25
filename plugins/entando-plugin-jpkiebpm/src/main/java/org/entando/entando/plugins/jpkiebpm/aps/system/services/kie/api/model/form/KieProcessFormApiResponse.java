package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;

@XmlRootElement(name = "response")
public class KieProcessFormApiResponse extends AbstractApiResponse {

	@Override
	@XmlElement(name = "result", required = true)
	public KieProcessFormApiResponseResult getResult() {
		return (KieProcessFormApiResponseResult) super.getResult();
	}

	public void setResult(KieProcessFormApiResponseResult result) {
		super.setResult(result);
	}

	@Override
	protected AbstractApiResponseResult createResponseResultInstance() {
		return new KieProcessFormApiResponseResult();
	}
}
