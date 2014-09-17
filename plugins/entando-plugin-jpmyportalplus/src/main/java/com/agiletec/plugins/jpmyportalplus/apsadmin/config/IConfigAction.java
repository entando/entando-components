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
package com.agiletec.plugins.jpmyportalplus.apsadmin.config;

/**
 * @author E.Santoboni
 */
public interface IConfigAction {
	
	public String edit();
	
	/**
	 * @deprecated Use {@link #addWidget()} instead
	 */
	public String addShowlet();

	public String addWidget();
	
	/**
	 * @deprecated Use {@link #removeWidget()} instead
	 */
	public String removeShowlet();

	public String removeWidget();
	
	public String save();
	
}