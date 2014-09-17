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
package com.agiletec.plugins.jpfacetnav.apsadmin.portal.specialwidget;

/**
 * @author E.Santoboni
 */
public interface IFacetNavTreeWidgetAction extends IFacetNavResultWidgetAction {
	
	/**
	 * Add a facet to the associated facet nodes
	 * @return The code describing the result of the operation.
	 */
	public String joinFacet();
	
	/**
	 * Remove a facet from the associated facet nodes
	 * @return The code describing the result of the operation.
	 */
	public String removeFacet();
	
}