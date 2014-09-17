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
package com.agiletec.plugins.jpcontentfeedback.aps.system;

/**
 * This interceptor can make sure that back buttons and double clicks don't cause un-intended side affects.
 * For example, you can use this to prevent careless users who might double click on a "checkout" button at an online store.
 * This interceptor uses a fairly primitive technique for when an invalid token is found: it returns the result invalid.token.
 *
 * TypeMessages parameter to specify the type of message to associate at invalid.token result
 * The values of type are the following:
 * 	  error: return an action error message
 * 	  message: return an action message
 * 	  none: don't return message
 *
 * @author D.Cherchi
 * @deprecated use com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.system.TokenInterceptor
 */
public class TokenInterceptor extends com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.system.TokenInterceptor {
	
}
