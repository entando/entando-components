/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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