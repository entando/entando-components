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

package org.entando.entando.plugins.jpkiebpm.aps.tags;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.InternalServletTag;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.BpmDatatableWidgetAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;

public class BpmDatatableInternalServletTag extends InternalServletTag {

    private static final Logger _logger = LoggerFactory.getLogger(BpmDatatableInternalServletTag.class);

    @Override
    public String getActionPath() {
        return "/ExtStr2/do/bpm/FrontEnd/Form/viewForm.action";
    }

    @Override
    protected void includeWidget(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget widget) throws ServletException, IOException {

        if (StringUtils.isNotBlank(this.getWidgetConfigInfoIdVar())) {
            final String configId = widget.getConfig().getProperty(BpmDatatableWidgetAction.PROP_NAME_WIDGET_INFO_ID);
            this.pageContext.setAttribute(widgetConfigInfoIdVar, configId);
        }

    }

    @Override
    public void release() {
        super.release();
        this.widgetConfigInfoIdVar = null;
    }

    public String getWidgetConfigInfoIdVar() {
        return widgetConfigInfoIdVar;
    }

    public void setWidgetConfigInfoIdVar(String widgetConfigInfoIdVar) {
        this.widgetConfigInfoIdVar = widgetConfigInfoIdVar;
    }

    private String widgetConfigInfoIdVar;


}
