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
package com.agiletec.plugins.jpremoteresourceloader.apsadmin.resource;

public interface IRemoteResourceLoaderAction {
	
	/**
	 * Executes the request for new resource definition.
	 * @return the action result code.
	 */
	public String newResource();
	
	/**
	 * Executes the save operation for the resource
	 * @return the action result code.
	 */
	public String save();
	
	/**
	 * Executes the action for the association of the category 
	 * to the resource during edit operation.
	 * @return the action result code.
	 */
	public String joinCategory();
	
	/**
	 * Executes the action for the association removal of the category 
	 * from the resource during edit operation.
	 * @return the action result code.
	 */
	public String removeCategory();
	
}
