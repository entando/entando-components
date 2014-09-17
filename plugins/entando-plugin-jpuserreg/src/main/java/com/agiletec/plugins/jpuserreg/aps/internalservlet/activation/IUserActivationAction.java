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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.activation;

/**
 * Interface for Struts Action to manage account activation
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 * */
public interface IUserActivationAction {
	
	/**
	 * Initialize the funtionality and redirect user to portal homepage if he has 
	 * activated already, or to an error page if token is consumed or wrong
	 * @return The action result.
	 */
	public String initActivation();
	
	/**
	 * Active account with information provided, if token is valid.
	 * Load also default roles and groups defined in the config.
	 * @return The action result.
	 */
	public String activate();
	
	/**
	 * Reactive account with information provided, if token is valid.
	 * @return The action result.
	 */
	public String reactivate();

	/**
	 * Initialize the funtionality and redirect user to portal homepage if he has 
	 * reactivated already, or to an error page if token is consumed or wrong
	 * @return The action result.
	 */
	public String initReactivation();
	
}