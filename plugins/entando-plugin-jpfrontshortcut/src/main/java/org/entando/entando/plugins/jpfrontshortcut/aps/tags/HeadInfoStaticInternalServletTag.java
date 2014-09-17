/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpfrontshortcut.aps.tags;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.tags.InternalServletTag;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 * Create the head info for front shortcut plugin. 
 * This tag has to be used only into widget decorator (footer).
 * @author E.Santoboni
 */
public class HeadInfoStaticInternalServletTag extends InternalServletTag {
	
	@Override
	protected void includeWidget(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget widget) throws ServletException, IOException {
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		PageModel pageModel = page.getModel();
		int frames = pageModel.getFrames().length;
		if (frames == (currentFrame.intValue() + 1)) {
			super.includeWidget(reqCtx, responseWrapper, widget);
		}
	}
	
	@Override
	public String getActionPath() {
		return "/ExtStr2/do/jpfrontshortcut/introHeader";
	}
	
	@Override
	public boolean isStaticAction() {
		return true;
	}
	
}