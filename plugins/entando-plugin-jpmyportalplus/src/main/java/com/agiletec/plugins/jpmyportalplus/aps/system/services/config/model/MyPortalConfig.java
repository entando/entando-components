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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model;

import java.util.Set;

/**
 * Contains the map of the configuration of jpmyportalplus. 
 * @author M. Minnai - E.Santoboni
 */
public class MyPortalConfig {
	
	@Deprecated
	public Set<String> getAllowedShowlets() {
		return this.getAllowedWidgets();
	}
	@Deprecated
	public void setAllowedShowlets(Set<String> allowedShowlets) {
		this.setAllowedWidgets(allowedShowlets);
	}
	
	public Set<String> getAllowedWidgets() {
		return _allowedWidgets;
	}
	public void setAllowedWidgets(Set<String> allowedWidgets) {
		this._allowedWidgets = allowedWidgets;
	}
	
	private Set<String> _allowedWidgets;
	
}