package org.entando.entando.plugins.jpkiebpm.aps.tags;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.BpmFormWidgetAction;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.InternalServletTag;

public class BpmFormInternalServletTag extends InternalServletTag {

	@Override
	public String getActionPath() {
		return "/ExtStr2/do/bpm/FrontEnd/Form/viewForm.action";
	}

	@Override
	protected void includeWidget(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget widget) throws ServletException, IOException {
		if (StringUtils.isNotBlank(this.getWidgetConfigInfoIdVar())) {
			this.pageContext.setAttribute(this.getWidgetConfigInfoIdVar(), widget.getConfig().getProperty(BpmFormWidgetAction.PROP_NAME_WIDGET_INFO_ID));
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
