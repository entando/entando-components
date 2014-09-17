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
package com.agiletec.plugins.jpfastcontentedit.aps.tags;

import com.agiletec.aps.tags.InternalServletTag;
import com.agiletec.plugins.jpfastcontentedit.aps.system.JpFastContentEditSystemConstants;

/**
 * Tag for widget "jpfastcontentedit_formAction".
 */
public class FastContentEditFormActionTag extends InternalServletTag {
	
	@Override
	public String getActionPath() {
		return JpFastContentEditSystemConstants.ACTION_PATH_FOR_CONTENT_EDIT;
	}
	
}
