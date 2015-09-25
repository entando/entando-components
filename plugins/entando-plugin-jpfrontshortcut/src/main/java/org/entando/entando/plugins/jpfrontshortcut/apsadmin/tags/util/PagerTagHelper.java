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
package org.entando.entando.plugins.jpfrontshortcut.apsadmin.tags.util;

import javax.servlet.ServletRequest;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.tags.util.AdminPagerTagHelper;
import org.entando.entando.plugins.jpfrontshortcut.apsadmin.util.ApsAjaxActionParamsUtil;

/**
 * Helper class for the pager for administration interface.
 * @author E.Santoboni
 */
public class PagerTagHelper extends AdminPagerTagHelper {
	
	@Override
	protected int getItemNumber(String pagerId, ServletRequest request) {
		String stringItem = null;
		String marker = (null != pagerId && pagerId.trim().length() > 0) ? pagerId : "pagerItem"; 
		String[] params = ApsAjaxActionParamsUtil.getApsParams(marker, "_", request);
		if (params != null && params.length == 2) {
			stringItem = params[1];
		} else {
			stringItem = request.getParameter(marker);
		}
		int item = 0;
		if (stringItem != null) {
			try {
				item = Integer.parseInt(stringItem);
			} catch (NumberFormatException e) {
				ApsSystemUtils.getLogger().error("Errore in parsing stringItem " + stringItem);
			}
		}
		return item;
	}
	
}