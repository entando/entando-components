/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessInstance;

/**
 * @author E.Santoboni
 */

@XmlSeeAlso({ KieProcessInstance.class })
public class KieProcessInstanceListApiResponseResult extends AbstractApiResponseResult {

	@Override
	@XmlElement(name = "items", required = false)
	public ListResponse<KieProcessInstance> getResult() {
		if (this.getMainResult() instanceof Collection) {
			List<KieProcessInstance> proocessList = new ArrayList<KieProcessInstance>();
			proocessList.addAll((Collection<KieProcessInstance>) this.getMainResult());
			ListResponse<KieProcessInstance> entity = new ListResponse<KieProcessInstance>(proocessList) {
			};
			return entity;
		}
		return null;
	}

}
