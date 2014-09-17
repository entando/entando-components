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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow;

/**
 * @author E.Santoboni
 */
public interface IWorkflowStepAction {
	
	/**
	 * Esegue l'azione preparatoria per la modifica degli step di un workflow.
	 * @return Il codice del risultato dell'azione.
	 */
	public String edit();
	
	/**
	 * Esegue l'azione di spostamento di posizione di uno step del workflow.
	 * @return Il codice del risultato dell'azione.
	 */
	public String moveStep();
	
	/**
	 * Esegue l'azione di rimozione di uno step del workflow.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeStep();
	
	/**
	 * Esegue l'azione di aggiunta di uno step del workflow.
	 * @return Il codice del risultato dell'azione.
	 */
	public String addStep();
	
	/**
	 * Esegue l'azione di salvataggio degli step di un workflow.
	 * @return Il codice del risultato dell'azione.
	 */
	public String save();
	
	public static final String MOVEMENT_UP_CODE = "UP";
	public static final String MOVEMENT_DOWN_CODE = "DOWN";
	
}