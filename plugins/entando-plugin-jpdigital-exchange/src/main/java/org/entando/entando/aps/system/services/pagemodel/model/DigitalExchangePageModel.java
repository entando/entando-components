/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.pagemodel.model;

import com.agiletec.aps.system.services.pagemodel.PageModel;
import javax.xml.bind.annotation.XmlTransient;

public class DigitalExchangePageModel extends PageModel {

    private String digitalExchange;

    @XmlTransient
    public String getDigitalExchange() {
        return digitalExchange;
    }

    public void setDigitalExchange(String digitalExchange) {
        this.digitalExchange = digitalExchange;
    }
}
