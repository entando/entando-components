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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.scheduler;

/**
 * Classe astratta base per l'implementazione del task 
 * da associare allo scheduler.
 * @author M.Casari
 */
public abstract class Task {
	
	public Task() {}
	
	public Task(Object taskInput){}
	
	public abstract void execute();
	
}
