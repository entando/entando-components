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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.task;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTaskDetail;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author E.Santoboni
 */
public class KieTaskDetailApiResponseResult extends AbstractApiResponseResult {

	@Override
	@XmlElement(name = "mainForm")
	public KieTaskDetail getResult() {
		return (KieTaskDetail) this.getMainResult();
	}

}
