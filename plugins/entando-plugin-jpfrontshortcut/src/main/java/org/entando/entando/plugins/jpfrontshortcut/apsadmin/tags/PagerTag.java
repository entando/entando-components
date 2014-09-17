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
package org.entando.entando.plugins.jpfrontshortcut.apsadmin.tags;

import com.agiletec.apsadmin.tags.AdminPagerTag;
import com.agiletec.apsadmin.tags.util.AdminPagerTagHelper;
import org.entando.entando.plugins.jpfrontshortcut.apsadmin.tags.util.PagerTagHelper;

/**
 * @author E.Santoboni
 */
public class PagerTag extends AdminPagerTag {
	
	@Override
	protected AdminPagerTagHelper getPagerHelper() {
		return new PagerTagHelper();
	}
	
}
