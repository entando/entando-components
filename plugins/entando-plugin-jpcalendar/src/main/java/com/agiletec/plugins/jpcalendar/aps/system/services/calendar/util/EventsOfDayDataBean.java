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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util;

import java.util.Date;
import java.util.Set;

/**
 * @author E.Santoboni
 */
public interface EventsOfDayDataBean {
	
	public String getAttributeNameEnd();
	
	public String getAttributeNameStart();
	
	public String getContentType();
	
	public Date getRequiredDay();
	
	public Set<String> getAllowedGroups();
	
	public void setAllowedGroups(Set<String> allowedGroups);
	
}
