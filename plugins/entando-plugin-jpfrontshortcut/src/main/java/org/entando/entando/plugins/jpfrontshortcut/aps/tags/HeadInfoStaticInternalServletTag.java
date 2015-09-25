/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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