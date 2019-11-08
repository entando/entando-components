/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.web.content.validator;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.web.common.annotation.ValidateString;
import org.hibernate.validator.constraints.NotEmpty;

public class BatchContentStatusRequest {

    @NotEmpty(message = "page.status.invalid")
    @ValidateString(acceptedValues = {"draft", "published"}, message = "content.status.invalid")
    private String status;
    private List<String> codes = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	@Override
	public String toString() {
		return "BatchContentStatusRequest [status=" + status + ", codes=" + codes + "]";
	}

}
